package com.jigar.me.data.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jigar.me.utils.AppConstants
import javax.inject.Inject


class AppPreferencesHelper @Inject constructor(
    context: Context,
    @PreferenceInfo private val prefFileName: String
) : PreferencesHelper {
    companion object {
        private const val PREF_KEY_FCMID = "PREF_KEY_FCMID"
        private const val PREF_KEY_DEVICE_ID = "PREF_KEY_DEVICE_ID"
        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_BASE_URL = "PREF_KEY_BASEURL"
    }

    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)


    override fun getCustomParam(paramName: String, defaultValue: String): String =
        mPrefs.getString(paramName, defaultValue).toString()

    override fun setCustomParam(paramName: String, paramValue: String) = mPrefs.edit {
        putString(paramName, paramValue)
    }

    override fun getCustomParamInt(paramName: String, defaultValue: Int): Int =
        mPrefs.getInt(paramName, defaultValue)

    override fun setCustomParamInt(paramName: String, paramValue: Int) = mPrefs.edit {
        putInt(paramName, paramValue)
    }

    override fun getCustomParamBoolean(paramName: String,defaultValue : Boolean): Boolean =
        mPrefs.getBoolean(paramName, defaultValue)

    override fun setCustomParamBoolean(paramName: String, paramValue: Boolean) = mPrefs.edit {
        putBoolean(paramName, paramValue)
    }

    override fun getBaseUrl(): String = mPrefs.getString(PREF_KEY_BASE_URL, AppConstants.TEMP_BASE_URL).toString()

    override fun setBaseUrl(baseUrl: String) = mPrefs.edit {
        putString(PREF_KEY_BASE_URL, baseUrl)
    }

    override fun getFCMID(): String = mPrefs.getString(PREF_KEY_FCMID, "").toString()

    override fun setFCMID(value: String) = mPrefs.edit {
        putString(PREF_KEY_FCMID, value)
    }

    override fun getAccessToken(): String = mPrefs.getString(PREF_KEY_BASE_URL, "").toString()

    override fun setAccessToken(accessToken: String) = mPrefs.edit {
        putString(PREF_KEY_BASE_URL, accessToken)
    }

    override fun getDeviceId(): String = mPrefs.getString(PREF_KEY_DEVICE_ID, "").toString()

    override fun setDeviceId(id: String) = mPrefs.edit {
        putString(PREF_KEY_DEVICE_ID, id)
    }

}
