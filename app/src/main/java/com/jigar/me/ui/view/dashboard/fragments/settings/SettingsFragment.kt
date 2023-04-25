package com.jigar.me.ui.view.dashboard.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.jigar.me.R
import com.jigar.me.data.local.data.AbacusBeadType
import com.jigar.me.data.local.data.AbacusContent
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.databinding.FragmentSettingsBinding
import com.jigar.me.databinding.LayoutAbacusExamBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.AbacusUtils
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment(), AbacusThemeSelectionsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentSettingsBinding
    private var isPurchased = false
    private lateinit var mNavController: NavController
    private lateinit var abacusThemeFreeAdapter: AbacusThemeSelectionsAdapter
    private lateinit var abacusThemePaidAdapter: AbacusThemeSelectionsAdapter
    private var selectedFreePosition : Int = -1
    private var selectedPaidPosition : Int = -1
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initViews()
        initListener()
        return binding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun initViews() {
        with(prefManager){
            isPurchased = (getCustomParam(AppConstants.Purchase.Purchase_All, "") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Add_Sub_level2,"") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Mul_Div_level3,"") == "Y")
            binding.isPurchased = isPurchased
        }

        binding.recyclerviewAbacusDefault.post {
            val columnFree = CommonUtils.calculateNoOfColumns((resources.getDimension(R.dimen.bead_column).toInt().dp.toFloat()),binding.recyclerviewAbacusDefault.width.dp.toFloat())
            binding.recyclerviewAbacusDefault.layoutManager = GridLayoutManager(requireContext(),columnFree)
            abacusThemeFreeAdapter = AbacusThemeSelectionsAdapter(DataProvider.getAbacusThemeFreeTypeList(requireContext(),AbacusBeadType.Exam),this@SettingsFragment, selectedFreePosition)
            binding.recyclerviewAbacusDefault.adapter = abacusThemeFreeAdapter

            val columnPaid = CommonUtils.calculateNoOfColumns((resources.getDimension(R.dimen.bead_column_paid).toInt().dp.toFloat()),binding.recyclerviewAbacusDefault.width.dp.toFloat())
            binding.recyclerviewAbacusPaid.layoutManager = GridLayoutManager(requireContext(),columnPaid)
            abacusThemePaidAdapter = AbacusThemeSelectionsAdapter(DataProvider.getAbacusThemePaidTypeList(requireContext(),AbacusBeadType.Exam),this@SettingsFragment, selectedPaidPosition, true)
            binding.recyclerviewAbacusPaid.adapter = abacusThemePaidAdapter

            setTheme()
        }
        setSettings()
        setAbacusAnswer()
    }

    private fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
        binding.cardPurchase.onClick { mNavController.navigate(R.id.action_settingsFragment_to_purchaseFragment) }

        binding.relHintSound.onClick { onOnOffClick(AppConstants.Settings.Setting__hint_sound,binding.isHintSound) }
        binding.swHintSound.onClick { onOnOffClick(AppConstants.Settings.Setting__hint_sound,binding.isHintSound) }

        binding.relAbacusSound.onClick { onOnOffClick(AppConstants.Settings.Setting_sound,binding.isAbacusSound) }
        binding.swSound.onClick { onOnOffClick(AppConstants.Settings.Setting_sound,binding.isAbacusSound) }

        binding.relAutoReset.onClick { onOnOffClick(AppConstants.Settings.Setting_auto_reset_abacus,binding.isAutoReset) }
        binding.swAutoReset.onClick { onOnOffClick(AppConstants.Settings.Setting_auto_reset_abacus,binding.isAutoReset) }

        binding.relNumberPuzzleSound.onClick { onOnOffClick(AppConstants.Settings.Setting_NumberPuzzleVolume,binding.isNumberPuzzleSound) }
        binding.swNumberPuzzleSound.onClick { onOnOffClick(AppConstants.Settings.Setting_NumberPuzzleVolume,binding.isNumberPuzzleSound) }

        binding.relDisplayAbacusNumber.onClick { onOnOffClick(AppConstants.Settings.Setting_display_abacus_number,binding.isDisplayAbacusNumber) }
        binding.swDisplayAbacusNumber.onClick { onOnOffClick(AppConstants.Settings.Setting_display_abacus_number,binding.isDisplayAbacusNumber) }

        binding.relDisplayHelpMessage.onClick { onOnOffClick(AppConstants.Settings.Setting_display_help_message,binding.isDisplayHelpMessage) }
        binding.swDisplayHelpMessage.onClick { onOnOffClick(AppConstants.Settings.Setting_display_help_message,binding.isDisplayHelpMessage) }

        binding.relHideTable.onClick { onOnOffClick(AppConstants.Settings.Setting_hide_table,binding.isHideTable) }
        binding.swHideTable.onClick { onOnOffClick(AppConstants.Settings.Setting_hide_table,binding.isHideTable) }

        binding.relLeftHand.onClick { onOnOffClick(AppConstants.Settings.Setting_left_hand,binding.isLeftHand) }
        binding.swLeftHand.onClick { onOnOffClick(AppConstants.Settings.Setting_left_hand,binding.isLeftHand) }

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

            binding.isNumberPuzzleSound = getCustomParamBoolean(AppConstants.Settings.Setting_NumberPuzzleVolume, true)
        }

    }
    private fun setTheme() {
        selectedFreePosition = -1
        selectedPaidPosition = -1
        val freeList = DataProvider.getAbacusThemeFreeTypeList(requireContext(),AbacusBeadType.Exam)
        if (prefManager.getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default).contains(AppConstants.Settings.theam_Default,true)){
            val position : Int? = freeList.indexOfFirst { it.type.equals(prefManager.getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default),true) }
            if (position != null && position != -1){
                selectedFreePosition = position
                setPreviewTheme(freeList[position].type)
            }
        }else{
            val paidList = DataProvider.getAbacusThemePaidTypeList(requireContext(),AbacusBeadType.Exam)
            val position : Int? = paidList.indexOfFirst { it.type.equals(prefManager.getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default),true) }
            if (position != null && position != -1){
                selectedPaidPosition = position
                setPreviewTheme(paidList[position].type)
            }
        }
        abacusThemeFreeAdapter.selectedPos(selectedFreePosition)
        abacusThemePaidAdapter.selectedPos(selectedPaidPosition)
    }

    private fun setPreviewTheme(theme : String) {
        prefManager.setCustomParam(AppConstants.Settings.TheamTempView,theme)
        binding.linearAbacus.removeAllViews()
        binding.linearAbacusPreview.invisible()


        lifecycleScope.launch {
            val abacusBinding : LayoutAbacusExamBinding = LayoutAbacusExamBinding.inflate(layoutInflater, null, false)
//        val abacusBinding : FragmentAbacusSubBinding = FragmentAbacusSubBinding.inflate(layoutInflater, null, false)
            binding.linearAbacus.addView(abacusBinding.root)
            val themeContent = DataProvider.findAbacusThemeType(requireContext(),theme, AbacusBeadType.Exam)
            themeContent.abacusFrameExam135.let {
                abacusBinding.rlAbacusMain.setBackgroundResource(it)
            }
            themeContent.dividerColor1.let {
                abacusBinding.ivDivider.setBackgroundColor(ContextCompat.getColor(requireContext(),it))
            }
            themeContent.resetBtnColor8.let {
                abacusBinding.ivReset.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
                binding.txtPreview.setTextColor(ContextCompat.getColor(requireContext(),it))
            }
            AbacusUtils.setAbacusColumnTheme(AbacusBeadType.SettingPreview,abacusBinding.abacusTop,abacusBinding.abacusBottom)
            binding.linearAbacusPreview.show()
            val number = DataProvider.generateSingleDigit(1, 998).toString()
            abacusBinding.tvCurrentVal.text = number
            AbacusUtils.setNumber(number,abacusBinding.abacusTop,abacusBinding.abacusBottom)

        }
    }

    override fun onThemePoligonItemClick(data: AbacusContent) {
        onThemeClick(data.type)
    }

    private fun setAbacusAnswer() {
        binding.isStepByStep = prefManager.getCustomParam(AppConstants.Settings.Setting_answer,AppConstants.Settings.Setting_answer_Step) == AppConstants.Settings.Setting_answer_Step
    }

    private fun onAbacusAnswerClick(answerType: String) {
        prefManager.setCustomParam(AppConstants.Settings.Setting_answer,answerType)
        setAbacusAnswer()
    }

    private fun onOnOffClick(type: String, isChecked: Boolean?) {
       if (type == AppConstants.Settings.Setting__hint_sound){
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
                themeType.contains(AppConstants.Settings.theam_Default,true) -> {
                    prefManager.setCustomParam(AppConstants.Settings.Theam,themeType)
                    abacusThemePaidAdapter.selectedPos(-1)
                }
                isPurchased -> {
                    prefManager.setCustomParam(AppConstants.Settings.Theam,themeType)
                    abacusThemeFreeAdapter.selectedPos(-1)
                }
                else -> {
                    abacusThemeFreeAdapter.selectedPos(-1)
                    requireContext().toastS(getString(R.string.txt_setting_hintsound_purchase))
                }
            }
            setPreviewTheme(themeType)
        }
    }



}