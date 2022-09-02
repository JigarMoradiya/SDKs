package com.example.iiifa_fan_android.data.api

import com.example.iiifa_fan_android.data.models.MainAPIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FanApi {

    @POST("fanlogin")
    suspend fun login(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("updatefanprofile")
    suspend fun updateFanProfile(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("resetpassword")
    suspend fun resetPassword(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("changepassword")
    suspend fun changePassword(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("addfan")
    suspend fun addFan(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

}