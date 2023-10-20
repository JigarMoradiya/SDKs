package com.sdk.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MultiSelect(
    val id: String,
    val name: String,
    var cover_images: ArrayList<CoverImage>? = null,
    val hex_code: String? = null,
    val _start_time: Long? = null,
    val _end_time: Long? = null,
    val position : Int = -1
) : Parcelable {
    val start_time: Long
        get() = if (_start_time != null) _start_time * 1000 else 0

    val end_time: Long
        get() = if (_end_time != null) _end_time * 1000 else 0
}

fun MultiSelect.toChip() = Chip(
    id = "$id",
    name = name,
    color = "$hex_code",
    cover_images = cover_images
)



