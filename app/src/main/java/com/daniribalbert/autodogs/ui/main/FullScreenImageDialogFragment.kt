package com.daniribalbert.autodogs.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.daniribalbert.autodogs.R
import com.daniribalbert.autodogs.databinding.DialogFullScreenImageBinding
import com.daniribalbert.autodogs.utils.extensions.loadImageUrl

class FullScreenImageDialogFragment : DialogFragment() {

    private val imageUrl by lazy { arguments?.getString(ARGS_IMAGE_URL, "") ?: "" }

    private var _binding: DialogFullScreenImageBinding? = null
    private val binding: DialogFullScreenImageBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFullScreenImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.loadImageUrl(imageUrl)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogScaleAnimation
    }

    companion object {
        private const val ARGS_IMAGE_URL = "ARGS_IMAGE_URL"

        const val TAG = "FullScreenImageDialogFragment"

        fun newInstance(imageUrl: String) = FullScreenImageDialogFragment().apply {
            arguments = Bundle().apply { putString(ARGS_IMAGE_URL, imageUrl) }
        }
    }
}
