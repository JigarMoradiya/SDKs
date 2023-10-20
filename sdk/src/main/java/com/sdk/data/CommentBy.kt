package com.sdk.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CommentBy(
        val email: String,
        val first_name: String,
        val id: String,
        val last_name: String,
        val user_type: String,
        var profile_url: String,
) : Parcelable