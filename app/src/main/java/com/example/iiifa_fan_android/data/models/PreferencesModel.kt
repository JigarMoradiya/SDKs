package com.example.iiifa_fan_android.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Preferences(
        var name: String,
        var id: String,
        var is_selected: Boolean = false
) : Parcelable

