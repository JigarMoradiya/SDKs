package com.sdk.data

data class ErrorSession(
    val channel: String,
    val created_at: Int,
    val created_by: String,
    val deleted_at: Any,
    val deleted_by: Any,
    val device_identifier: String,
    val device_name: String,
    val patient_id: String,
    val id: String,
    val is_deleted: Int,
    val is_logged_out: Int,
    val location: String,
    val login_time: Int,
    val logout_time: Any,
    val updated_at: Int,
    val updated_by: String
)