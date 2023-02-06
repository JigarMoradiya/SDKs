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
import com.jigar.me.databinding.DialogSudokuCompleteBinding
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick

object SudokuCompleteDialog {

    var alertdialog: AlertDialog? = null

    fun showPopup(context: Context,totalTime: String,listener: SudokuCompleteDialogInterface) {

        val alertLayout = DialogSudokuCompleteBinding.inflate(context.layoutInflater,null,false)
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setView(alertLayout.root)

        alertLayout.txtTotalTime.text = totalTime
        alertLayout.btnYes.onClick {
            alertdialog?.dismiss()
            listener.sudokuCompleteDialogCloseClick()
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
        wlp?.width = WindowManager.LayoutParams.WRAP_CONTENT
        wlp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp?.gravity = Gravity.CENTER
        windows?.attributes = wlp
        alertdialog?.show()
    }

    interface SudokuCompleteDialogInterface {
        fun sudokuCompleteDialogCloseClick()
    }

}