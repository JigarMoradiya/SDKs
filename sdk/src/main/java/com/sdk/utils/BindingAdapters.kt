package com.sdk.utils

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sdk.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("imgUrl","placeHolder")
fun setImgUrl(view: AppCompatImageView, profile_url: String?, placeHolder : Drawable?) {
    val context = view.context
    Glide.with(view.context)
        .load(profile_url)
        .apply(RequestOptions().placeholder(placeHolder?: ContextCompat.getDrawable(context, R.drawable.ic_image_placeholder)))
        .into(view)
}

@BindingAdapter("circleImgUrl","placeHolder")
fun setCircleImgUrl(view: CircleImageView, circleImgUrl: String?, placeHolder : Drawable?) {
    val context = view.context
    Glide.with(view.context)
        .load(circleImgUrl)
        .apply(RequestOptions().placeholder(placeHolder?: ContextCompat.getDrawable(context, R.drawable.ic_default_user)))
        .into(view)
}