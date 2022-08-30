package com.example.iiifa_fan_android.data.api

import com.example.iiifa_fan_android.utils.MainAPIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface CommonApi {
    @POST("sendresendotp")
    suspend fun generateOtp(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("validateotp")
    suspend fun validateOtp(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("logoutuser")
    suspend fun logoutUser(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse
}