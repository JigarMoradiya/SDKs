package com.jigar.me.ui.view.confirm_alerts.dialogs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.jigar.me.R
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.databinding.DialogDownloadImagesBinding
import java.io.File

object MaterialPurchaseDialog {

    var alertdialog: AlertDialog? = null

    fun hideDialog() {
        alertdialog?.dismiss()
    }

    fun showPopup(activity: Activity, file : File?, data: InAppSkuDetails?, listener: MaterialPurchaseDialogInterface) {

        val alertLayout = DialogDownloadImagesBinding.inflate(activity.layoutInflater,null,false)

        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setView(alertLayout.root)

        alertLayout.txtStoragePath.text = file!!.absolutePath
        alertLayout.imgClose.setOnClickListener {
            hideDialog()
        }
        alertLayout.btnDownload.setOnClickListener {
            hideDialog()
            listener.onDownloadClick()
        }
        alertBuilder.setView(alertLayout.root)
        alertBuilder.setCancelable(false)
        alertdialog = alertBuilder.show()
        alertdialog!!.setCanceledOnTouchOutside(false)
        val windows = alertdialog?.window
        val colorD = ColorDrawable(Color.TRANSPARENT)
        val insetD = InsetDrawable(colorD, 140, 20, 140, 20)
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

    interface MaterialPurchaseDialogInterface {
        fun onPurchaseClick(data: InAppSkuDetails?)
        fun onDownloadClick()
    }

}