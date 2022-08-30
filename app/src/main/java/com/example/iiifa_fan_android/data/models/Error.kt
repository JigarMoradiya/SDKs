package com.example.iiifa_fan_android.data.models

import com.example.iiifa_fan_android.utils.Constants


class Error(
    var message: String? = Constants.API_ERROR, var userMessage: String? = null,
    var errorType: String? = Constants.API_ERROR_TYPE, var error: Boolean = false,
    var sessions: ArrayList<ErrorSession>? = null
) {

    override fun toString(): String {
        return "Error(message=$message, userMessage=$userMessage, errorType=$errorType, error=$error)"
    }
}