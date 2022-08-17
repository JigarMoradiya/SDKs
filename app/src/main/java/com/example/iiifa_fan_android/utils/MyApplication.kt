package com.example.iiifa_fan_android.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import com.onesignal.OSNotificationAction
import com.onesignal.OneSignal
import com.stripe.android.CustomerSession
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.File
import java.io.IOException
import java.net.SocketException
import javax.inject.Inject
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal.OSNotificationOpenedHandler
import android.content.Intent
import androidx.navigation.NavDeepLinkBuilder
import com.google.gson.Gson
import com.onesignal.OSNotification

import org.json.JSONObject


@HiltAndroidApp
class MyApplication : Application() {

    init {
        instance = this
        alreadyCalledversionCheck = false
    }

    companion object {
        private var instance: MyApplication? = null
        var alreadyCalledversionCheck: Boolean? = false

        fun getInstance(): Context {
            return instance!!.applicationContext
        }

//        fun isAlreadyCalledVersionCheck(): Boolean {
//            return alreadyCalledversionCheck
//        }
//
//        fun setAlreadyCalledVersionCheck(value: Boolean) {
//            alreadyCalledversionCheck = value
//        }
    }


    override fun onCreate() {
        super.onCreate()


        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })


        RxJavaPlugins.setErrorHandler { e: Throwable ->
            if (e is UndeliverableException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (e is IOException || e is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (e is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (e is NullPointerException || e is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), e)
                return@setErrorHandler
            }
            if (e is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), e)
                return@setErrorHandler
            }
            Log.d("Undeliverable exception", e.toString())
        }


    }



}