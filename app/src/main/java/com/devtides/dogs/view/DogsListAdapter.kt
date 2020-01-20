package com.devtides.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.devtides.dogs.R
import com.devtides.dogs.model.DogBreed
import com.devtides.dogs.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(var dogsList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(){

    fun updateDogList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) = with(holder) {
        val dog = dogsList[position]
        view.name.text = dog.dogBreed
        view.lifespan.text = dog.lifeSpan
        view.setOnClickListener{
            val action = ListFragmentDirections.actionDetailFragmengt()
            action.dogUuid = dog.uuid
            Navigation.findNavController(it).navigate(action)
        }
        view.image_view.loadImage(dog.imageUrl)
    }

    class DogViewHolder(var view: View) : RecyclerView.ViewHolder(view)
}