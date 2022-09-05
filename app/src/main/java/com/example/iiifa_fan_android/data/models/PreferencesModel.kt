package com.example.iiifa_fan_android.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Preferences(
        val name: String,
        val id: Int,
        var is_selected: Boolean = false,
        val is_deleted: Int = 0
) : Parcelable

