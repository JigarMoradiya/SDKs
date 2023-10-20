package com.sdk.data

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.sdk.utils.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val action: String? = null,
    val admin_comment: String? = null,
    val article_id: String? = null,
    var content: String? = null,
    val count: Int? = null,
    var title: String? = null,
    var cover_image: CoverImage? = null,
    val cover_image_id: String? = null,
    @SerializedName("published_at")
    val _creat: Long? = null,
    val created_by: String? = null,
    val deleted_at: Long? = null,
    val deleted_by: String? = null,
    val emotions: ArrayList<Emotion>? = null,
    @SerializedName("expert")
    var _experts: Expert,
    val expert_id: String? = null,
    val hidden_at: Long? = null,
    val hidden_by: String? = null,
    val id: String,
    val is_deleted: Int? = null,
    val like_count: Int? = null,
    val published_by: String? = null,
    val save_count: Int? = null,
    val is_feedback_provided: Int? = null,
    var sentiments: ArrayList<Sentiment>? = null,
    var chips: ArrayList<Chip>? = null,
    var status: String? = null,
    val updated_at: Int? = null,
    val updated_by: String? = null,
    val view_count: Int? = null,
    var duration: Int = 0,
    var is_liked: Int? = 0,
    var is_saved: Int? = 0,
    var hidden_by_admin: Int? = 0,
    @SerializedName("total_comments")
    var comment_total: Int = 0,
    val comments: ArrayList<Comment>? = null,
    var selectedEmotion: Emotion? = null,
    var is_followed: Int = 0,
    var userId: String? = null,
    var fetched_at: Long = System.currentTimeMillis(),
    var audio_url: String? = null,
    var connectedminds_feed: Int = 0,
    var share_url: String? = null,
    var content_preview: String? = null
) : Parcelable {
    val created_at: Long
        get() = if (_creat != null) _creat * 1000 else 0

    fun toJsonString(): String {
        return Gson().toJson(this)
    }

    fun expert(): Expert {
        _experts.is_followed = this.is_followed
        return _experts
    }

    val expert: Expert
        get() = expert()
}


fun Article.toCommentAuthor() = CommentMediaDetails(
    id = "$id",
    name = "${expert?.first_name} ${expert?.last_name}",
    description = title,
    created_at = created_at,
    profile_picture = "${expert?.profile_url}",
    type = Constants.ARTICLE
)