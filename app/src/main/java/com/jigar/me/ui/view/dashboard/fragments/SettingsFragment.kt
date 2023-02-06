package com.jigar.me.ui.view.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jigar.me.R
import com.jigar.me.databinding.FragmentSettingsBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.toastS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {
    private lateinit var binding: FragmentSettingsBinding
    private var isPurchased = false

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
    }

    private fun initViews() {
        with(prefManager){
            isPurchased = (getCustomParam(AppConstants.Purchase.Purchase_All, "") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Add_Sub_level2,"") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Mul_Div_level3,"") == "Y")
            binding.isPurchased = isPurchased
        }

        setSettings()
        setTheme()
        setAbacusAnswer()
    }

    private fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
        binding.cardPurchase.onClick { mNavController.navigate(R.id.action_settingsFragment_to_purchaseFragment) }
        binding.relThemePoligon.onClick { onThemeClick(AppConstants.Settings.theam_Poligon) }
        binding.relThemeEgg.onClick { onThemeClick(AppConstants.Settings.theam_Egg) }
        binding.relThemeStar.onClick { onThemeClick(AppConstants.Settings.theam_Star) }
        binding.relThemeEyes.onClick { onThemeClick(AppConstants.Settings.theam_eyes) }
        binding.relThemeShape.onClick { onThemeClick(AppConstants.Settings.theam_shape) }

        binding.relHintSound.onClick { onOnOffClick(AppConstants.Settings.Setting__hint_sound,binding.isHintSound) }
        binding.swHintSound.onClick { onOnOffClick(AppConstants.Settings.Setting__hint_sound,binding.isHintSound) }

        binding.relAbacusSound.onClick { onOnOffClick(AppConstants.Settings.Setting_sound,binding.isAbacusSound) }
        binding.swSound.onClick { onOnOffClick(AppConstants.Settings.Setting_sound,binding.isAbacusSound) }

        binding.relAutoReset.onClick { onOnOffClick(AppConstants.Settings.Setting_auto_reset_abacus,binding.isAutoReset) }
        binding.swAutoReset.onClick { onOnOffClick(AppConstants.Settings.Setting_auto_reset_abacus,binding.isAutoReset) }

        binding.relSudokuSound.onClick { onOnOffClick(AppConstants.Settings.Setting_SudokuVolume,binding.isSudokuSound) }
        binding.swSudokuSound.onClick { onOnOffClick(AppConstants.Settings.Setting_SudokuVolume,binding.isSudokuSound) }

        binding.relNumberPuzzleSound.onClick { onOnOffClick(AppConstants.Settings.Setting_NumberPuzzleVolume,binding.isNumberPuzzleSound) }
        binding.swNumberPuzzleSound.onClick { onOnOffClick(AppConstants.Settings.Setting_NumberPuzzleVolume,binding.isNumberPuzzleSound) }

        binding.relDisplayAbacusNumber.onClick { onOnOffClick(AppConstants.Settings.Setting_display_abacus_number,binding.isDisplayAbacusNumber) }
        binding.swDisplayAbacusNumber.onClick { onOnOffClick(AppConstants.Settings.Setting_display_abacus_number,binding.isDisplayAbacusNumber) }

        binding.relDisplayHelpMessage.onClick { onOnOffClick(AppConstants.Settings.Setting_display_help_message,binding.isDisplayHelpMessage) }
        binding.swDisplayHelpMessage.onClick { onOnOffClick(AppConstants.Settings.Setting_display_help_message,binding.isDisplayHelpMessage) }

        binding.relHideTable.onClick { onOnOffClick(AppConstants.Settings.Setting_hide_table,binding.isHideTable) }
        binding.swHideTable.onClick { onOnOffClick(AppConstants.Settings.Setting_hide_table,binding.isHideTable) }

        binding.relLeftHand.onClick { onOnOffClick(AppConstants.Settings.Setting_left_hand,binding.isLeftHand) }
        binding.swLeftHand.onClick { onOnOffClick(AppConstants.Settings.Setting_hide_table,binding.isHideTable) }

        binding.relAnswerStep.onClick { onAbacusAnswerClick(AppConstants.Settings.Setting_answer_Step) }
        binding.relAnswerFinal.onClick { onAbacusAnswerClick(AppConstants.Settings.Setting_answer_Final) }

    }
    private fun setSettings() {
        with(prefManager){
            binding.isDisplayHelpMessage = getCustomParamBoolean(AppConstants.Settings.Setting_display_help_message, true)

            binding.isDisplayAbacusNumber = getCustomParamBoolean(AppConstants.Settings.Setting_display_abacus_number, true)

            binding.isHideTable = getCustomParamBoolean(AppConstants.Settings.Setting_hide_table, false)

            binding.isLeftHand = getCustomParamBoolean(AppConstants.Settings.Setting_left_hand, true)

            binding.isHintSound = getCustomParamBoolean(AppConstants.Settings.Setting__hint_sound, false)

            binding.isAbacusSound = getCustomParamBoolean(AppConstants.Settings.Setting_sound, true)

            binding.isAutoReset = getCustomParamBoolean(AppConstants.Settings.Setting_auto_reset_abacus, false)

            binding.isSudokuSound = getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y") == "y"

            binding.isNumberPuzzleSound = getCustomParamBoolean(AppConstants.Settings.Setting_NumberPuzzleVolume, true)
        }

    }
    private fun setTheme() {
        binding.abacusTheme = prefManager.getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default)
    }

    private fun setAbacusAnswer() {
        binding.isStepByStep = prefManager.getCustomParam(AppConstants.Settings.Setting_answer,AppConstants.Settings.Setting_answer_Step) == AppConstants.Settings.Setting_answer_Step
    }

    private fun onAbacusAnswerClick(answerType: String) {
        prefManager.setCustomParam(AppConstants.Settings.Setting_answer,answerType)
        setAbacusAnswer()
    }

    private fun onOnOffClick(type: String, isChecked: Boolean?) {
        if (type == AppConstants.Settings.Setting_SudokuVolume){
            if (isChecked == true){
                prefManager.setCustomParam(AppConstants.Settings.Setting_SudokuVolume,"n")
            }else{
                prefManager.setCustomParam(AppConstants.Settings.Setting_SudokuVolume,"y")
            }
        }else if (type == AppConstants.Settings.Setting__hint_sound){
            if (isChecked == true){
                prefManager.setCustomParamBoolean(type, false)
            }else{
                if (isPurchased){
                    prefManager.setCustomParamBoolean(type, true)
                }else{
                    prefManager.setCustomParamBoolean(type, false)
                    requireContext().toastS(getString(R.string.txt_setting_hintsound_purchase))
                }
            }
        }else{
            if (isChecked == true){
                prefManager.setCustomParamBoolean(type, false)
            }else{
                prefManager.setCustomParamBoolean(type, true)
            }

        }
        setSettings()
    }

    private fun onThemeClick(themeType: String) {
        if (themeType != prefManager.getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default)){
            when {
                themeType == AppConstants.Settings.theam_Egg || themeType == AppConstants.Settings.theam_Poligon -> {
                    prefManager.setCustomParam(AppConstants.Settings.Theam,themeType)
                    setTheme()
                }
                isPurchased -> {
                    prefManager.setCustomParam(AppConstants.Settings.Theam,themeType)
                    setTheme()
                }
                else -> {
                    requireContext().toastS(getString(R.string.txt_setting_hintsound_purchase))
                }
            }
        }
    }

}