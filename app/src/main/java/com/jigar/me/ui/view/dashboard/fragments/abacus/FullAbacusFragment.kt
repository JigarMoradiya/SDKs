package com.jigar.me.ui.view.dashboard.fragments.abacus

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.jigar.me.R
import com.jigar.me.databinding.FragmentFullAbacusBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.AbacusMasterBeadShiftListener
import com.jigar.me.ui.view.base.abacus.AbacusMasterView
import com.jigar.me.ui.view.base.abacus.OnAbacusValueChangeListener
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.confirm_alerts.dialogs.ToddlerRangeDialog
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.ViewUtils
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
    private var values: Int = 1
    private var random_min: Int = 0
    private var random_max: Int = 0
    private var total_count: Int = 1

    private var currentSumVal = 0f
    private var isResetRunning = false
    private var isPurchased = false
    private var resetX = 0f
    private var theam = AppConstants.Settings.theam_Egg

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentFullAbacusBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
    }

    private fun initViews() {
        setAbacus()
        setSwitchs(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                binding.txtResetEveryTime.setPadding(0,16.dp,0,0)
                binding.txtRangeLable?.setPadding(0,16.dp,0,0)
                binding.txtRandom?.setPadding(0,16.dp,0,0)
                binding.txtStartWith1.setPadding(0,16.dp,0,0)
            } catch (e: Exception) {
            }
        }
    }

    private fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
        binding.ivReset.onClick { resetClick()}
        binding.swRandom.onClick { switchRandomClick() }
        binding.swReset.onClick { switchResetClick() }
        binding.swResetStarting.onClick { switchResetStartingClick() }
        binding.txtRange.onClick { rangeClick() }
    }

    private fun resetClick() {
        if (!isResetRunning) {
            isResetRunning = true
            binding.ivReset.y = 0f
            binding.ivReset.animate().setDuration(200)
                .translationYBy((binding.ivReset.height / 2).toFloat()).withEndAction {
                    binding.ivReset.animate().setDuration(200)
                        .translationYBy((-binding.ivReset.height / 2).toFloat()).withEndAction {
                            isResetRunning = false
                        }.start()
                }.start()
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
    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" &&
            prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" &&
            !isPurchased && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y") { // if not purchased
            showAMFullScreenAds(getString(R.string.interstitial_ad_unit_id_abacus_full_screen))
        }
    }
    private fun setAbacus() {
        with(prefManager){
            isPurchased = (getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"") == "Y")
            if (isPurchased){
                setCustomParam(AppConstants.Settings.TheamTempView,getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default))
            }else{
                ads()
                setCustomParam(AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default)
            }
            theam = getCustomParam(AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default)

        }
        if (theam == AppConstants.Settings.theam_shape) {
            binding.ivDivider.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black2))
        } else {
            binding.ivDivider.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorAccent_light))
        }

        if (theam.equals(AppConstants.Settings.theam_eyes, ignoreCase = true)) {
            val colSpacing: Int = ViewUtils.convertDpToPixel(Constants.Col_Space_full_eyes,requireActivity())
            binding.abacusTop.setNoOfRowAndBeads(0, 9, 1, colSpacing)
            binding.abacusBottom.setNoOfRowAndBeads(0, 9, 4, colSpacing)
        }else if (theam.equals(AppConstants.Settings.theam_Default, ignoreCase = true)) {
            val colSpacing: Int = ViewUtils.convertDpToPixel(Constants.Col_Space_full_polygon,requireActivity())
            binding.abacusTop.setNoOfRowAndBeads(0, 9, 1, colSpacing)
            binding.abacusBottom.setNoOfRowAndBeads(0, 9, 4, colSpacing)
        } else {
            val colSpacing: Int = ViewUtils.convertDpToPixel(Constants.Col_Space_full_default,requireActivity())
            binding.abacusTop.setNoOfRowAndBeads(0, 9, 1, colSpacing)
            binding.abacusBottom.setNoOfRowAndBeads(0, 9, 4, colSpacing)
        }
        binding.abacusTop.onBeadShiftListener = this
        binding.abacusBottom.onBeadShiftListener = this
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
                delay(500)
                binding.relAbacus.show()
                if (isSpeak) {
                    speakOut(requireContext().resources.getString(R.string.speech_set) + " " + values)
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
            R.id.abacusTop -> if (binding.abacusBottom.engine != null) {
                val bottomVal = binding.abacusBottom.engine!!.getValue()
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
                binding.tvCurrentVal.text = strCurVal
                onAbacusValueChange(abacusView, currentSumVal)
            }
            R.id.abacusBottom -> if (binding.abacusTop.engine != null) {
                val topVal = binding.abacusTop.engine!!.getValue()
                var i = 0
                while (i < rowValue.size) {
                    accumulator *= 10
                    val rval = rowValue[i]
                    if (rval > -1) accumulator += rval * singleBeadWeight
                    i++
                }
                val intSumVal = topVal + accumulator
                val strCurVal = intSumVal.toString()
                currentSumVal = java.lang.Float.valueOf(strCurVal)
                binding.tvCurrentVal.text = strCurVal
                onAbacusValueChange(abacusView, currentSumVal)
            }
        }
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
                    delay(500)
                    if (getCustomParam(AppConstants.Settings.SW_Reset,"") == "Y") {
                        resetAbacus()
                    }
                    speakOut(requireContext().resources.getString(R.string.speech_set) + " " + values)
                }
                binding.txtAbacus.text = requireContext().resources.getString(R.string.set_only) + " " + values
            }
        }

    }

    override fun onAbacusValueSubmit(sum: Float) {

    }

    override fun onAbacusValueDotReset() {
        resetAbacus()
    }

    private fun resetAbacus() {
        binding.abacusTop.reset()
        binding.abacusBottom.reset()
    }

}