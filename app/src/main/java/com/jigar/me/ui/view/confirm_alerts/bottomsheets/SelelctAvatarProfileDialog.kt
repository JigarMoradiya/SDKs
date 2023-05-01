package com.jigar.me.ui.view.confirm_alerts.bottomsheets

import android.app.Activity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jigar.me.R
import com.jigar.me.data.local.data.*
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.databinding.DialogAvatarProfileBinding
import com.jigar.me.ui.view.confirm_alerts.adapter.SelectAvatarAdapter
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.*

object SelelctAvatarProfileDialog {
    private lateinit var selectAvatarAdapter: SelectAvatarAdapter
    fun showPopup(activity: Activity,prefManager : AppPreferencesHelper, listener: AvatarProfileDialogInterface) {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
        val sheetBinding: DialogAvatarProfileBinding = DialogAvatarProfileBinding.inflate(activity.layoutInflater)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        with(sheetBinding){
            val avatarList = DataProvider.getAvatarList()
            val avatarId = prefManager.getCustomParamInt(Constants.avatarId,0)
            val index : Int? = avatarList.map { it -> it.id }.indexOf(avatarId)
            if (index != null) {
                if (index > -1){
                    tvChooseAvatar.hide()
                    cardEditImage.show()
                    ivAvatar.setImageResource(avatarList[index].image)
                    edtUserName.setText(prefManager.getCustomParam(Constants.childName,""))
                }
            }
            selectAvatarAdapter = SelectAvatarAdapter(avatarList,index)
            tvCancel.onClick {
                linearChooseAvatar.hide()
                linearMain.show()
            }
            tvClose.onClick {
                bottomSheetDialog.dismiss()
            }
            cardEditImage.onClick{
                tvChooseAvatar.performClick()
            }
            tvChooseAvatar.onClick{
                context.hideKeyboard(edtUserName)
                recyclerViewAvatar.adapter = selectAvatarAdapter
                linearChooseAvatar.show()
                linearMain.hide()
            }
            btnSubmit.onClick {
                if (selectAvatarAdapter.selectedPosition == -1 && avatarId == 0){
                    context.toastS(context.getString(R.string.please_select_avatar))
                }else if (edtUserName.text.toString().trim().isEmpty()){
                    context.toastS(context.getString(R.string.please_enter_anonymously_name))
                }else{
                    bottomSheetDialog.dismiss()
                    val selectedAvatar = avatarList[selectAvatarAdapter.selectedPosition]
                    prefManager.setCustomParamInt(Constants.avatarId,selectedAvatar.id)
                    prefManager.setCustomParam(Constants.childName,edtUserName.text.toString().trim())
                    listener.avatarProfileCloseDialog()
                }
            }
            btnDone.onClick {
                if (selectAvatarAdapter.selectedPosition == -1 && avatarId == 0){
                    context.toastS(context.getString(R.string.please_select_avatar))
                }else{
                    ivAvatar.setImageResource(avatarList[selectAvatarAdapter.selectedPosition].image)
                    tvChooseAvatar.hide()
                    cardEditImage.show()
                    linearChooseAvatar.hide()
                    linearMain.show()
                }
            }
            bottomSheetDialog.setContentView(root)
        }
        activity.setBottomSheetDialogAttr(bottomSheetDialog,Constants.bottomSheetWidthBaseOnRatio5)
        bottomSheetDialog.show()
    }

    interface AvatarProfileDialogInterface {
        fun avatarProfileCloseDialog()
    }

}