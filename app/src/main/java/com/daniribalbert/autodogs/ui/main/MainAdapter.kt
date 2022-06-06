package com.daniribalbert.autodogs.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.daniribalbert.autodogs.databinding.ItemDogBinding
import com.daniribalbert.autodogs.utils.extensions.loadImageUrl

class MainAdapter(val onClick: (String) -> Unit, val onLongClick: (String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val imageList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserViewHolder(ItemDogBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> holder.bind(imageList[position])
        }
    }

    fun addImage(imageUrl: String): Boolean {
        if (!imageList.contains(imageUrl)) {
            imageList.add(0, imageUrl)
            notifyItemInserted(0)
            return true
        }
        return false
    }

    private inner class UserViewHolder(binding: ItemDogBinding) : RecyclerView.ViewHolder(binding.root) {

        val image: ImageView = binding.image

        init {
            image.setOnClickListener { onClick.invoke(imageList[bindingAdapterPosition]) }
            image.setOnLongClickListener {
                onLongClick.invoke(imageList[bindingAdapterPosition])
                true
            }
        }

        fun bind(url: String) {
            image.loadImageUrl(url)
        }
    }
}
