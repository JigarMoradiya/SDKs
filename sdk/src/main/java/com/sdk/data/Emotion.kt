package com.sdk.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Emotion(
        val id: String,
        val name: String,
        val posts: ArrayList<Post>? = null,
        val articles: ArrayList<Article>? = null,
        val podcasts: ArrayList<Podcast>? = null,
        val clips: ArrayList<HealingClip>? = null,
        val hex_code: String? = null,
) : Parcelable

fun Emotion.toMultiSelect() = MultiSelect(
        id = "$id",
        name = "$name",
        hex_code = "$hex_code",
)


fun Emotion.toChip() = Chip(
        id = "$id",
        name = "$name",
        color = "$hex_code",
)


fun Emotion.toShortData() = Emotion(
        id = "$id",
        name = "$name"
)