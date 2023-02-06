package com.jigar.me.ui.view.confirm_alerts.bottomsheets

import android.app.Activity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jigar.me.R
import com.jigar.me.databinding.BottomSheetNumberSequencCompleteBinding
import com.jigar.me.databinding.BottomSheetsFreeVsPaidBinding
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.setBottomSheetDialogAttr

object PurchaseInfoBottomSheetDialog {

    fun showPopup(activity: Activity) {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
        val sheetBinding: BottomSheetsFreeVsPaidBinding = BottomSheetsFreeVsPaidBinding.inflate(activity.layoutInflater)

        bottomSheetDialog.setContentView(sheetBinding.root)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setCanceledOnTouchOutside(true)

        sheetBinding.tvClose.onClick {
            bottomSheetDialog.dismiss()
        }
        activity.setBottomSheetDialogAttr(bottomSheetDialog, Constants.bottomSheetWidthBaseOnRatio6)
        bottomSheetDialog.show()
    }

}