package com.sdk.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoverImage(
    val id: String,
    var image_url: String,
    var text_hex_code: String? = null
) : Parcelable