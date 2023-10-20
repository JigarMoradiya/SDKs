package com.sdk.data

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.sdk.utils.extensions.isNotNullOrEmpty
import kotlinx.parcelize.Parcelize

@Parcelize
class Expert(
    var id: String,
    var first_name: String,
    var last_name: String,
    var email: String,
    var gender: String? = null,
    var followers_count: Int?,
    var is_account_verified: Int?,
    var non_uk_account_id: String?,
    var consultations: Int?,
    @SerializedName("specializations")
    val specialization: ArrayList<Specialization>?,
    @SerializedName("specialization_list")
    val expert_specializations: String? = null,
    val user_type: String? = null,
    val profile_url: String? = null,
    val about_me: String? = null,
    val prices: ArrayList<Currency>?,
    val article: ArrayList<Article>? = null,
    val post: Post? = null,
    val podcasts: ArrayList<Podcast>? = null,
    val rating: Double,
    val reviews: Int?,
    val available_today: ArrayList<ExpertAvailability>? = null,
    val is_expert_certified: Int,
    val is_connectedminds_expert: Int,
    var conversation_id: String?,
    var is_followed: Int,
    var userId: String? = null,
    var fetched_at: Long = System.currentTimeMillis(),
    var created_at: Long = 0,
    @SerializedName("community")
    val communities: ArrayList<Communities>? = null,
    @SerializedName("languages")
    val language: ArrayList<Languages>? = null,
    @SerializedName("language")
    val language_str: String? = null,
    var published_podcasts_count: Long = 0,
    var published_articles_count: Long = 0,
    var published_courses_count: Long = 0,
    var published_content_count: Long = 0,
    var published_community_count: Long = 0
) : Parcelable {

    fun getLanguageStr() : String?{
        return if (!language_str.isNullOrEmpty()){
            language_str
        }else if (language.isNotNullOrEmpty()){
            val list : List<String>? = language?.map { it.name }
            list?.joinToString()
        }else{
            ""
        }
    }

    fun fullName() = first_name+" "+last_name
    val created: Long
        get() = if (created_at != null) created_at * 1000 else 0
    fun getDesignation(): String {

        var data = ""

        if (!expert_specializations.isNullOrBlank()) {
            data = expert_specializations
        } else if (specialization != null) {
            for (element in specialization) {
                data = if (data.isBlank())
                    element.name
                else
                    data + ", " + element.name
            }
        }

        return data
    }


    fun toJsonString(): String {
        return Gson().toJson(this)
    }

//    fun getChatUser(): User {
//        return User(
//            id,
//            first_name,
//            last_name,
//            email,
//            profile_url.toString(),
//            false,
//            false
//        )
//    }

    fun getFullName(): String {
        return first_name + " " + last_name
    }

}