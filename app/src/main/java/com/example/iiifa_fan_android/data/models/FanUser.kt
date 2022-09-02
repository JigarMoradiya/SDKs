package com.example.iiifa_fan_android.data.models

import java.util.*

data class FanUser(
    val age: Int,
    val apple_id: String? = null,
    val created_at: Int,
    val created_by: String,
    val decades: Any? = null,
    val genres: Any? = null,
    val email: String,
    val first_name: String,
    val gender: String? = null,
    val id: String,
    val is_deleted: Int,
    val is_email_verified: Int,
    val is_phone_number_verified: Int,
    val is_preference_email: Int,
    val is_preference_push_notification: Int,
    val last_login_time: Int,
    val last_name: String,
    val mfa_required: Int,
    val password_count: Int,
    val phone_number: String? = null,
    val profile_url: String? = null,
    val registration_type: String,
    val secret: String,
    val status: String,
    val stripe_customer_id: String? = null
) {
    fun getGenders(): String {
        return gender?.substring(0, 1)?.uppercase(Locale.ROOT) + gender?.substring(1)
            ?.lowercase(Locale.ROOT)
    }
}