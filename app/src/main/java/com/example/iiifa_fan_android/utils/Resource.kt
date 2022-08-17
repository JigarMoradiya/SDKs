package com.example.iiifa_fan_android.utils

import com.onesignal.OneSignal
import okhttp3.ResponseBody

sealed class Resource<out T> {

    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
            val isNetworkError: Boolean,
            val errorCode: Int?,
            val errorBody: String?,
            val errorType: String? = null
    ) : Resource<Nothing>()

    object Loading : Resource<Nothing>()
}