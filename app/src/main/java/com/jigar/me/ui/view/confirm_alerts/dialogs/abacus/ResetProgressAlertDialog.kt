package com.jigar.me.ui.view.confirm_alerts.dialogs.abacus

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.jigar.me.R
import com.jigar.me.databinding.DialogAlertBinding
import com.jigar.me.utils.extensions.layoutInflater

object ResetProgressAlertDialog {

    var alertdialog: AlertDialog? = null

    fun hideDialog() {
        alertdialog?.dismiss()
    }

    fun showPopup(context: Context,listener: ResetProgressAlertDialogInterface) {

        val inflater = context.layoutInflater
        val alertLayout: DialogAlertBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_alert, null, false)
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setView(alertLayout.root)

        alertLayout.txtTitle.text = context.resources.getString(R.string.txt_reset_page)
        alertLayout.txtQue.text = context.resources.getString(R.string.txt_reset_page_alert)
        alertLayout.btnYes.text = context.resources.getString(R.string.txtYES)
        alertLayout.btnNo.text = context.resources.getString(R.string.txtNo)

        alertLayout.btnNo.setOnClickListener {
            hideDialog()
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

    interface ResetProgressAlertDialogInterface {
        fun resetProgressConfirm()
    }

}