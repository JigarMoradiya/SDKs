package com.example.iiifa_fan_android.ui.view.commonapiscalls.interfaces

interface UploadToS3Interface {
    fun onSuccess(response: String, uploadedFileName: String, position: Int, type : String)
    fun onFailure(error: String)
}