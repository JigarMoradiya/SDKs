 package com.jigar.me.utils.extensions

import android.app.Activity
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Insets
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jigar.me.R
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import java.io.File


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

 fun Activity.setBottomSheetDialogAttr(bottomSheetDialog: BottomSheetDialog,widthRatio : Int = Constants.bottomSheetWidthBaseOnRatio5) {
     bottomSheetDialog.setOnShowListener { dialog ->
         val d = dialog as BottomSheetDialog
         val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
         val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
         bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
         bottomSheetBehavior.isHideable = true
         bottomSheetBehavior.peekHeight = 0

         bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
             override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                 when (newState) {
                     BottomSheetBehavior.STATE_COLLAPSED -> { bottomSheetDialog.cancel() }
                     BottomSheetBehavior.STATE_DRAGGING -> Unit
                     BottomSheetBehavior.STATE_EXPANDED -> Unit
                     BottomSheetBehavior.STATE_HALF_EXPANDED -> Unit
                     BottomSheetBehavior.STATE_HIDDEN -> Unit
                     BottomSheetBehavior.STATE_SETTLING -> Unit
                 }
             }
             override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
         })
     }

     val width = getScreenWidth() / widthRatio
     val windows = bottomSheetDialog.window
     val colorD = ColorDrawable(Color.TRANSPARENT)
     val insetD = InsetDrawable(colorD, width, 0, width, 0)
     windows?.setBackgroundDrawable(insetD)
     val wlp = windows?.attributes
     wlp?.width = WindowManager.LayoutParams.MATCH_PARENT
     wlp?.height = WindowManager.LayoutParams.WRAP_CONTENT
     wlp?.gravity = Gravity.CENTER
     windows?.attributes = wlp
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

 fun Context.openYoutube(url : String = AppConstants.YOUTUBE_URL) {
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