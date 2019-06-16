package com.synchroniverse.bowwow.breeddetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.synchroniverse.bowwow.R

class ImageAdapter(var imageUrlList: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateList(newUrlList: List<String>) {
        imageUrlList = newUrlList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_view_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageViewHolder).bind(imageUrlList[position])
    }

    class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.imageView)

        fun bind(imageUrl: String) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imageView)
        }
    }
}