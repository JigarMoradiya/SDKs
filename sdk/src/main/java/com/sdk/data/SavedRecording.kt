package com.sdk.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedRecording(
    @SerializedName("created_at")
    var _created_at: Long,
    var created_by: String = "",
    var duration: Int,
    var expert_id: String = "",
    var id: String = "",
    var is_deleted: Int = 0,
    var name: String?,
    var path: String?,
    var status: String = ""
) : Parcelable {
    override fun toString(): String {
        return "SavedRecording(created_at=$created_at, created_by='$created_by', duration=$duration, expert_id='$expert_id', id='$id', is_deleted=$is_deleted, name='$name', path='$path', status='$status')"
    }

    val created_at: Long
        get() = _created_at * 1000

}