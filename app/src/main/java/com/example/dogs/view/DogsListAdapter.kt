package com.example.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.R
import com.example.dogs.model.Dog
import com.example.dogs.util.getProgressDrawable
import com.example.dogs.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val dogsList: ArrayList<Dog>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {
    fun updateDogList(newDogsList: List<Dog>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    inner class DogViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false) as View
        return DogViewHolder(view)

    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.itemView.name.text = dogsList[position].dogBreed
        holder.itemView.lifespan.text = dogsList[position].lifeSpan
        holder.itemView.imageView.loadImage(
            dogsList[position].imgUrl,
            getProgressDrawable(holder.itemView.imageView.context)
        )
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionDetailFragment())
        }
    }

    override fun getItemCount(): Int = dogsList.count()
}