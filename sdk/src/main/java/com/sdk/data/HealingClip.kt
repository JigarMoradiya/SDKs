package com.sdk.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sdk.utils.Constants
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
data class HealingClip(
    val id: String? = null,
    val caption: String? = null,
    val path: String? = null,
    val thumbnail_url: String? = null,
    val expert_id: String? = null,
    var status: String? = null,
    var connectedminds_feed: Int = 0,
    var action: String? = null,
    val published_by: String? = null,
    @SerializedName("published_at")
    val _createdAt: Long = 0,
    @SerializedName("created_at")
    val _creat: Long = 0,
    val admin_comment: String? = null,
    var is_hidden: Int = 0,
    val hidden_at: Long? = null,
    val hidden_by: String? = null,
    var like_count: Int = 0,
    val view_count: Int = 0,
    val save_count: Int = 0,
    val hidden_upon_suspension: Int? = 0,
    val created_by: String? = null,
    val updated_at: Int? = 0,
    val updated_by: String? = null,
    val is_deleted: Int? = 0,
    val deleted_at: Long? = null,
    val deleted_by: String? = null,
    var share_url: String? = null,
    @SerializedName("object")
    val objects: String? = null,
    var hidden_by_admin: Int? = 0,
    @SerializedName("total_comments")
    var comment_total: Int = 0,
    var is_saved: Int? = 0,
    var is_liked: Int? = 0,
    var is_followed: Int = 0,
    @SerializedName("expert")
    var _experts: Expert? = null,
    val sentiments: ArrayList<Sentiment>? = null,
    val comments: ArrayList<Comment>? = null,
    var presigned_url: String? = null,
    var isAds: Boolean = false,
    val emotions: ArrayList<Emotion>? = null,
    @SerializedName("similar_clip")
    val similar_clip: ArrayList<HealingClip>? = null,

    ) : Parcelable {
    val created_at: Long
        get() = _createdAt * 1000

    val created: Long
        get() = _creat * 1000


    fun expert(): Expert? {
        _experts?.is_followed = this.is_followed
        return _experts
    }
}

fun HealingClip.toCommentAuthor() = CommentMediaDetails(
    id = "$id",
    name = "",
    description = caption,
    created_at = created_at,
    profile_picture = "",
    type = Constants.HEALING_CLIP
)

fun HealingClip.toViewData() = HealingClipViewRequest(
    clip_id = "$id",
    emotion_id = caption
)

@Parcelize
data class HealingClipViewRequest(
    var clip_id: String? = null,
    val emotion_id: String? = null
) : Parcelable