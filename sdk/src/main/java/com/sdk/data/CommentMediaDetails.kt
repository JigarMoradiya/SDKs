package com.sdk.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentMediaDetails(
    var description: String? = null,
    val created_at: Long? = null,
    val profile_picture: String? = null,
    val name: String,
    val type: String,
    val id: String,
    val is_comment_enabled: Boolean? = true
) : Parcelable