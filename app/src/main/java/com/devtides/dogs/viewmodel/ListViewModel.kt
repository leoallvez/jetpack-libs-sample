
package com.devtides.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.devtides.dogs.model.DogBreed
import com.devtides.dogs.model.DogDatabase
import com.devtides.dogs.model.DogsApiService
import com.devtides.dogs.util.NotificationHelper
import com.devtides.dogs.util.SharedPreferencesHelper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit


class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper= SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L //five minutes in nano seconds
    //private var updateDelayMillis : Long = 10 * 1000
    //private var handler = Handler()
    //private var runnable : Runnable? = null
    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        checkCacheDuration()
        loading.value = true
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        }else {
            fetchFromRemote()
        }

        /**
        disposable.add(Observable.interval(
            5000, 5000,
            TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                fetchFromRemote()
            })
        **/
    }

    private fun checkCacheDuration() {
        val cachePreferences = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreferences?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L )
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun refreshFromDatabaseCache() {
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchFromRemote() {

        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread()) // make call in new background thread
                .observeOn(AndroidSchedulers.mainThread()) // process the result on main thread
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() { // Get Single
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)
                        Toast.makeText(getApplication(), "Dogs retrieved from endpoint", Toast.LENGTH_LONG).show()
                        NotificationHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogBreed>) {
        //Set LiveData
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>) {

        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
