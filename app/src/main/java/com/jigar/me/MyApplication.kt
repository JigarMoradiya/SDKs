package com.jigar.me

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketException


@HiltAndroidApp
class MyApplication : Application() {

    init {
        instance = this
        alreadyCalledversionCheck = false
    }

    companion object {
        private var instance: MyApplication? = null
        var alreadyCalledversionCheck: Boolean? = false

        var analytics: FirebaseAnalytics? = null

        fun logEvent(event:String,data: Bundle?){
            analytics?.logEvent(event,data)
        }

        fun getInstance(): Context {
            return instance!!.applicationContext
        }

    }


    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        analytics = FirebaseAnalytics.getInstance(this@MyApplication)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
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
            Log.e("Undeliverable exception", e.toString())
        }


    }



}