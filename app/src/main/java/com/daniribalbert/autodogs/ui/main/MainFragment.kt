package com.daniribalbert.autodogs.ui.main

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.daniribalbert.autodogs.R
import com.daniribalbert.autodogs.databinding.MainFragmentBinding
import com.daniribalbert.autodogs.ui.base.BaseFragment
import com.daniribalbert.autodogs.utils.extensions.showShortToast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


class MainFragment : BaseFragment() {

    private val viewModel: MainViewModel by sharedViewModel()
    private lateinit var binding: MainFragmentBinding

    private lateinit var listener: FragmentInteractionListener

    private var lastSelectedImage: String? = null

    private val adapter by lazy { MainAdapter(::onImageClicked, ::onImageLongClicked) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as FragmentInteractionListener
        } catch (exception: Exception) {
            throw IllegalArgumentException("$context should implement ${FragmentInteractionListener::class.java}!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAdd.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.loadNewImage() }
        binding.listImages.adapter = adapter

        viewModel.dogImageLiveData.observe(viewLifecycleOwner) {
            binding.loading.visibility = View.GONE
            it?.result?.let { images ->
                images.forEach { imageUrl ->
                    val changed = adapter.addImage(imageUrl)
                    if (changed && isListAtTop()) binding.listImages.scrollToPosition(0)
                }
            }
            it?.error?.let { error ->
                handleError(error)
            }
        }
    }

    private fun isListAtTop(): Boolean {
        return (binding.listImages.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
    }

    private fun onImageClicked(imageUrl: String) {
        listener.onShowFullScreenImage(imageUrl)
    }

    private fun onImageLongClicked(imageUrl: String) {
        val ctx = context ?: return
        AlertDialog.Builder(ctx).setMessage(R.string.save_share_msg)
            .setPositiveButton(R.string.save) { _, _ -> saveImage(imageUrl) }
            .setNegativeButton(R.string.share) { _, _ -> shareImage(imageUrl) }
            .setNeutralButton(R.string.cancel, null)
            .show()
    }

    private fun saveImage(imageUrl: String) {
        lastSelectedImage = imageUrl
        if (checkWritePermissions()) {
            getImageBitmap(imageUrl) { saveImageBitmap(it) }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_CHECK_PERMISSIONS_SAVE
            )
        }
    }

    private fun shareImage(imageUrl: String) {
        lastSelectedImage = imageUrl
        val ctx = context ?: return
        if (checkWritePermissions()) {
            getImageBitmap(imageUrl) {
                val path = saveImageBitmap(it)
                Timber.d("Image path? $path")
                path?.let { imagePath ->
                    val uri = FileProvider.getUriForFile(
                        ctx,
                        "com.daniribalbert.autodogs.fileprovider",
                        File(imagePath)
                    )
                    Intent(Intent.ACTION_SEND).apply {
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, uri)
                    }.also { intent ->
                        startActivity(Intent.createChooser(intent, "Share Image"))
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_CHECK_PERMISSIONS_SHARE
            )
        }

    }

    private fun checkWritePermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun saveImageBitmap(imageBitmap: Bitmap): String? {
        val filePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context?.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "doggo_${System.currentTimeMillis()}.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + File.separator + getString(R.string.app_name)
                )
            }

            val uri = resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            Timber.d("Image Saved to: ${uri?.path}")
            uri?.path
        } else {
            val outputDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getString(R.string.app_name)
            )
            if (!outputDir.exists()) outputDir.mkdirs()

            val imageFile = File(outputDir.path, "doggo_${System.currentTimeMillis()}.jpg")
            imageFile.createNewFile()

            val fileOutputStream = FileOutputStream(imageFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            Timber.d("Image Saved to: ${imageFile.path}")
            imageFile.path
        }

        if (filePath != null) context?.let { showShortToast(it, getString(R.string.image_saved))  }
        return filePath
    }

    private fun getImageBitmap(imageUrl: String, callback: (Bitmap) -> Unit) {
        val ctx = context ?: return

        val target = object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {}

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                callback.invoke(resource)
            }

        }
        Glide.with(ctx).asBitmap().load(imageUrl).into(target)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CODE_CHECK_PERMISSIONS_SHARE) {
                shareImage(lastSelectedImage!!)
            } else {
                saveImage(lastSelectedImage!!)
            }
        }
    }

    interface FragmentInteractionListener {
        fun onShowFullScreenImage(imageUrl: String)
    }

    companion object {
        fun newInstance() = MainFragment()

        private const val REQUEST_CODE_CHECK_PERMISSIONS_SHARE = 0
        private const val REQUEST_CODE_CHECK_PERMISSIONS_SAVE = 1
    }

}
