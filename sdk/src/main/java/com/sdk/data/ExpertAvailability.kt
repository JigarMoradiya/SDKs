package com.sdk.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize class ExpertAvailability(
    var id: String?,
    @SerializedName("start_time")
    var _start_time: Long,
    @SerializedName("end_time")
    var _end_time: Long,
) : Parcelable {
    val start_time: Long
        get() = if (_start_time != null) _start_time * 1000 else 0

    val end_time: Long
        get() = if (_end_time != null) _end_time * 1000 else 0

}