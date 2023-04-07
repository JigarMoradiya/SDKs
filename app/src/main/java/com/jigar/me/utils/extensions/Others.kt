package com.jigar.me.utils.extensions

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.jigar.me.BuildConfig

val TAG = "jigarLog"
fun String?.isStringNotBlank() = this!=null && this.isNotBlank()
fun Collection<Any?>?.isNotNullOrEmpty() = this!=null && this.isNotEmpty()
fun Collection<Any?>?.isEmpty() = this!=null && this.isEmpty()

fun Any.log(message: String) {
    if (BuildConfig.DEBUG){
        Log.e(TAG, message)
    }
}