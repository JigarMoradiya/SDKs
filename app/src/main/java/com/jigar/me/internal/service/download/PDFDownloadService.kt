package com.jigar.me.internal.service.download

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.*
import com.jigar.me.R
import android.util.Log
import android.webkit.CookieManager
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.MyApplication
import com.jigar.me.data.model.DownloadMaterialData
import com.jigar.me.ui.view.dashboard.MainDashboardActivity
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.DateTimeUtils
import com.jigar.me.utils.extensions.downloadFilePath
import com.jigar.me.utils.extensions.isStringNotBlank
import com.jigar.me.utils.extensions.notificationManager
import com.jigar.me.utils.extensions.toastS

import com.tonyodev.fetch2.*
import com.tonyodev.fetch2.Status.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.FetchObserver
import com.tonyodev.fetch2core.Reason
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

@AndroidEntryPoint
class PDFDownloadService : Service(), FetchObserver<Download> {

    //Notification builder for updating count
    private lateinit var builder: NotificationCompat.Builder
    private val reqMap = HashMap<Int, Status>()

    //retry Counts
    private val retryCount = HashMap<Int, Int>()
    private var downloadList = ArrayList<DownloadMaterialData>()
    private var listIndex = -1

    private lateinit var fetch: Fetch
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        fetch = MyApplication.getFetchInstance()
        fetch.addListener(fetchListener)
        startForegroundService()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        listIndex = 0
        val list : ArrayList<DownloadMaterialData> = Gson().fromJson(intent.getStringExtra(
            EXTRA_DOWNLOAD_LIST
        ), object :
                TypeToken<ArrayList<DownloadMaterialData>>() {}.type)
        downloadList.addAll(list)
        if (intent.action != null) {
            val action = intent.action
            val id = intent.getIntExtra("downloadId", 0)

            if (action != null) when (action) {
                ACTION_START_FOREGROUND_SERVICE -> {
                    startDownload()
                }
                ACTION_STOP_FOREGROUND_SERVICE -> {
                    stopForegroundService()
                }
                ACTION_PAUSE -> {
                    if (id != 0){
                        fetch.pause(id)
                    }
                }
                ACTION_RESUME -> {
                    if (id != 0)
                        fetch.resume(id)
                }

                ACTION_RETRY -> {
                    if (id != 0){
                        fetch.retry(id)
                    }
                }
                ACTION_REMOVE ->{
                    if (id!=0)
                        fetch.remove(id)
                }

            }
        }
        return START_NOT_STICKY
    }

    private fun startDownload() {
        if (listIndex < downloadList.size){
            GlobalScope.launch(Dispatchers.IO){

                val url = downloadList[listIndex].pdf_path
                val currentDateFormate =  DateTimeUtils.getDateString(Date(),DateTimeUtils.yyyy_MM_dd_HH_mm)
                val filePath = downloadFilePath().plus("/").plus(downloadList[listIndex].groupName).plus("_").plus(currentDateFormate).plus(".pdf")
                File(filePath).delete()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (url.isStringNotBlank() && filePath.isStringNotBlank()) {
                        val request = Request(url, filePath)
                        request.groupId = GROUPID
                        request.addHeader("id", downloadList[listIndex].groupId)
                        request.addHeader("name", downloadList[listIndex].groupName)
                        request.addHeader("Cookie", CookieManager.getInstance().getCookie(url) ?: "")
                        request.addHeader("User-Agent", Constants.BROWSER_MOBILE_AGENET)
                        request.autoRetryMaxAttempts = 3

                        reqMap[request.id] = QUEUED
                        if (!retryCount.containsKey(request.id))
                            retryCount[request.id] = 0

                        fetch.enqueue(request, { updatedRequest: Request? ->
                            updateCounters()
                            listIndex++
                            startDownload()
                        }) { error: Error? ->
                            toastS("getting error in add download. "+ (error?.name ?: ""))
                        }
                    }

                },2000)
            }

        }else{
            listIndex = 0
            downloadList.clear()
        }
    }

    /* Used to build and start foreground service. */
    private fun startForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val intent = Intent(this, MainDashboardActivity::class.java)
        val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { PendingIntent.FLAG_MUTABLE } else { PendingIntent.FLAG_UPDATE_CURRENT }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, pendingIntentFlag)

        builder = NotificationCompat.Builder(this, getString(R.string.app_download_monitor_channel_id))
        val notification = builder
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_stat_name)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_stat_name))
            .setContentText("Downloading: 0, Completed: 0")
            .setContentTitle("Downloading...")
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(pendingIntent) //intent
            .setGroup(NOTIGROUP)
            .setOnlyAlertOnce(true)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.app_download_monitor_channel_id),
                getString(R.string.app_download_monitor_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun stopForegroundService() {
        // Stop foreground service and remove the notification.
        stopForeground(true)
        notificationManager.cancel(NOTIFICATION_ID)
        // Stop the foreground service.
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        fetch.removeListener(fetchListener)
    }

    override fun onChanged(data: Download, reason: Reason) = Unit

    private val fetchListener: FetchListener = object : AbstractFetchListener() {

        override fun onAdded(download: Download) {
            reqMap[download.id] = ADDED
        }

        override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
            reqMap[download.id] = QUEUED
        }

        override fun onCompleted(download: Download) {
            GlobalScope.launch(Dispatchers.IO){
                reqMap[download.id] = COMPLETED
                // event log for sudoku
                MyApplication.logEvent(AppConstants.FirebaseEvents.MaterialDownloaded, Bundle().apply {
                    putString("name", download.headers["name"])
                })
                // Add Media to be discovered by Different Apps.
                MediaScannerConnection.scanFile(this@PDFDownloadService, arrayOf(download.file), null) { path, uri ->
                    // Media Scanned
                    updateCounters()
                    if (shouldStopService()) {
                        stopForegroundService()
                    }
                }
            }

        }

        override fun onError(download: Download, error: Error, throwable: Throwable?) {
            reqMap[download.id] = FAILED
            updateCounters()
            var prevCount = retryCount[download.id] ?: 0
            if (prevCount < 3) {
                prevCount++
                fetch.retry(download.id)
                //retryCount.put(failedRequest.id, prevCount)
                retryCount[download.id] = prevCount
            } else {
                if (shouldStopService()) {
                    stopForegroundService()
                }
            }
        }

        override fun onProgress(download: Download, etaInMilliSeconds: Long,downloadedBytesPerSecond: Long) = Unit

        override fun onPaused(download: Download) {

            reqMap[download.id] = PAUSED
            updateCounters()
            if (shouldStopService()) stopForegroundService()
        }

        override fun onResumed(download: Download) {
            reqMap[download.id] = QUEUED
            updateCounters()
        }

        override fun onCancelled(download: Download) {
            reqMap[download.id] = CANCELLED
            updateCounters()
            if (shouldStopService()) stopForegroundService()
        }

        override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
            reqMap[download.id] = DOWNLOADING
            updateCounters()
        }


        override fun onRemoved(download: Download) {
            reqMap[download.id] = REMOVED
            updateCounters()
            if (shouldStopService())
                stopForegroundService()
        }

        override fun onDeleted(download: Download) {
            reqMap[download.id] = DELETED
            updateCounters()
            if (shouldStopService())
                stopForegroundService()
        }
    }

    private fun updateCounters() {
        var countCompleted = 0
        var downloadingCount = 0
        for (key in reqMap.keys) {
            when (reqMap[key]) {
                COMPLETED -> countCompleted++
                DOWNLOADING -> downloadingCount++
                else -> {}
            }
        }
        builder.setContentText("Downloading - $downloadingCount, Downloaded - $countCompleted")
        notificationManager.notify(NOTIFICATION_ID, builder.build())


    }

    private fun shouldStopService(): Boolean {
        for (key in reqMap.keys) {
            val processStatus: Status? = reqMap[key]
            if(processStatus == DOWNLOADING || processStatus == QUEUED || processStatus == ADDED) return false
        }
        return true
    }

    companion object {
        private const val TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE"
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_REMOVE = "ACTION_REMOVE"
        const val ACTION_RETRY = "ACTION_RETRY"
        const val ACTION_RESUME = "ACTION_RESUME"

        // Mandatory Extras
        const val EXTRA_DOWNLOAD_LIST = "extra_download_list"

        var GROUPID = "AbacusChildLearningApp".hashCode()
        //Notificatiom Group
        val NOTIGROUP = GROUPID.toString()
        const val NOTIFICATION_ID = 7765

        fun startPDFDownload(context : Context,list : List<DownloadMaterialData>){
            val intent = Intent(context, PDFDownloadService::class.java)
            intent.action = ACTION_START_FOREGROUND_SERVICE
            intent.putExtra(EXTRA_DOWNLOAD_LIST, Gson().toJson(list))
            context.startService(intent)
        }
    }
}