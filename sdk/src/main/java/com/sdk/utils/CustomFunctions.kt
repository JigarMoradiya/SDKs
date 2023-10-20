package com.sdk.utils

import android.annotation.SuppressLint
import android.util.Base64
import com.sdk.BuildConfig
import org.apache.commons.text.StringEscapeUtils


object CustomFunctions {
    //Decode string
    val keyThree: String
        get() {
            return String(Base64.decode(BuildConfig.CONSTANT_KEY,Base64.DEFAULT))
        }
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
//        return Settings.Secure.getString(ConnectedMindSDK().instance?.contentResolver, Settings.Secure.ANDROID_ID)
        return "sdk_user"
    }

    @JvmStatic
    fun decodeMessage(message: String?): String {
        return try {
            StringEscapeUtils.unescapeJava(message)
        } catch (e: java.lang.Exception) {
            ""
        }
    }
}