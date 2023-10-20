package com.connectedminds.sdks.utils

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