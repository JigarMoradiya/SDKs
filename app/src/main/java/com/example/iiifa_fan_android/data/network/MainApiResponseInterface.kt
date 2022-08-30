package com.example.iiifa_fan_android.data.network

import com.google.gson.JsonObject
import com.example.iiifa_fan_android.data.models.Error

interface MainApiResponseInterface {
    fun onSuccess(successResponse: JsonObject?, apiName: String?)
    fun onFailure(failureMessage: Error?, apiName: String?)
}