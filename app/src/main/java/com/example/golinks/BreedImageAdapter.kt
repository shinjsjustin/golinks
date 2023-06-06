package com.example.golinks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BreedImageAdapter(private var images: List<String>) : RecyclerView.Adapter<BreedImageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.breed_image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        Glide.with(holder.itemView)
            .load(image)
            .into(holder.imageView)
    }

    override fun getItemCount() = images.size

    fun setData(newData: List<String>) {
        images = newData
        notifyDataSetChanged()
    }
}
