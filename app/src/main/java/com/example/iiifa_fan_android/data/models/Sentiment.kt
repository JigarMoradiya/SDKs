package com.example.iiifa_fan_android.data.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class Sentiment(
    val cover_images: ArrayList<CoverImage>? = null,
    val created_at: Long? = null,
    val created_by: String? = null,
    val deleted_at: Long? = 0,
    val deleted_by: String? = null,
    val id: String,
    val is_deleted: Int? = 0,
    val is_enabled: Int? = 0,
    val name: String? = null,
    val type: String? = null,
    val hex_code: String? = null,
    val updated_at: Int? = 0,
    val updated_by: String? = null
) : Parcelable


fun Sentiment.toMultiSelect() = MultiSelect(
    id = "$id",
    name = "$name",
    hex_code = "$hex_code",
    cover_images = cover_images
)

fun Sentiment.toChip() = Chip(
    id = "$id",
    name = "$name",
    color = "$hex_code",
    cover_images = cover_images,
    created_at = created_at
)