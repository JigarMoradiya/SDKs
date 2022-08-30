package com.example.iiifa_fan_android.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.example.iiifa_fan_android.utils.EncryptRequestData
import javax.inject.Inject


class AppPreferencesHelper @Inject constructor(
    context: Context,
    @PreferenceInfo private val prefFileName: String
) :
    PreferencesHelper {
    companion object {
        private const val PREF_USER_DATA = "pref_user_data"
        private const val PREF_USER_ID = "pref_user_id"
        private const val PREF_USER_EMAIL = "pref_user_email"
        private const val PREF_TOKEN = "pref_token"
        private const val NOTIFICATION_TOKEN = "notification_token"
        private const val PROFILE_URL = "profile_url"
        private const val SHA_STRING = "sha_string"
        private const val IS_USER_LOGGED_IN_ONCE = "is_user_logged_in_once"
    }

    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override fun getUserData(): String? {
        return EncryptRequestData.decrypt(mPrefs.getString(PREF_USER_DATA, null))
    }

    override fun setUserData(value: String?) {
        mPrefs.edit().putString(PREF_USER_DATA, EncryptRequestData.encrypt(value)).apply()
    }

    override fun getUserId(): String? {
        return EncryptRequestData.decrypt(mPrefs.getString(PREF_USER_ID, null))
    }

    override fun setUserId(value: String?) {
        mPrefs.edit().putString(PREF_USER_ID, EncryptRequestData.encrypt(value)).apply()
    }

    override fun getUserEmail(): String? {
        return EncryptRequestData.decrypt(mPrefs.getString(PREF_USER_EMAIL, null))
    }

    override fun setUserEmail(value: String?) {
        mPrefs.edit().putString(PREF_USER_EMAIL, EncryptRequestData.encrypt(value)).apply()
    }

    override fun getProfileUrl(): String? {
        return EncryptRequestData.decrypt(mPrefs.getString(PROFILE_URL, null))
    }

    override fun setProfileUrl(value: String?) {
        mPrefs.edit().putString(PROFILE_URL, EncryptRequestData.encrypt(value)).apply()
    }

    override fun getCertSha(): String? {
        return EncryptRequestData.decrypt(mPrefs.getString(SHA_STRING, null))
    }

    override fun setCertSha(value: String?) {
        mPrefs.edit().putString(SHA_STRING, EncryptRequestData.encrypt(value)).apply()
    }

    override fun getIsUserLoggedInOnce(): String? {
        return EncryptRequestData.decrypt(mPrefs.getString(IS_USER_LOGGED_IN_ONCE, null))
    }

    override fun setIsUserLoggedInOnce(value: String?) {
        mPrefs.edit().putString(IS_USER_LOGGED_IN_ONCE, EncryptRequestData.encrypt(value)).apply()
    }

    override fun getToken(): String? {
        return EncryptRequestData.decrypt(mPrefs.getString(PREF_TOKEN, null))
    }

    override fun setToken(value: String?) {
        mPrefs.edit().putString(PREF_TOKEN, EncryptRequestData.encrypt(value)).apply()
    }

    override fun getNotificationToken(): String? {
        return EncryptRequestData.decrypt(mPrefs.getString(NOTIFICATION_TOKEN, null))
    }

    override fun setNotificationToken(value: String?) {
        mPrefs.edit().putString(NOTIFICATION_TOKEN, EncryptRequestData.encrypt(value)).apply()
    }

}
