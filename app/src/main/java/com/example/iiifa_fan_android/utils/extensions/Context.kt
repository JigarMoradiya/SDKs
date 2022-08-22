package com.example.iiifa_fan_android.utils.extensions

import android.app.DownloadManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast

val Context.downloadManager: DownloadManager
    get() = getSystemServiceAs(Context.DOWNLOAD_SERVICE)

val Context.layoutInflater: LayoutInflater
    get() = getSystemServiceAs(Context.LAYOUT_INFLATER_SERVICE)


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

