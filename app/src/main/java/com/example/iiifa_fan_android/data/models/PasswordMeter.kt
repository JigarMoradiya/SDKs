package com.example.iiifa_fan_android.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordMeter(
        var id: String,
        var name: String,
        var status: PasswordMeterStatus
) : Parcelable


enum class PasswordMeterStatus {
    SELECTED,
    ERROR,
    NOTSELECTED,
}