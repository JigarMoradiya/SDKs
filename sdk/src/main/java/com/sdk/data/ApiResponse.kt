package com.sdk.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiResponse(
        var status_code : Int = 0,
        var message: String? = null,
        var emotionList : ArrayList<Emotion>? = null
) : Parcelable