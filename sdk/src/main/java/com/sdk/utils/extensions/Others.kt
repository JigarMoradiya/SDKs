package com.sdk.utils.extensions

import android.content.res.Resources
import android.os.Bundle
import com.google.gson.Gson

fun String?.isStringNotBlank() = this!=null && this.isNotBlank()
fun Collection<Any?>?.isNotNullOrEmpty() = this!=null && this.isNotEmpty()
fun Collection<Any?>?.isEmpty() = this!=null && this.isEmpty()

fun MutableMap<String?, Any?>.getBundle() : Bundle{
    val bundle = Bundle()
    for (key in this.keys){
        if (this[key] is String){
            bundle.putString(key, this[key] as String?)
        }else {
            bundle.putString(key, Gson().toJson(this[key]))
        }
    }
    return bundle
}
val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()