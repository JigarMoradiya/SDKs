package com.example.iiifa_fan_android.data.repositories

import com.example.iiifa_fan_android.data.api.LoginApi
import com.example.iiifa_fan_android.data.api.SafeApiCall
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val api: LoginApi
) : SafeApiCall {

    suspend fun login(params: Map<String?, Any?>?) = safeApiCall {
        api.login(params)
    }
}