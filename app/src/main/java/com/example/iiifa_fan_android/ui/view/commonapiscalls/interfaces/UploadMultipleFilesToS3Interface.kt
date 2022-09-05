package com.example.iiifa_fan_android.ui.view.commonapiscalls.interfaces

import com.example.iiifa_fan_android.data.models.Asset

interface UploadMultipleFilesToS3Interface {
    fun onSuccessResponse(list: ArrayList<Asset>)
    fun onFailure(error: String)
}