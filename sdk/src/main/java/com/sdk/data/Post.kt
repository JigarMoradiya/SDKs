package com.sdk.data

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.sdk.utils.Constants
import com.sdk.utils.CustomFunctions
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    @SerializedName("post_media") var assets: ArrayList<Asset>?,
    @SerializedName("description") var _description: String? = null,
    var sentiment_ids: List<String>? = null,
    var id: String? = null,
    var type: String,
    var user_id: String,
    val user_type: String? = null,
    val admin_comment: String? = null,
    val count: Int? = null,
    val expert_id: String? = null,
    var status: String? = null,
    val action: String? = null,
    val like_count: Int? = null,
    val view_count: Int? = null,
    val save_count: Int? = null,
    @SerializedName("published_at") val _created_at: Long? = null,
    val created_by: String? = null,
    val deleted_at: Long? = null,
    val deleted_by: String? = null,
    val published_by: String? = null,
    val is_deleted: Int? = null,
    val hidden_at: Long? = null,
    val hidden_by: String? = null,
    var sentiments: ArrayList<Sentiment>? = null,
    val emotions: ArrayList<Emotion>? = null,
    @SerializedName("comments")
    val _comments: ArrayList<Comment>? = null,
    val expert: Expert? = null,
    var selectedEmotion: Emotion? = null,
    var is_liked: Int? = 0,
    var is_saved: Int? = 0,
    @SerializedName("total_comments")
    var comment_total: Int = 0,
    var hidden_by_admin: Int? = 0,

    ) : Parcelable {

    val created_at: Long
        get() = if (_created_at != null) _created_at * 1000 else 0

    val description: String
        get() = if (_description.isNullOrBlank()) "" else CustomFunctions.decodeMessage(_description)
            .toString()

    val comments: List<Comment>?
        get() = if (_comments != null && _comments.size > 2) _comments.take(2)
            .toList() else _comments

    fun toJsonString(): String {
        return Gson().toJson(this)
    }

}

fun Post.toCommentAuthor() = CommentMediaDetails(
    id = "$id",
    name = "${expert?.first_name} ${expert?.last_name}",
    description = description,
    created_at = created_at,
    profile_picture = "${expert?.profile_url}",
    type = Constants.POST
)