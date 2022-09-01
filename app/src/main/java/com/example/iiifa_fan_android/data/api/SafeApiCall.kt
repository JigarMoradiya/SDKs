package com.example.iiifa_fan_android.data.api


import android.util.Log
import com.example.iiifa_fan_android.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Log.e("error_safe_api_call", throwable.toString()+"....."+throwable.response())
                        Resource.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody().toString()
                        )
                    }
                    else -> {
                        Log.e("error_safe_api_call", throwable.toString())
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }
}