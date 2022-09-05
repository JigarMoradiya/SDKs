package com.example.iiifa_fan_android.data.api

import com.example.iiifa_fan_android.data.models.MainAPIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface CommonApi {
    @POST("checkuserexists")
    suspend fun checkUserExists(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("sendresendotp")
    suspend fun generateOtp(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("validateotp")
    suspend fun validateOtp(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("logoutuser")
    suspend fun logoutUser(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("resetpassword")
    suspend fun resetPassword(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

    @POST("changepassword")
    suspend fun changePassword(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse

}