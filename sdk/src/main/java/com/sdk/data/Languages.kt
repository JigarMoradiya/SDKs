package com.sdk.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Languages(
    var id: String,
    var name: String,
    val hex_code: String,
    var is_enabled: Int,
    var is_deleted: Int,
    var created_at: Long,
    var updated_at: Long,
    var min: Int,
    var max: Int,
    var selectedList: ArrayList<String>? = null,
) : Parcelable


fun Languages.toMultiSelect() = MultiSelect(
    id = id,
    name = name,
    hex_code = "#3858A2",
)

fun Languages.toChip() = Chip(
    id = id,
    name = name,
    color = "#3858A2",
)

