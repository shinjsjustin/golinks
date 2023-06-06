package com.example.golinks
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DogBreedAdapter(private var dogBreeds: List<String>, private val clickListener: (String)-> Unit) : RecyclerView.Adapter<DogBreedAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val breedName: TextView = view.findViewById(R.id.breedName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dog_breed_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val breed = dogBreeds[position]
        holder.breedName.text = breed
        holder.itemView.setOnClickListener({clickListener(breed)})
    }

    override fun getItemCount() = dogBreeds.size

    fun setData(newData: List<String>) {
        dogBreeds = newData
        notifyDataSetChanged()
    }
}