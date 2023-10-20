package com.sdk.utils

import android.content.Context
import android.content.SharedPreferences
import com.sdk.network.customs.EncryptRequestData

class PrefManager(context: Context) {
    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    private val PREF_NAME = "connectedmindsSDK"
    private val SHA_STRING = "sha_string"
    private val TOKEN = "token"
    private val USER_ID = "user_id"
    var PRIVATE_MODE = 0
    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref!!.edit()
    }

    fun getUserId(): String? {
        return EncryptRequestData.decrypt(pref?.getString(USER_ID, ""))
    }

    fun setUserId(userId: String?) {
        editor?.putString(USER_ID, EncryptRequestData.encrypt(userId!!))?.apply()
    }
    fun getCertSha(): String? {
        return EncryptRequestData.decrypt(pref?.getString(SHA_STRING, ""))
    }

    fun setCertSha(sha: String?) {
        editor?.putString(SHA_STRING, EncryptRequestData.encrypt(sha?:""))?.apply()
    }

    fun getToken(): String? {
        return EncryptRequestData.decrypt(pref?.getString(TOKEN, ""))
    }

    fun setToken(token: String?) {
        editor?.putString(TOKEN, EncryptRequestData.encrypt(token?:""))?.apply()
    }

}