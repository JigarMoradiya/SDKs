package com.sdk.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Specialization(
    val created_at: Long,
    val created_by: String,
    val deleted_at: Long,
    val deleted_by: String,
    val id: String,
    val is_deleted: Int,
    val is_enabled: Int,
    val hex_code: String,
    val name: String,
    val updated_at: Long,
    val updated_by: String,
) : Parcelable

fun Specialization.toMultiSelect() = MultiSelect(
    id = id,
    name = name,
    hex_code = hex_code,
)

fun Specialization.toChip() = Chip(
    id = id,
    name = name,
    color = hex_code,
)