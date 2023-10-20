package com.sdk.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import kotlinx.parcelize.Parcelize

@Parcelize
data class Asset(
        val path: String? = null,
        val post_media_id: String? = null,
        var presigned_url: String? = null,
        var thumbnail_presigned_url: String? = null,
        var type: String,
        var action: String? = null,
        val id: String? = null,
        var thumbnail_path: String? = null
) : Parcelable {

    fun getJsonObjectForAddEditPost(): JsonObject {
        val json = JsonObject()

        id?.let {
            json.addProperty("id", id)
        }

        json.addProperty("type", type)
        json.addProperty("path", presigned_url)

        if (!thumbnail_path.isNullOrBlank())
            json.addProperty("thumbnail_path", thumbnail_path)

        if (!action.isNullOrBlank())
            json.addProperty("action", action)

        return json
    }

    fun getJsonObjectForEditRemovedAssetPost(): JsonObject {
        val json = JsonObject()

        id?.let {
            json.addProperty("post_media_id", id)
        }

        json.addProperty("action", action)
        return json
    }
}


fun Asset.toEditPost() = Asset(
    post_media_id = "$id ",
    type = "$type"
)
