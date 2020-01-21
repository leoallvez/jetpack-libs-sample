package com.devtides.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.devtides.dogs.R
import com.devtides.dogs.databinding.ItemDogBinding
import com.devtides.dogs.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(var dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener {

    fun updateDogList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil
            .inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) = with(holder) {
        view.dog = dogsList[position]
        view.listener = this@DogsListAdapter
    }

    override fun onDogClicked(view: View) {
        val action = ListFragmentDirections.actionDetailFragmengt()
        action.dogUuid = view.dogId.text.toString().toInt()
        Navigation.findNavController(view).navigate(action)
    }
    //Automatically generated ItemDogBinding from item_dog.xml
    class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)
}

interface DogClickListener {
    fun onDogClicked(view: View)
}