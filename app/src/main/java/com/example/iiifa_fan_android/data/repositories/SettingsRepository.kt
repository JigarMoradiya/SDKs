package com.example.iiifa_fan_android.data.repositories

import com.example.iiifa_fan_android.data.api.SafeApiCall
import com.example.iiifa_fan_android.data.api.SettingsApi
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val api: SettingsApi
) : SafeApiCall {

    suspend fun manageEntities(params: Map<String?, Any?>?) = safeApiCall {
        api.manageEntities(params)
    }
}