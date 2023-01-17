package com.jigar.me.ui.view.confirm_alerts.bottomsheets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jigar.me.R
import com.jigar.me.databinding.BottomsheetsFreeVsPaidBinding
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick

object PurchaseInfoBottomSheet {

    fun showPopup(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val sheetView: BottomsheetsFreeVsPaidBinding =
            DataBindingUtil.inflate(context.layoutInflater, R.layout.bottomsheets_free_vs_paid, null,false)

        sheetView.viewLine.onClick {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetDialog.setContentView(sheetView.root)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setCanceledOnTouchOutside(true)

        val windows = bottomSheetDialog.window
        val colorD = ColorDrawable(Color.TRANSPARENT)
        val insetD = InsetDrawable(colorD, 250, 0, 250, 0)
        windows?.setBackgroundDrawable(insetD)
        val wlp = windows?.attributes
        wlp?.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp?.gravity = Gravity.CENTER
        windows?.attributes = wlp

        bottomSheetDialog.show()
    }

}