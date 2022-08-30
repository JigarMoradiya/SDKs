package com.example.iiifa_fan_android.data.pref

interface PreferencesHelper {
    fun getUserData(): String?
    fun setUserData(value: String?)

    fun getUserId(): String?
    fun setUserId(value: String?)

    fun getUserEmail(): String?
    fun setUserEmail(value: String?)

    fun getProfileUrl(): String?
    fun setProfileUrl(value: String?)

    fun getIsUserLoggedInOnce(): String?
    fun setIsUserLoggedInOnce(value: String?)

    fun getCertSha(): String?
    fun setCertSha(value: String?)

    fun getToken(): String?
    fun setToken(value: String?)

    fun getNotificationToken(): String?
    fun setNotificationToken(value: String?)
}
