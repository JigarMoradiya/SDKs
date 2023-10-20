package com.sdk.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sdk.utils.CustomFunctions
import com.sdk.utils.extensions.isNotNullOrEmpty
import kotlinx.parcelize.Parcelize


@Parcelize
data class Communities(
    @SerializedName("member_id")
    var member_id: String? = null,
    @SerializedName("member_status")
    var member_status: String? = null,
    @SerializedName("patient_count")
    val patient_count: Int = 0,
    @SerializedName("expert_count")
    val expert_count: Int = 0,
    @SerializedName("unread_message_count")
    var unread_message_count: Int = 0,
    @SerializedName("is_anonymous")
    val is_anonymous: Int = 0,
    @SerializedName("is_connectedminds_support")
    val is_connectedminds_support: Int = 0,
    @SerializedName("conversation_id")
    val conversation_id: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name_: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("profile_url")
    val profile_url: String? = null,
    @SerializedName("cover_url")
    val cover_url: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("sentiments")
    val sentiments: ArrayList<Sentiment> = arrayListOf(),
    @SerializedName("experts")
    val experts: ArrayList<CommunitiesExpert> = arrayListOf(),
    @SerializedName("joined_on")
    val joined_on: Long? = null,
    @SerializedName("last_message_created_at")
    val last_message_created_at: Long? = null,
) : Parcelable {
    val name : String
        get() = CustomFunctions.decodeMessage(this.name_)
    val description_ : String
        get() = CustomFunctions.decodeMessage(this.description)
    fun isCommunityJoined() : Boolean = (member_status != null && member_status == "approved")
    fun isCommunityJoinRequestedNot() : Boolean = member_status.isNullOrEmpty() || member_status.equals("request-rejected",true)
    fun isCommunityPrivate() : Boolean = type == "private"
    fun isCommunityJoinRequested() : Boolean = type == "private" && !member_status.isNullOrEmpty() && member_status.equals("pending",true)

    fun getUnreadMsgCount() : String{
        return when (unread_message_count) {
            0 -> {
                ""
            }
            1 -> {
                "1 new message"
            }
            else -> {
                "$unread_message_count new messages"
            }
        }
    }


    fun getSpecializations() : String{
        return if (experts.isNotNullOrEmpty()){
            experts.first().specializations
        }else{""}
    }
    fun facilitatedBy() : String{
        return if (experts.isNotNullOrEmpty()){
            if (experts.size > 1){
                experts.first().first_name +" "+ experts.first().last_name +" +"+(experts.size - 1)
            }else{
                experts.first().first_name +" "+ experts.first().last_name
            }
        }else{
            ""
        }

    }
    fun totalMember() : String{
        return if (patient_count == 0){
            "New"
        }else if (patient_count > 1){
            (patient_count).toString().plus(" Members")
        }else{
            (patient_count).toString().plus(" Member")
        }
    }


    fun totalMember50Plus() : String{
        return if (patient_count >= 50){
            ((patient_count / 50) * 50).toString().plus("+ Members")
        }else{
            ""
        }
    }

    fun getCommunitiesModel() : CommunitiesSubModel{
        return CommunitiesSubModel(name,type,profile_url)
    }

}

data class CommunitiesSubModel(val _name: String? = null,val type: String?= null,val profile_url: String?= null){
    val name = CustomFunctions.decodeMessage(_name)
}

@Parcelize
data class CommunitiesExpert(
    val first_name: String,
    val id: String,
    val last_name: String,
    val profile_url: String,
    val specializations: String = ""
): Parcelable{
    fun fullName() : String = "$first_name $last_name"
}

@Parcelize
data class CommunitiesJoinData(
    var id: String,
    var joined_on: Long? = 0
) : Parcelable

fun Communities.toCommunitiesJoinData() = CommunitiesJoinData(
    id = "$id",
    joined_on = joined_on
)