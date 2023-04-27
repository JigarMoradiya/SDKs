package com.jigar.me.ui.view.dashboard.fragments.abacus

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jigar.me.R
import com.jigar.me.data.local.data.AbacusBeadType
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.databinding.FragmentAbacusSubKidBinding
import com.jigar.me.databinding.FragmentFullAbacusBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.AbacusMasterBeadShiftListener
import com.jigar.me.ui.view.base.abacus.AbacusMasterView
import com.jigar.me.ui.view.base.abacus.OnAbacusValueChangeListener
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.confirm_alerts.dialogs.ToddlerRangeDialog
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.dp
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class FullAbacusFragment : BaseFragment(), ToddlerRangeDialog.ToddlerRangeDialogInterface,
    AbacusMasterBeadShiftListener,
    OnAbacusValueChangeListener {
    private lateinit var binding: FragmentFullAbacusBinding
    private var abacusBinding: FragmentAbacusSubKidBinding? = null
    private var values: Int = 1
    private var random_min: Int = 0
    private var random_max: Int = 0
    private var total_count: Int = 1

    private var currentSumVal = 0f
    private var isResetRunning = false
    private var isPurchased = false
    private var is1stTime = false
    private var theme = AppConstants.Settings.theam_Egg
    private lateinit var mNavController: NavController
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentFullAbacusBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initViews()
        initListener()
        return binding.root
    }

    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    private fun initViews() {
        setAbacus()
        setSwitchs(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                binding.txtResetEveryTime.setPadding(0,16.dp,0,0)
                binding.txtRangeLable.setPadding(0,16.dp,0,0)
                binding.txtRandom.setPadding(0,16.dp,0,0)
                binding.txtStartWith1.setPadding(0,16.dp,0,0)
            } catch (e: Exception) {
            }
        }
    }

    private fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
        binding.swRandom.onClick { switchRandomClick() }
        binding.swReset.onClick { switchResetClick() }
        binding.swResetStarting.onClick { switchResetStartingClick() }
        binding.txtRange.onClick { rangeClick() }
    }

    private fun resetClick() {
        if (!isResetRunning) {
            isResetRunning = true
            abacusBinding?.ivReset?.y = 0f
            abacusBinding?.ivReset?.animate()?.setDuration(200)
                ?.translationYBy((abacusBinding?.ivReset?.height!! / 2).toFloat())?.withEndAction {
                    abacusBinding?.ivReset?.animate()?.setDuration(200)
                        ?.translationYBy((-abacusBinding?.ivReset?.height!! / 2).toFloat())!!.withEndAction {
                            isResetRunning = false
                        }.start()
                }?.start()
            onAbacusValueDotReset()
        }
    }

    private fun switchResetClick() {
        if (prefManager.getCustomParam(AppConstants.Settings.SW_Reset,"") == "Y") {
            prefManager.setCustomParam(AppConstants.Settings.SW_Reset, "N")
            binding.swReset.isChecked = false
        } else {
            prefManager.setCustomParam(AppConstants.Settings.SW_Reset, "Y")
            binding.swReset.isChecked = true
        }
    }

    private fun switchResetStartingClick() {
        prefManager.setCustomParamInt(AppConstants.Settings.Toddler_No,
            prefManager.getCustomParamInt(AppConstants.Settings.SW_Range_min,1))
        setSwitchs(true)
    }

    private fun rangeClick() {
        ToddlerRangeDialog.showPopup(requireActivity(),
            prefManager.getCustomParamInt(AppConstants.Settings.SW_Range_min,1).toString(),
            (prefManager.getCustomParamInt(AppConstants.Settings.SW_Range_max,101)-1).toString(),this)
    }

    private fun switchRandomClick() {
        with(prefManager){
            if (isPurchased) {
                if (getCustomParam(AppConstants.Settings.SW_Random,"") == "Y") {
                    setCustomParam(AppConstants.Settings.SW_Random,"N")
                } else {
                    setCustomParam(AppConstants.Settings.SW_Random,"Y")
                }
                setSwitchs(true)
            } else {
                setCustomParam(AppConstants.Settings.SW_Random,"N")
                setSwitchs(false)
                notPurchaseDialog()
            }
        }

    }
    // not purchased
    private fun notPurchaseDialog() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.txt_purchase_alert),getString(R.string.txt_page_full_abacus_not_purchased)
            ,getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_not_purchased,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    goToPurchase()
                }
                override fun onConfirmationNoClick(bundle: Bundle?) = Unit
            })
    }

    private fun goToPurchase() {
        mNavController.navigate(R.id.action_fullAbacusFragment_to_purchaseFragment)
    }

    private fun setAbacus() {
        with(prefManager){
            isPurchased = (getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"") == "Y")
            if (isPurchased){
                setCustomParam(AppConstants.Settings.TheamTempView,getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default))
            }else{
                if (getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default).contains(AppConstants.Settings.theam_Default,true)){
                    setCustomParam(AppConstants.Settings.TheamTempView,getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default))
                }else{
                    setCustomParam(AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default)
                }
            }
            theme = getCustomParam(AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default)

        }

        binding.linearAbacus.removeAllViews()
        abacusBinding = FragmentAbacusSubKidBinding.inflate(layoutInflater, null, false)
        binding.linearAbacus.addView(abacusBinding?.root)
        if (DataProvider.generateIndex() == 0){
            abacusBinding?.imgKidLeft?.setImageResource(R.drawable.ic_boy_abacus_left)
            abacusBinding?.imgKidHandLeft?.setImageResource(R.drawable.ic_boy_abacus_hand_left)
        }else{
            abacusBinding?.imgKidLeft?.setImageResource(R.drawable.ic_girl_abacus_left)
            abacusBinding?.imgKidHandLeft?.setImageResource(R.drawable.ic_girl_abacus_hand_left)
        }
        abacusBinding?.imgKidLeft?.show()
        abacusBinding?.imgKidHandLeft?.show()

        abacusBinding?.ivReset?.onClick { resetClick()}

        val themeContent = DataProvider.findAbacusThemeType(requireContext(),theme,AbacusBeadType.None)
        abacusBinding?.rlAbacusMain?.setBackgroundResource(themeContent.abacusFrame135)
        abacusBinding?.ivDivider?.setBackgroundColor(ContextCompat.getColor(requireContext(),themeContent.dividerColor1))
        themeContent.resetBtnColor8.let {
            abacusBinding?.ivReset?.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
            abacusBinding?.ivRight?.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
            abacusBinding?.ivLeft?.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        abacusBinding?.abacusTop?.setNoOfRowAndBeads(0, 9, 1)
        abacusBinding?.abacusBottom?.setNoOfRowAndBeads(0, 9, 4)

        abacusBinding?.abacusTop?.onBeadShiftListener = this@FullAbacusFragment
        abacusBinding?.abacusBottom?.onBeadShiftListener = this@FullAbacusFragment
    }
    private fun setSwitchs(isSpeak: Boolean) {
        with(prefManager){
            binding.swRandom.isChecked = getCustomParam(AppConstants.Settings.SW_Random, "") == "Y"
            binding.swReset.isChecked = getCustomParam(AppConstants.Settings.SW_Reset, "") == "Y"
            random_min = getCustomParamInt(AppConstants.Settings.SW_Range_min,1)
            random_max = getCustomParamInt(AppConstants.Settings.SW_Range_max,101)
            binding.txtRange.text = requireContext().resources.getString(R.string.txt_From) + " " + random_min + " " + requireContext().resources.getString(
                R.string.txt_To
            ) + " " + (random_max - 1)
            if (getCustomParam(AppConstants.Settings.SW_Random, "") != "Y") {
                values = getCustomParamInt(AppConstants.Settings.Toddler_No, random_min)
            } else if (getCustomParam(AppConstants.Settings.SW_Random,"") == "Y") {
                values = genrateRandom()
            }
            total_count = getCustomParamInt(AppConstants.Settings.Toddler_No_Count,1)
            binding.txtAbacus.text = requireContext().resources.getString(R.string.set_only) + " " + values

            lifecycleScope.launch {
                delay(300)
                is1stTime = true
                if (requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
                    goToNextValue()
                }else{
                    notOfflineSupportDialog()
                }
            }
        }
    }

    private fun genrateRandom(): Int {
        val r = Random()
        val i1 = r.nextInt(random_max - random_min) + random_min
        return i1
    }

    // ToddlerRangeDialog click listener
    override fun onSubmitClickToddlerRange(fromValue: String, toValue: String) {
        with(prefManager){
            setCustomParamInt(AppConstants.Settings.SW_Range_min,fromValue.toInt())
            setCustomParamInt(AppConstants.Settings.SW_Range_max,toValue.toInt() + 1)
            setCustomParamInt(AppConstants.Settings.Toddler_No,getCustomParamInt(AppConstants.Settings.SW_Range_min,1))
            setSwitchs(true)
        }
    }

    // abacus Bead Shift Listener
    override fun onBeadShift(abacusView: AbacusMasterView, rowValue: IntArray) {
        val singleBeadWeight = abacusView.singleBeadValue
        var accumulator = 0

        when (abacusView.id) {
            R.id.abacusTop -> if (abacusBinding?.abacusBottom?.engine != null) {
                val bottomVal = abacusBinding?.abacusBottom?.engine!!.getValue()
                var i = 0
                while (i < rowValue.size) {
                    accumulator *= 10
                    val rval = rowValue[i]
                    if (rval > -1) accumulator += rval * singleBeadWeight
                    i++
                }
                val intSumVal = bottomVal + accumulator
                val strCurVal = intSumVal.toString()
                currentSumVal = java.lang.Float.valueOf(strCurVal)
                abacusBinding?.tvCurrentVal?.text = strCurVal
                onAbacusValueChange(abacusView, currentSumVal)
            }
            R.id.abacusBottom -> if (abacusBinding?.abacusTop?.engine != null) {
                val topVal = abacusBinding?.abacusTop?.engine!!.getValue()
                var i = 0
                while (i < rowValue.size) {
                    accumulator *= 10
                    val rval = rowValue[i]
                    if (rval > -1) accumulator += rval * singleBeadWeight
                    i++
                }
                val intSumVal = topVal + accumulator
                val strCurVal = intSumVal.toString()
                currentSumVal = strCurVal.toFloat()
                abacusBinding?.tvCurrentVal?.text = strCurVal
                onAbacusValueChange(abacusView, currentSumVal)
            }
        }
    }
    private fun notOfflineSupportDialog() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.no_internet_working),getString(R.string.for_offline_support_msg)
            ,getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_sad_emoji,isCancelable = false,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    goToPurchase()
                }
                override fun onConfirmationNoClick(bundle: Bundle?){
                    if (requireContext().isNetworkAvailable){
                        goToNextValue()
                    } else{
                        mNavController.navigateUp()
                    }
                }
            })
    }
    override fun onAbacusValueChange(abacusView: View, sum: Float) {
        with(prefManager){
            if (sum.toInt() == values) {
                binding.swResetStarting.isChecked = false
                if (getCustomParam(AppConstants.Settings.SW_Random,"") != "Y") {
                    values++
                    if (values > 9999999) {
                        values = getCustomParamInt(AppConstants.Settings.SW_Range_min,1)
                    }
                    setCustomParamInt(AppConstants.Settings.Toddler_No,values)
                } else if (getCustomParam(AppConstants.Settings.SW_Random,"") == "Y") {
                    random_min = getCustomParamInt(AppConstants.Settings.SW_Range_min,1)
                    random_max = getCustomParamInt(AppConstants.Settings.SW_Range_max,101)
                    binding.txtRange.text = requireContext().resources.getString(R.string.txt_From) + " " + random_min + " " + requireContext().resources.getString(
                        R.string.txt_To
                    ) + " " + random_max
                    values = genrateRandom()
                }
                total_count = getCustomParamInt(AppConstants.Settings.Toddler_No_Count,1)
                total_count++
                if (total_count > 99999999) {
                    total_count = 1
                }
                setCustomParamInt(AppConstants.Settings.Toddler_No_Count,total_count)
                lifecycleScope.launch {
                    delay(300)
                    if (getCustomParam(AppConstants.Settings.SW_Reset,"") == "Y") {
                        resetAbacus()
                    }
                    is1stTime = false
                    if (requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
                        goToNextValue()
                    }else{
                        notOfflineSupportDialog()
                    }
//                    speakOut(requireContext().resources.getString(R.string.speech_set) + " " + values)
                }
                binding.txtAbacus.text = requireContext().resources.getString(R.string.set_only) + " " + values
//                ads()
            }
        }

    }

    private fun goToNextValue() {
        speakOut(requireContext().resources.getString(R.string.speech_set) + " " + values)
        if (!is1stTime){
            ads()
        }
    }

    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" &&
            prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" &&
            !isPurchased && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y") { // if not purchased
            showAMFullScreenAds(getString(R.string.interstitial_ad_unit_id_abacus_full_screen))
        }
    }

    override fun onAbacusValueSubmit(sum: Float) {

    }

    override fun onAbacusValueDotReset() {
        resetAbacus()
    }

    private fun resetAbacus() {
        abacusBinding?.abacusTop?.reset()
        abacusBinding?.abacusBottom?.reset()
    }

}