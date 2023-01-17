package com.jigar.me.data.pref

interface PreferencesHelper {
    // constants
    fun getDeviceId(): String
    fun setDeviceId(id: String)

    fun getAccessToken(): String
    fun setAccessToken(accessToken: String)

    fun getCustomParam(paramName: String,defaultValue : String): String
    fun setCustomParam(paramName: String,paramValue: String)

    fun getCustomParamInt(paramName: String,defaultValue : Int): Int
    fun setCustomParamInt(paramName: String,paramValue: Int)

    fun getCustomParamBoolean(paramName: String,defaultValue : Boolean): Boolean
    fun setCustomParamBoolean(paramName: String,paramValue: Boolean)

    fun getBaseUrl(): String
    fun setBaseUrl(baseUrl: String)

    fun getFCMID(): String
    fun setFCMID(value: String)
}
