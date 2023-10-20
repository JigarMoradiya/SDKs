package com.connectedminds.sdks.utils

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("icons")
fun setIcons(view: AppCompatImageView, icons: Int?) {
    icons?.let { view.setImageResource(it) }
}

@BindingAdapter("imgUrl","placeHolder")
fun setImgUrl(view: AppCompatImageView, profile_url: String?, placeHolder : Drawable?) {
    val context = view.context
    Glide.with(view.context)
        .load(profile_url)
//        .apply(RequestOptions().placeholder(placeHolder?: ContextCompat.getDrawable(context, R.drawable.ic_default_user)))
        .into(view)
}