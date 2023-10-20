package com.sdk.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sdk.utils.Constants
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
data class Podcast(
    val admin_comment: String? = null,
    val cover_image_id: String? = null,
    @SerializedName("published_at")
    val _createdAt: Long = 0,
    @SerializedName("created_at")
    val _creat: Long = 0,
    val created_by: String? = null,
    val deleted_at: Long? = null,
    val deleted_by: String? = null,
    val description: String? = null,
    val expert_id: String? = null,
    val hidden_at: Long? = null,
    val hidden_by: String? = null,
    val id: String,
    val is_deleted: Int? = 0,
    var is_saved: Int? = 0,
    val like_count: Int? = 0,
    val podcast_media_id: String? = null,
    val published_by: String? = null,
    val save_count: Int? = 0,
    var status: String? = null,
    val title: String,
    val updated_at: Int? = 0,
    val updated_by: String? = null,
    val view_count: Int? = 0,
    val sentiments: ArrayList<Sentiment>? = null,
    @SerializedName("similar_podcast")
    val similar_podcasts: ArrayList<Podcast>? = null,
    val emotions: ArrayList<Emotion>? = null,
    @SerializedName("expert")
    var _experts: Expert,
    val podcast_media: PodcastMedia? = null,
    val cover_image: CoverImage? = null,
    var is_liked: Int? = 0,
    var is_added_to_playlist: Int? = 0,
    var selectedEmotion: Emotion? = null,
    var hidden_by_admin: Int? = 0,
    @SerializedName("total_comments")
    var comment_total: Int = 0,
    var is_followed: Int = 0,
    val comments: ArrayList<Comment>? = null,
    var fetched_at: Long = System.currentTimeMillis(),
    var localFilePath: String? = null,
    var isDownloaded: Int = 0, // 0 = pending, 1 = complete, -1 = fail
    var userId: String? = null,
    var isViewAdded: Boolean = false,
    var connectedminds_feed: Int = 0,
    var share_url: String? = null
) : Parcelable {

    val created_at: Long
        get() = _createdAt * 1000

    val created: Long
        get() = _creat * 1000


    fun expert(): Expert {
        _experts.is_followed = this.is_followed
        return _experts
    }

    val expert: Expert
        get() = expert()

}

fun Podcast.toSavedRecording() = SavedRecording(
    _created_at = "$_createdAt".toLong(),
    created_by = "${expert?.id}",
    expert_id = "${expert?.id}",
    is_deleted = "$is_deleted".toInt(),
    id = "$id",
    status = "edit",
    name = "$title",
    duration = "${podcast_media?.duration}".toInt(),
    path = "${podcast_media?.presigned_url}"
)


fun Podcast.toCommentAuthor() = CommentMediaDetails(
    id = "$id",
    name = "${expert?.first_name} ${expert?.last_name}",
    description = description,
    created_at = created_at,
    profile_picture = "${expert?.profile_url}",
    type = Constants.PODCAST
)


@Parcelize
class Podcasts : ArrayList<Podcast>(), Parcelable
