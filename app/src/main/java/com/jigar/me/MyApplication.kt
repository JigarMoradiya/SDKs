package com.jigar.me

import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.jigar.me.data.model.NotificationData
import com.jigar.me.internal.service.download.CustomFileDownloader
import com.jigar.me.internal.service.download.FileDownloadNotificationManager
import com.jigar.me.ui.view.dashboard.MainDashboardActivity
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.openURL
import com.jigar.me.utils.extensions.openYoutube
import com.jigar.me.utils.extensions.shareIntent
import com.onesignal.OneSignal
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2core.Downloader
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException
import javax.inject.Inject


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

        private var fetch: Fetch? = null
        fun getFetchInstance(): Fetch {

            if (fetch == null || fetch?.isClosed == true) {
                val fetchConfiguration = FetchConfiguration.Builder(getInstance())
                    .setDownloadConcurrentLimit(10)
                    .setHttpDownloader(CustomFileDownloader(Downloader.FileDownloaderType.PARALLEL))
                    .setNamespace(Constants.FETCH_NAMESPACE)
                    .setNotificationManager(object :
                        FileDownloadNotificationManager(getInstance()) {
                        override fun getFetchInstanceForNamespace(namespace: String): Fetch {
                            return fetch!!
                        }
                    })
                    .build()
                fetch = Fetch.Impl.getInstance(fetchConfiguration)
            }
            return fetch!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        analytics = FirebaseAnalytics.getInstance(this@MyApplication)

        oneSignal()

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

    private fun oneSignal() {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.ONE_SIGNAL)

        OneSignal.setNotificationOpenedHandler { result ->
            Log.d("notification_data", result.toString())
            val additional_data = result.notification.additionalData.toString()
            if (additional_data.isNotEmpty()) {
                val notification = Gson().fromJson(additional_data, NotificationData::class.java)

                if (notification != null) {
                    when (notification.type) {
                        Constants.notificationTypeStarter -> {
                            moveToDestination(R.id.fullAbacusFragment)
                        }
                        Constants.notificationTypeNumber -> {
                            moveToPages(AppConstants.HomeClicks.Menu_Number,getString(R.string.Number))
                        }
                        Constants.notificationTypeAddition -> {
                            moveToPages(AppConstants.HomeClicks.Menu_Addition,getString(R.string.Addition))
                        }
                        Constants.notificationTypeSubtraction -> {
                            moveToPages(AppConstants.HomeClicks.Menu_Addition_Subtraction,getString(R.string.AdditionSubtraction))
                        }
                        Constants.notificationTypeMultiplication -> {
                            moveToPages(AppConstants.HomeClicks.Menu_Multiplication,getString(R.string.Multiplication))
                        }
                        Constants.notificationTypeDivision -> {
                            moveToPages(AppConstants.HomeClicks.Menu_Division,getString(R.string.Division))
                        }
                        Constants.notificationTypeMaterial -> {
                            moveToDestination(R.id.materialHomeFragment)
                        }
                        Constants.notificationTypeExercise -> {
                            moveToDestination(R.id.exerciseHomeFragment)
                        }
                        Constants.notificationTypeMaterialMath -> {
                            moveToPractiseMaterialType(AppConstants.Extras_Comman.DownloadType_Maths)
                        }
                        Constants.notificationTypeMaterialNursery -> {
                            moveToPractiseMaterialType(AppConstants.Extras_Comman.DownloadType_Nursery)
                        }
                        Constants.notificationTypeExam -> {
                            moveToDestination(R.id.examHomeFragment)
                        }
                        Constants.notificationTypeNumberSequence -> {
                            moveToDestination(R.id.puzzleNumberHomeFragment)
                        }
                        Constants.notificationTypeSetting -> {
                            moveToDestination(R.id.settingsFragment)
                        }
                        Constants.notificationTypePurchase -> {
                            moveToDestination(R.id.purchaseFragment)
                        }
                        Constants.notificationTypeYoutube -> {
                            this.openYoutube(notification.youtube_url)
                        }
                        Constants.notificationTypeRate -> {
                            this.openURL("https://play.google.com/store/apps/details?id=${this.packageName}")
                        }
                        Constants.notificationTypeShare -> {
                            this.shareIntent()
                        }
                        else -> {
                            moveToDestination(R.id.homeFragment)
                        }
                    }
                }else{
                    moveToDestination(R.id.homeFragment)
                }

            }else{
                moveToDestination(R.id.homeFragment)
            }
        }


        OneSignal.setNotificationWillShowInForegroundHandler {

        }
    }

    private fun moveToPages(from : Int, title : String) {
        val args = Bundle()
        args.putInt("from", from)
        args.putString("title", title)

        NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_navigation_graph)
            .setDestination(R.id.pageFragment)
            .setArguments(args)
            .setComponentName(MainDashboardActivity::class.java)
            .createTaskStackBuilder().getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)!!
            .send()
    }
    private fun moveToPractiseMaterialType(downloadType : String) {
        val args = Bundle()
        args.putString("downloadType", downloadType)

        NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_navigation_graph)
            .setDestination(R.id.materialDownloadFragment)
            .setArguments(args)
            .setComponentName(MainDashboardActivity::class.java)
            .createTaskStackBuilder().getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)!!
            .send()
    }
    private fun moveToDestination(id : Int) {
        NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_navigation_graph)
            .setDestination(id)
            .setComponentName(MainDashboardActivity::class.java)
            .createTaskStackBuilder().getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)!!
            .send()
    }

}
