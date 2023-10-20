package com.sdk.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chip(
        var name: String,
        var id: String,
        var color: String,
        var cover_images: ArrayList<CoverImage>? = null,
        val created_at: Long? = null,
) : Parcelable

fun Chip.toSentiment() = Sentiment(
        id = "$id",
        name = "$name",
        cover_images = cover_images,
        created_at = created_at
)
