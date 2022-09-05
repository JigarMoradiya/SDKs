package com.example.iiifa_fan_android.data.repositories

import com.example.iiifa_fan_android.data.api.CommonApi
import com.example.iiifa_fan_android.data.api.SafeApiCall
import javax.inject.Inject

class CommonRepository @Inject constructor(
    private val api: CommonApi
) : SafeApiCall {
    suspend fun checkUserExists(params: Map<String?, Any?>?) = safeApiCall {
        api.checkUserExists(params)
    }
    suspend fun sendResendOTP(params: Map<String?, Any?>?) = safeApiCall {
        api.generateOtp(params)
    }
    suspend fun validateOtp(params: Map<String?, Any?>?) = safeApiCall {
        api.validateOtp(params)
    }
    suspend fun logoutUser(params: Map<String?, Any?>?) = safeApiCall {
        api.logoutUser(params)
    }
    suspend fun resetPassword(params: Map<String?, Any?>?) = safeApiCall {
        api.resetPassword(params)
    }
    suspend fun changePassword(params: Map<String?, Any?>?) = safeApiCall {
        api.changePassword(params)
    }
    suspend fun getSignedPutObjectUrl(params: Map<String?, Any?>?) = safeApiCall {
        api.getSignedPutObjectUrl(params)
    }

}