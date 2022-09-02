package com.example.iiifa_fan_android.data.api

import com.example.iiifa_fan_android.data.models.MainAPIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SettingsApi {
    @POST("manageentities")
    suspend fun manageEntities(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse
}