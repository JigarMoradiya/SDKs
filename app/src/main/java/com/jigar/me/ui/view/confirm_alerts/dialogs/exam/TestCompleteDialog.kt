package com.jigar.me.ui.view.confirm_alerts.dialogs.exam

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.jigar.me.R
import com.jigar.me.databinding.DialogExamCompleteBinding
import com.jigar.me.databinding.RawPagelistParentBinding
import com.jigar.me.utils.extensions.layoutInflater

object TestCompleteDialog {

    var alertdialog: AlertDialog? = null

    fun hideDialog() {
        alertdialog?.dismiss()
    }

    fun showPopup(
        activity: Activity,
        totalTime: String,
        totalSkip: String,
        totalWrong: String,
        totalRight: String,
        totalQuestion: String,
        listener: TestCompleteDialogInterface
    ) {

        val alertLayout = DialogExamCompleteBinding.inflate(activity.layoutInflater,null,false)
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setView(alertLayout.root)

        alertLayout.txtTotalTime.text = totalTime
        alertLayout.txtSkipQue.text = totalSkip
        alertLayout.txtWrongQue.text = totalWrong
        alertLayout.txtRightQue.text = totalRight
        alertLayout.txtTotalQue.text = totalQuestion

        val percentage : Float = ((totalRight.toFloat() * 5) / totalQuestion.toFloat())
        alertLayout.simpleRatingBar.rating = percentage
        alertLayout.btnNo.setOnClickListener {
            hideDialog()
            listener.testCompleteClose()
        }
        alertLayout.btnYes.setOnClickListener {
            hideDialog()
            listener.testCompleteGotoResult()
        }
        alertBuilder.setView(alertLayout.root)
        alertBuilder.setCancelable(false)
        alertdialog = alertBuilder.show()
        val windows = alertdialog?.window
        val colorD = ColorDrawable(Color.TRANSPARENT)
        val insetD = InsetDrawable(colorD, 100, 5, 100, 5)
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

    interface TestCompleteDialogInterface {
        fun testCompleteClose()
        fun testCompleteGotoResult()
    }

}