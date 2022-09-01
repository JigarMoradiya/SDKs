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

    suspend fun changePassword(params: Map<String?, Any?>?) = safeApiCall {
        api.changePassword(params)
    }

    suspend fun addFan(params: Map<String?, Any?>?) = safeApiCall {
        api.addFan(params)
    }
}