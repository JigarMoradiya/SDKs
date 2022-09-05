package com.example.iiifa_fan_android.data.repositories

import com.example.iiifa_fan_android.data.api.FanApi
import com.example.iiifa_fan_android.data.api.SafeApiCall
import javax.inject.Inject

class FanRepository @Inject constructor(
    private val api: FanApi
) : SafeApiCall {

    suspend fun login(params: Map<String?, Any?>?) = safeApiCall {
        api.login(params)
    }

    suspend fun socialMediaLogin(params: Map<String?, Any?>?) = safeApiCall {
        api.getSocialMediaLogin(params)
    }

    suspend fun updateFanProfile(params: Map<String?, Any?>?) = safeApiCall {
        api.updateFanProfile(params)
    }

    suspend fun addFan(params: Map<String?, Any?>?) = safeApiCall {
        api.addFan(params)
    }
}