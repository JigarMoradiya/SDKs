package com.jigar.me.ui.view.confirm_alerts.bottomsheets

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.jigar.me.R
import com.jigar.me.databinding.BottomSheetsFreeVsPaidBinding
import com.jigar.me.utils.extensions.onClick

object PurchaseInfoBottomSheetDialog {
    var alertdialog: AlertDialog? = null
    fun showPopup(activity: Activity) {
        val alertLayout = BottomSheetsFreeVsPaidBinding.inflate(activity.layoutInflater,null,false)
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setView(alertLayout.root)

        alertLayout.tvClose.onClick {
            alertdialog?.dismiss()
        }
        alertBuilder.setCancelable(false)
        alertdialog = alertBuilder.show()
        val windows = alertdialog?.window
        val colorD = ColorDrawable(Color.TRANSPARENT)
        val insetD = InsetDrawable(colorD, 150, 50, 150, 10)
        windows?.setBackgroundDrawable(insetD)
        // Setting Animation for Appearing from Center
        windows?.attributes?.windowAnimations = R.style.DialogAppearFromCenter
        // Positioning it in Bottom Right
        val wlp = windows?.attributes
        wlp?.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp?.gravity = Gravity.CENTER
        windows?.attributes = wlp
        alertdialog?.show()
    }

}