 package com.jigar.me.utils.extensions

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Toast
import com.jigar.me.R
import com.jigar.me.utils.AppConstants
import java.io.File


val Context.downloadManager: DownloadManager
    get() = getSystemServiceAs(Context.DOWNLOAD_SERVICE)

val Context.layoutInflater: LayoutInflater
    get() = getSystemServiceAs(Context.LAYOUT_INFLATER_SERVICE)

val Context.notificationManager: NotificationManager
    get() = getSystemServiceAs(Context.NOTIFICATION_SERVICE)



@Suppress("UNCHECKED_CAST")
fun <T> Context.getSystemServiceAs(serviceName: String) = getSystemService(serviceName) as T

val Context.isNetworkAvailable: Boolean
    get() {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < 23) {
            val ni = cm.activeNetworkInfo
            if (ni != null) {
                return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
            }
        } else {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
        }
        return false
    }

fun Context.toastS(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastL(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.openMail() {
    val emailId = resources.getText(R.string.txt_mail).toString()
    val email = Intent(Intent.ACTION_SENDTO,Uri.fromParts(
        "mailto",emailId, null))
//    email.type = "message/rfc822"
    email.putExtra(Intent.EXTRA_EMAIL,arrayOf(emailId.toString()))
    email.putExtra(Intent.EXTRA_SUBJECT, resources.getText(R.string.app_name))
    email.putExtra(Intent.EXTRA_TEXT, "")
//    email.setPackage("com.google.android.gm")
//    if (email.resolveActivity(packageManager)!=null){
        startActivity(Intent.createChooser(email, resources.getString(R.string.app_name)))
//    }else{
//        toastS(getString(R.string.gmail_not_found))
//    }

}
fun Context.openPlayStore() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
    } catch (e: Exception) {
        e.printStackTrace()
        toastS(getString(R.string.play_store_not_found))
    }
}
fun Context.shareIntent(msg : String) {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
    sharingIntent.putExtra(Intent.EXTRA_TEXT, msg)
    startActivity(sharingIntent)
}

 fun Context.openYoutube() {
     val url = AppConstants.YOUTUBE_URL
     val i = Intent(Intent.ACTION_VIEW)
     i.data = Uri.parse(url)
     startActivity(i)
 }



fun Context.downloadFilePath() : String?{
    val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getString(R.string.download_folder))
    if (!folder.exists()) {
        folder.mkdir()
    }
//    return getExternalFilesDir("download")?.path
    return folder.path
}