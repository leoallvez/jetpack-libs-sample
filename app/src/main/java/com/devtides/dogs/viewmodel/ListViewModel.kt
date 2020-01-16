
package com.devtides.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devtides.dogs.model.DogBreed

class ListViewModel : ViewModel() {

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError  = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val d1 = DogBreed(
            "1",
            "Corgi 1",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        val d2 = DogBreed(
            "2",
            "Corgi 2",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        val d3 = DogBreed(
            "3",
            "Corgi 3",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        val d4 = DogBreed(
            "4",
            "Corgi 4",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        val d5 = DogBreed(
            "5",
            "Corgi 5",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        val d6 = DogBreed(
            "6",
            "Corgi 6",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        val d7 = DogBreed(
            "7",
            "Corgi 7",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        val d8 = DogBreed(
            "8",
            "Corgi 8",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )
        val d9 = DogBreed(
            "9",
            "Corgi 9",
            "15 year", "breedGroup",
            "brefFor",
            "temperament",
            ""
        )

        val dogList = arrayListOf(d1, d2, d3, d4, d5, d6, d7, d8, d9)

        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }
}