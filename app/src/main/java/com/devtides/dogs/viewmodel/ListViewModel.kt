
package com.devtides.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devtides.dogs.model.DogBreed
import com.devtides.dogs.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread()) // make call in new background thread
                .observeOn(AndroidSchedulers.mainThread()) // process the result on main thread
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() { // Get Single
                    override fun onSuccess(dogList: List<DogBreed>) {
                        //Set LiveData
                        dogs.value = dogList
                        dogsLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
