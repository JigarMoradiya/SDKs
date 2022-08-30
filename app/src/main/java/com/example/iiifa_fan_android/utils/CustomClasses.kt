package com.example.iiifa_fan_android.utils

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.example.iiifa_fan_android.data.pref.AppPreferencesHelper
import com.example.iiifa_fan_android.ui.view.login.activities.SplashActivity

import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.google.gson.JsonObject

object CustomClasses {
    //Convert into JSONObject
    fun convertObjectToJsonObject(content: Any?): JsonObject {
        val gson = Gson()
        val value = gson.toJson(content);
        return gson.fromJson(value, JsonObject::class.java)
    }

    //Logout user and clear local storage
    @JvmStatic
    fun handleForbiddenResponse() {
        val context: Context = MyApplication.getInstance()
        val prefManager = AppPreferencesHelper(context, Constants.PREF_NAME)
        if (!TextUtils.isEmpty(prefManager.getUserData())) {
            prefManager.setUserEmail(null)
            prefManager.setUserData(null)
            prefManager.setUserId(null)
            prefManager.setToken(null)
            prefManager.setNotificationToken(null)
            val intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("forcefully_logout", true)
            context.startActivity(intent)
            LoginManager.getInstance().logOut()
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // .requestIdToken(getString(R.string.server_client_id))
                    //  .requestIdToken(BuildConfig.google_sign_in_key)
                    .requestEmail()
                    .build()
            GoogleSignIn.getClient(context, gso).signOut()
            //            });
        }
    }

}