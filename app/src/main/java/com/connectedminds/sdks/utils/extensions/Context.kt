 package com.connectedminds.sdks.utils.extensions

import android.app.Activity
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Insets
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.util.*


 @Suppress("UNCHECKED_CAST")
 fun <T> Context.getSystemServiceAs(serviceName: String) = getSystemService(serviceName) as T

 val Context.downloadManager: DownloadManager
    get() = getSystemServiceAs(Context.DOWNLOAD_SERVICE)

 val Context.layoutInflater: LayoutInflater
    get() = getSystemServiceAs(Context.LAYOUT_INFLATER_SERVICE)

 val Context.notificationManager: NotificationManager
    get() = getSystemServiceAs(Context.NOTIFICATION_SERVICE)

 fun Context.toastS(message: String) {
     Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
 }

 fun Context.toastL(message: String) {
     Toast.makeText(this, message, Toast.LENGTH_LONG).show()
 }

 fun Context.setLocale(lan: String) {
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
         val myLocale = Locale(lan)
         val res: Resources = resources
         val dm: DisplayMetrics = res.displayMetrics
         val conf: Configuration = res.configuration
         conf.locale = myLocale
         res.updateConfiguration(conf, dm)
     } else {
         val locale = Locale(lan)
         Locale.setDefault(locale)
         val configuration: Configuration = resources.configuration
         configuration.setLocale(locale)
         createConfigurationContext(configuration)
     }
 }

 fun Context.hideKeyboard(view: View) {
     val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
     imm.hideSoftInputFromWindow(view.windowToken, 0)
 }

 fun Context.showKeyboard(view: View) {
     val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
     inputMethodManager!!.showSoftInput(view,0)
 }

 fun Activity.hideKeyboard() {
     val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
     //Find the currently focused view, so we can grab the correct window token from it.
     var view = currentFocus
     //If no view currently has focus, create a new one, just so we can grab a window token from it
     if (view == null) {
         view = View(this)
     }
     imm.hideSoftInputFromWindow(view.windowToken, 0)
 }



 fun Activity.getScreenWidth(): Int {
     return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
         val windowMetrics = this.windowManager.currentWindowMetrics
         val insets: Insets = windowMetrics.windowInsets
             .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
         windowMetrics.bounds.width() - insets.left - insets.right
     } else {
         val displayMetrics = DisplayMetrics()
         this.windowManager.defaultDisplay.getMetrics(displayMetrics)
         displayMetrics.widthPixels
     }
 }

val Context.isNetworkAvailable: Boolean
    get() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            return nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                    nc?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        }
        return false
    }