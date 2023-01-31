package com.jigar.me.ui.view.confirm_alerts.dialogs.exam

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.jigar.me.R
import com.jigar.me.databinding.DialogAlertBinding

object TestLeaveDialog {

    var alertdialog: AlertDialog? = null

    fun hideDialog() {
        alertdialog?.dismiss()
    }

    fun showPopup(activity: Activity,listener: TestLeaveDialogInterface) {
        val alertLayout = DialogAlertBinding.inflate(activity.layoutInflater,null,false)
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setView(alertLayout.root)

        alertLayout.txtTitle.text = activity.resources.getText(R.string.test)
        alertLayout.txtQue.text = activity.resources.getText(R.string.test_cancel)
        alertLayout.btnYes.text = activity.resources.getText(R.string.txtYES)
        alertLayout.btnNo.text = activity.resources.getText(R.string.txtNo)

        alertLayout.btnNo.setOnClickListener {
            hideDialog()
        }
        alertLayout.btnYes.setOnClickListener {
            hideDialog()
            listener.testLeaveConfirm()
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

    interface TestLeaveDialogInterface {
        fun testLeaveConfirm()
    }

}