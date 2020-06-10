package com.daniribalbert.autodogs.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.daniribalbert.autodogs.R
import com.daniribalbert.autodogs.utils.extensions.loadImageUrl
import kotlinx.android.synthetic.main.item_dog.view.*

class MainAdapter(val onClick: (String) -> Unit, val onLongClick: (String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val imageList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false)
        return UserViewHolder(view)
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

    private inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image: ImageView = view.image

        init {
            image.setOnClickListener { onClick.invoke(imageList[adapterPosition]) }
            image.setOnLongClickListener {
                onLongClick.invoke(imageList[adapterPosition])
                true
            }
        }

        fun bind(url: String) {
            image.loadImageUrl(url)
        }
    }
}