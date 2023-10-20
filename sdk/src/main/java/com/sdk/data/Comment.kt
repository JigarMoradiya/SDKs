package com.sdk.data

import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import com.sdk.utils.CustomFunctions.decodeMessage
import kotlinx.parcelize.Parcelize

@Parcelize
class Comment(
        var id: String,
        @SerializedName("text")
        var _text: String,
        var user_id: String,
        var type: String,
        @SerializedName("created_at")
        var _created_at: Long,
        var comment_by: CommentBy
) : Parcelable {
    fun getText(): String? {
        return if (!TextUtils.isEmpty(_text)) {
            var desc: String? = _text
            desc = decodeMessage(desc!!.replace("\\n", "\n"))
            return decodeMessage(desc)
//            if (desc!!.startsWith("\"") && desc.endsWith("\"")) {
//                desc.substring(1, desc.length - 1)
//            } else {
//                desc
//            }
        } else {
            _text
        }
    }

    fun isDeletedUser() : Boolean{
        return comment_by.first_name == "undefine" && comment_by.last_name == "undefine"
    }
    fun getName() : String{
        return if (comment_by.first_name == "undefine" && comment_by.last_name == "undefine"){
            "connectedmind user"
        }else{
            comment_by.first_name
        }
    }


    val created_at: Long
        get() = if (_created_at != null) _created_at * 1000 else 0


}

