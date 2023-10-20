package com.sdk.network

import android.view.View
import com.google.gson.JsonObject
import com.sdk.data.ApiResponse

interface ApiResponsesListener {
    fun success(message : String? = null, apiName : String? = null, content: JsonObject? = null, view: View? = null)
    fun error(message : String? = null, apiName : String? = null)

//    fun success(response : ApiResponse? = null, isSuccess : Boolean = false)
//    fun error(response : ApiResponse? = null)
}