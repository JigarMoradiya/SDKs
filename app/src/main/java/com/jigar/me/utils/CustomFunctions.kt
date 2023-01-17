package com.jigar.me.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.jigar.me.MyApplication
import com.jigar.me.data.pref.AppPreferencesHelper
import java.util.regex.Matcher
import java.util.regex.Pattern


object CustomFunctions {
    //Logout user and clear local storage
    @JvmStatic
    fun handleForbiddenResponse(showingAfterLogout: Boolean? = true) {
        val context: Context = MyApplication.getInstance()
        val prefManager = AppPreferencesHelper(context, Constants.PREF_NAME)
    }

    @JvmStatic
    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }


    @JvmStatic
    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    @JvmStatic
    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }


    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getDeviceId(): String {
        return Settings.Secure.getString(
            MyApplication.getInstance().contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }


    @JvmStatic
    private fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }


    @JvmStatic
    fun containsLowerCase(value: String): Boolean {
        var bool = false
        val pattern = Pattern.compile("(?=.*[a-z])")
        val matcher: Matcher = pattern.matcher(value)
        while (matcher.find()) {
            bool = true
        }
        return bool
    }

    @JvmStatic
    fun containsUpperCase(value: String): Boolean {
        var bool = false
        val pattern = Pattern.compile("(?=.*[A-Z])")
        val matcher: Matcher = pattern.matcher(value)
        while (matcher.find()) {
            bool = true
        }
        return bool
    }

    @JvmStatic
    fun containsNumericCharacter(value: String): Boolean {
        var bool = false
        val pattern = Pattern.compile("[0-9]")
        val matcher: Matcher = pattern.matcher(value)
        while (matcher.find()) {
            bool = true
        }
        return bool
    }

    @JvmStatic
    fun containsSpecialCharacter(value: String): Boolean {
        var bool = false
        val pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(value)
        while (matcher.find()) {
            bool = true
        }
        return bool
    }

}
