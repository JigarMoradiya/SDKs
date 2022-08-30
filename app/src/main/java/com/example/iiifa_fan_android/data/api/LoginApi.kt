package com.example.iiifa_fan_android.data.api

import com.example.iiifa_fan_android.utils.MainAPIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("fanlogin")
    suspend fun login(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

}