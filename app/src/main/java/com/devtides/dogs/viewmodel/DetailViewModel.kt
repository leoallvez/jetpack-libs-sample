package com.devtides.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devtides.dogs.model.DogBreed

class DetailViewModel : ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch() {
        val d1 = DogBreed(
            "1",
            "Corgi 1",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        dogLiveData. value = d1
    }
}