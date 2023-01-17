package com.jigar.me.ui.view.confirm_alerts.dialogs.abacus

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.jigar.me.R
import com.jigar.me.databinding.DialogAlertBinding

object CompleteResetAlertDialog {

    var alertdialog: AlertDialog? = null

    fun hideDialog() {
        alertdialog?.dismiss()
    }

    fun showPopup(activity: Activity,listener: CompleteResetAlertDialogInterface) {

        val inflater = activity.layoutInflater
        val alertLayout: DialogAlertBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_alert, null, false)
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setView(alertLayout.root)

        alertLayout.txtTitle.text = activity.resources.getString(R.string.app_name)
        alertLayout.txtQue.text = activity.resources.getString(R.string.txt_page_completed_already_purchased)
        alertLayout.btnYes.text = activity.resources.getString(R.string.txt_reset_now)

        alertLayout.btnNo.setOnClickListener {
            hideDialog()
            listener.goBack()
        }
        alertLayout.btnYes.setOnClickListener {
            hideDialog()
            listener.resetProgressConfirm()
        }
        alertBuilder.setView(alertLayout.root)
        alertBuilder.setCancelable(false)
        alertdialog = alertBuilder.show()
        alertdialog!!.setCanceledOnTouchOutside(false)
        val windows = alertdialog?.window
        val colorD = ColorDrawable(Color.TRANSPARENT)
        val insetD = InsetDrawable(colorD, 40, 5, 40, 5)
        windows?.setBackgroundDrawable(insetD)
        // Setting Animation for Appearing from Center
        windows?.attributes?.windowAnimations = R.style.DialogAppearFromCenter
        // Positioning it in Bottom Right
        val wlp = windows?.attributes
        wlp?.width = WindowManager.LayoutParams.WRAP_CONTENT
        wlp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp?.gravity = Gravity.CENTER
        windows?.attributes = wlp
        alertdialog?.show()
    }

    interface CompleteResetAlertDialogInterface {
        fun resetProgressConfirm()
        fun goBack()
    }

}