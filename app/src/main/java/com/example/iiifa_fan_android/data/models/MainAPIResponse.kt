package com.example.iiifa_fan_android.utils

import com.example.mentalhealthpatient.data.models.Error
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class MainAPIResponse {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var isStatus = false

    @SerializedName("code")
    var code = 0

    @SerializedName("content")
    var content: JsonObject? = null

    @SerializedName("error")
    var error: Error? = null


    constructor(message: String?, status: Boolean, code: Int, content: JsonObject?, error: Error?) {
        this.message = message
        isStatus = status
        this.code = code
        this.content = content
        this.error = error
    }

    override fun toString(): String {
        return "MainAPIResponse{" +
                "message='" + message + '\'' +
                ", status=" + isStatus +
                ", code=" + code +
                ", content=" + content +
                ", error=" + error +
                '}'
    }
}