package com.jigar.me.ui.view.confirm_alerts.dialogs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.jigar.me.R
import com.jigar.me.databinding.DialogCompleteNumberPuzzleBinding

object NumberSequenceCompleteDialog {

    var alertdialog: AlertDialog? = null

    fun hideDialog() {
        alertdialog?.dismiss()
    }

    fun showPopup(
        activity: Activity, type: Int, totalMoves: Int, bestMoves: Int,
        listener: NumberSequenceCompleteDialogInterface
    ) {

        val inflater = activity.layoutInflater
        val alertLayout: DialogCompleteNumberPuzzleBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_complete_number_puzzle, null, false)
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setView(alertLayout.root)

        alertLayout.txtSubTitle.text = activity.getString(R.string.you_completed_this_puzzle_in_moves,totalMoves.toString())
        val puzzleType = when (type) {
            4 -> {
                activity.getString(R.string._4_x_4_puzzle)
            }
            5 -> {
                activity.getString(R.string._5_x_5_puzzle)
            }
            else -> {
                activity.getString(R.string._3_x_3_puzzle)
            }
        }
        if (totalMoves <= bestMoves){
            alertLayout.txtSubTitle1.text = activity.getString(R.string.this_is_your_best_moves_in_puzzle, puzzleType)
        }else{
            alertLayout.txtSubTitle1.text = activity.getString(R.string.but_your_best_moves_in_puzzle_is,puzzleType)+" "+bestMoves
            alertLayout.txtSubTitle1.setTextColor(ContextCompat.getColor(activity,R.color.colorOrange))
        }

        alertLayout.btnNo.setOnClickListener {
            hideDialog()
            listener.numberSequenceCompleteClose()
        }
        alertLayout.btnYes.setOnClickListener {
            hideDialog()
            listener.numberSequenceCompleteContinue()
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

    interface NumberSequenceCompleteDialogInterface {
        fun numberSequenceCompleteClose()
        fun numberSequenceCompleteContinue()
    }

}