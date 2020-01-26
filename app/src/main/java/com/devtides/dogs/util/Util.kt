package com.devtides.dogs.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.devtides.dogs.R

val PERMISSION_SEND_SMS = 234

private fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(url: String?, options: RequestOptions? = null) {

    val progress = getProgressDrawable(context)

    val opt = options ?: RequestOptions().placeholder(progress).error(R.mipmap.ic_dog_icon)

    Glide.with(context)
        .setDefaultRequestOptions(opt)
        .load(url)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url)
}