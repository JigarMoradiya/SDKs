package com.jigar.me.ui.view.confirm_alerts.dialogs.sudoku

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.jigar.me.R
import com.jigar.me.databinding.DialogAboutSudokuBinding
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick

object AboutSudokuDialog {

    var alertdialog: AlertDialog? = null

    fun hideDialog() {
        alertdialog?.dismiss()
    }

    fun showPopup(context: Context) {

        val inflater = context.layoutInflater
        val mBinding: DialogAboutSudokuBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_about_sudoku, null, false)
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setView(mBinding.root)

        mBinding.txtClose.onClick {
            hideDialog()
        }
        alertBuilder.setView(mBinding.root)
        alertBuilder.setCancelable(true)
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

}