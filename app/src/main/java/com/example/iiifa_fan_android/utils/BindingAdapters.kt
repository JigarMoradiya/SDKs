package com.example.iiifa_fan_android.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("icons")
fun setIcons(view: AppCompatImageView, icons: Int?) {
    icons?.let { view.setImageResource(it) }
}