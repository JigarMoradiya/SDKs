package com.example.iiifa_fan_android.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SocialMediaUserModel(
    var user_id: String? = "-1",
    var social_id: String? = "", //fb id
    var first_name: String? = "",
    var last_name: String? = "",
    var fullName: String? = "",
    var email: String? = "",
    var profile_pic: String? = "",
    var social_token: String? = "", //facebook token,google token,normal
    var social_type: String? = "" //facebook, google, apple
) : Parcelable