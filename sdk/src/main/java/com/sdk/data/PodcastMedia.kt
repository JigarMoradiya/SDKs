package com.sdk.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PodcastMedia(
    val duration: Int = 0,
    val id: String,
    val name: String,
    val path: String,
    val presigned_url: String,
    val status: String,
    val duration_preview: Int = 0,
    val path_preview: String? = null,
    val presigned_url_preview: String? = null,
    var canAccess: Boolean = true
) : Parcelable