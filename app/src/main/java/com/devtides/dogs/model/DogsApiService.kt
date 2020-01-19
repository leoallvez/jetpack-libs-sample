package com.devtides.dogs.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DogsApiService {

    //Create a DogsApi object with retrofit.
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // JSON to our list of model class
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // list of model class to Single
        .build()
        .create(DogsApi::class.java)

    //Single is an observable.
    fun getDogs(): Single<List<DogBreed>> = api.getDogs()

    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com"
    }
}