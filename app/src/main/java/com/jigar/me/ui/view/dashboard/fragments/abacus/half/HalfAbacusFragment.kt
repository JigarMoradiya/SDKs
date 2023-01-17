package com.jigar.me.ui.view.dashboard.fragments.abacus.half

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.R
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.model.PojoAbacus
import com.jigar.me.data.model.pages.AdditionSubtractionCategory
import com.jigar.me.databinding.DialogAlertBinding
import com.jigar.me.databinding.FragmentHalfAbacusBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.AbacusMasterCompleteListener
import com.jigar.me.ui.view.base.abacus.OnAbacusValueChangeListener
import com.jigar.me.ui.view.confirm_alerts.dialogs.abacus.CompleteResetAlertDialog
import com.jigar.me.ui.view.confirm_alerts.dialogs.abacus.ResetProgressAlertDialog
import com.jigar.me.ui.view.confirm_alerts.dialogs.abacus.ResetPurchaseAlertDialog
import com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter.AbacusAdditionSubtractionTypeAdapter
import com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter.AbacusDivisionTypeAdapter
import com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter.AbacusMultiplicationTypeAdapter
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.utils.*
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@AndroidEntryPoint
class HalfAbacusFragment : BaseFragment(),
    OnAbacusValueChangeListener, CompleteResetAlertDialog.CompleteResetAlertDialogInterface,
    AbacusAdditionSubtractionTypeAdapter.HintListener,
    ResetProgressAlertDialog.ResetProgressAlertDialogInterface,
    ResetPurchaseAlertDialog.ResetPurchaseAlertDialogInterface {
    private lateinit var binding: FragmentHalfAbacusBinding
    private val apiViewModel by viewModels<AppViewModel>()

    private var abacusType = ""
    private var pageId = ""
    private var Que2_str = "" // required only for Multiplication and Division
    private var Que2_type = "" //required only for Multiplication and Division
    private var Que1_digit_type = 0 // required only for Multiplication
    private var total = 0 // required only for addition subtraction
    private var From = 0 // required only for number
    private var To = 0 // required only for number
    private var isRandom = false // required only for number
    private var number = 0 // required only for number
    private var abacus_number = 0 // required only for number
    private var current_pos = 0

    // Settings Constants
    private var isPurchased = false
    private var isDisplayHelpMessage = true
    private var isStepByStep = false
    private var isAutoRefresh = false
    private var isHideTable = false
    private var isHintSound = false

    private var speek_hint = ""

    private var abacus_type = 0 // 0 = sum-sub-single  1 = multiplication 2 = divide
    private var final_column = 0
    private var noOfDecimalPlace = 0

    private var adapterAdditionSubtraction: AbacusAdditionSubtractionTypeAdapter =
        AbacusAdditionSubtractionTypeAdapter(
            arrayListOf(), this, true
        )
    private var adapterMultiplication: AbacusMultiplicationTypeAdapter =
        AbacusMultiplicationTypeAdapter(
            arrayListOf(), true
        )
    private var adapterDivision: AbacusDivisionTypeAdapter = AbacusDivisionTypeAdapter(
        arrayListOf(), true
    )

    private var list_abacus: List<PojoAbacus> = arrayListOf()
    private var list_abacus_main = ArrayList<HashMap<String, String>>()
    private var alertdialog: AlertDialog? = null

    // abacus move
    private var isMoveNext: Boolean = false
    private var shouldResetAbacus = false

    // for division
    private var postfixZero = ""

    private var abacusFragment: HalfAbacusSubFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        abacusType = requireArguments().getString(AppConstants.Extras_Comman.AbacusType, "")
        pageId = requireArguments().getString(AppConstants.apiParams.pageId, "")
        when (abacusType) {
            AppConstants.Extras_Comman.AbacusTypeNumber -> {
                From = requireArguments().getInt(AppConstants.Extras_Comman.From, 0)
                To = requireArguments().getInt(AppConstants.Extras_Comman.To, 0)
                isRandom = requireArguments().getBoolean(
                    AppConstants.Extras_Comman.isType_random,
                    false
                )
            }
            AppConstants.Extras_Comman.AbacusTypeAdditionSubtraction -> {
                total = requireArguments().getInt(AppConstants.apiParams.total, 0)
            }
            else -> { // for multiplication and division
                Que2_str = requireArguments().getString(AppConstants.Extras_Comman.Que2_str, "")
                Que2_type = requireArguments().getString(AppConstants.Extras_Comman.Que2_type, "")
                if (abacusType == AppConstants.Extras_Comman.AbacusTypeMultiplication) {
                    Que1_digit_type =
                        requireArguments().getInt(AppConstants.Extras_Comman.Que1_digit_type, 0)
                }
            }
        }
        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentHalfAbacusBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        ads()
        return binding.root
    }

    private fun initViews() {
        if (prefManager.getCustomParamBoolean(AppConstants.Settings.Setting_left_hand, true)){
            setLeftAbacusRules()
        }else{
            setRightAbacusRules()
        }

        isDisplayHelpMessage = prefManager.getCustomParamBoolean(AppConstants.Settings.Setting_display_help_message, true)
        isHintSound = prefManager.getCustomParamBoolean(AppConstants.Settings.Setting__hint_sound, false)
        isHideTable = prefManager.getCustomParamBoolean(AppConstants.Settings.Setting_hide_table, false)
        isStepByStep = prefManager.getCustomParam(AppConstants.Settings.Setting_answer,AppConstants.Settings.Setting_answer_Step) == AppConstants.Settings.Setting_answer_Step
        isAutoRefresh = if (isStepByStep){
            prefManager.getCustomParamBoolean(AppConstants.Settings.Setting_auto_reset_abacus, false)
        }else{
            false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.txtTitle.setPadding(0,16.dp,0,0)
        }
        startAbacus()
    }

    private fun initListener() {
        binding.cardBack.onClick { goBack() }
        binding.cardResetProgress.onClick { resetProgressClick() }
        binding.cardSettingTop.onClick { mNavController.navigate(R.id.action_halfAbacusFragment_to_settingsFragment) }
        binding.cardYoutube.onClick { requireContext().openYoutube() }
        binding.cardPurchase.onClick { goToPurchase()  }
        binding.cardTable.onClick { tableClick() }
        binding.relativeTable.onClick { binding.relativeTable.hide() }
    }

    private fun tableClick() {
        if (binding.relativeTable.isVisible) {
            binding.relativeTable.hide()
        } else {
            binding.relativeTable.show()
        }
    }

    private fun resetProgressClick() {
        if (isPurchased) {
            ResetProgressAlertDialog.showPopup(requireActivity(), this)
        } else {
            ResetPurchaseAlertDialog.showPopup(requireActivity(), this)
        }
    }
    override fun goToPurchase() {
        mNavController.navigate(R.id.action_halfAbacusFragment_to_purchaseFragment)
    }

    fun onBackClick(){
        if (abacusFragment?.isLighterShow() == true){
            abacusFragment?.dismissLighter()
        }
        binding.flAbacus.removeAllViews()
        binding.relAbacus.hide()
        goBack()
    }

    // dialog listener
    override fun resetProgressConfirm() {
        updateToFirebase(0)
        removeSum()
        current_pos = 0
        startAbacus()
    }

    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y"
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y"
            && !isPurchased && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y") { // if not purchased
            showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_abacus))
            showAMFullScreenAds(getString(R.string.interstitial_ad_unit_id_abacus_half_screen))
        }
    }
    private fun initObserver() {
        apiViewModel.getAbacusOfPagesResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
//                    CustomViews.startButtonLoading(requireActivity())
                }
                is Resource.Success -> {
//                    CustomViews.hideButtonLoading()
                    it.value.content?.also {
                        val list: ArrayList<PojoAbacus> = Gson().fromJson(
                            it.asJsonArray, object : TypeToken<ArrayList<PojoAbacus>>() {}.type)
                        prefManager.setCustomParam(AppConstants.apiParams.pageId + pageId, Gson().toJson(list))
                        setAbacusOfPagesAdditionSubtraction(list)
                    }
                }
                is Resource.Failure -> {
//                    CustomViews.hideButtonLoading()
                    it.errorBody?.let { it1 -> requireContext().toastL(it1) }
                }
                else -> {}
            }
        }
    }
    private fun startAbacus() {
        val currentPosTemp = CommonUtils.getCurrentSumFromPref(requireContext(),pageId)
        if (currentPosTemp!= null){
            current_pos = currentPosTemp
        }
        getCurrentSumFromPref()
        if (requireContext().isNetworkAvailable) {
            if (abacusType == AppConstants.Extras_Comman.AbacusTypeAdditionSubtraction) {
                isPurchased = (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
                        || prefManager.getCustomParam(AppConstants.Purchase.Purchase_Add_Sub_level2,"") == "Y")
                setTempTheme()
                if (prefManager.getCustomParam(AppConstants.apiParams.pageId + pageId, "").isEmpty()) {
                    getListAdditionSubtractionApi()
                } else {
                    val type = object : TypeToken<List<PojoAbacus>>() {}.type
                    val listTemp: List<PojoAbacus> =
                        Gson().fromJson(prefManager.getCustomParam(AppConstants.apiParams.pageId + pageId, ""), type)
                    if (isPurchased) {
                        if (listTemp.size == total) {
                            setAbacusOfPagesAdditionSubtraction(listTemp)
                        } else {
                            getListAdditionSubtractionApi()
                        }
                    } else {
                        if (listTemp.size == AppConstants.Purchase.Purchase_limit_free) {
                            setAbacusOfPagesAdditionSubtraction(listTemp)
                        } else {
                            getListAdditionSubtractionApi()
                        }
                    }
                }
            } else if (abacusType == AppConstants.Extras_Comman.AbacusTypeMultiplication) {
                isPurchased = (prefManager.getCustomParam(
                    AppConstants.Purchase.Purchase_All,"") == "Y"
                        || prefManager.getCustomParam(
                    AppConstants.Purchase.Purchase_Mul_Div_level3,"") == "Y")
                setTempTheme()
                setDataOfMultiplication(true)
            } else if (abacusType == AppConstants.Extras_Comman.AbacusTypeDivision) {
                isPurchased = (prefManager.getCustomParam(
                    AppConstants.Purchase.Purchase_All,"") == "Y"
                        || prefManager.getCustomParam(
                    AppConstants.Purchase.Purchase_Mul_Div_level3,"") == "Y")
                setTempTheme()
                setDataOfDivision(true)
            } else if (abacusType == AppConstants.Extras_Comman.AbacusTypeNumber) {
                isPurchased = (prefManager.getCustomParam(
                    AppConstants.Purchase.Purchase_All,"") == "Y"
                        || prefManager.getCustomParam(
                    AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"") == "Y")
                setTempTheme()
                setDataOfNumber(true)
            } else {
                goBack()
            }
        } else {
            requireContext().toastS(getString(R.string.no_internet))
            goBack()
        }
    }

    override fun goBack() {
        mNavController.navigateUp()
    }

    private fun setTempTheme() {
        if (isPurchased){
            prefManager.setCustomParam(
                AppConstants.Settings.TheamTempView,
                prefManager.getCustomParam(
                    AppConstants.Settings.Theam,
                    AppConstants.Settings.theam_Default
                )
            )
        }else{
            prefManager.setCustomParam(
                AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default
            )
        }
    }

    private fun setDataOfNumber(fromTop: Boolean) {
        if (!isPurchased && current_pos >= AppConstants.Purchase.Purchase_limit_free) {
            if (fromTop) {
                completePage(resources.getString(R.string.txt_page_completed_already), "2")
            } else {
                completePage(resources.getString(R.string.txt_page_completed_free), "2")
            }
        } else {
            binding.txtTitle.text =
                (current_pos + 1).toString() + "/" + resources.getString(R.string.unlimited)
            binding.tvAnsNumber.text = ""
            abacus_type = 0
            number = if (isRandom) {
                DataProvider.genrateSingleDigit(From, To)
            } else {
                prefManager.getCustomParamInt(pageId + "value", From)
            }
            if (number > To) {
                number = From
            }
            lifecycleScope.launch {
                delay(500)
                if (isPurchased && isHintSound) {
                    speakOut(resources.getString(R.string.speech_set) + " " + number)
                }
            }
            val answer = java.lang.Double.valueOf(number.toString() + "")
            var noOfDecimalPlace = 0
            var column = 3

            if (answer == answer.toLong().toDouble()) {
                val ans = answer.toLong().toString() + ""
                column = if (ans.length < 3) 3 else ans.length
                noOfDecimalPlace = 0
            } else {
                val ans = answer.toString()
                noOfDecimalPlace = ans.length - ans.indexOf(".") - 1
                column = ans.length - 1
            }
            binding.tvAnsNumber.text = number.toString()
            binding.relativeQueNumber.visibility = View.VISIBLE
            replaceAbacusFragment(column, noOfDecimalPlace)
        }
    }

    private fun setDataOfDivision(fromTop: Boolean) {
        if (!isPurchased && current_pos >= AppConstants.Purchase.Purchase_limit_free) {
            if (fromTop) {
                completePage(resources.getString(R.string.txt_page_completed_already), "2")
            } else {
                completePage(resources.getString(R.string.txt_page_completed_free), "2")
            }
        } else {
            binding.txtTitle.text =
                (current_pos + 1).toString() + "/" + resources.getString(R.string.unlimited)
            binding.tvAns.text = ""
            abacus_type = 2
            list_abacus_main = DataProvider.genrateDevide(
                Que2_str,
                Que2_type,
                current_pos
            )
            var column = 3
            if (list_abacus_main.size == 2) {
                lifecycleScope.launch {
                    delay(500)
                    if (isPurchased && isHintSound) {
                        speakOut(list_abacus_main[0][Constants.Que].toString() + " divide by " + list_abacus_main[1][Constants.Que])
                    }
                }
                binding.cardAbacusQue.visibility = View.VISIBLE
                adapterDivision = AbacusDivisionTypeAdapter(
                    list_abacus_main,
                    isStepByStep
                )
                binding.recyclerview.adapter = adapterDivision
//                adapter_type2.setData(list_abacus_main, isStepByStep)
                adapterDivision.clearIterationCount()
                val divisor = Integer.valueOf(list_abacus_main[1][Constants.Que]!!)
                val finalAns = adapterDivision.getDivideIterationCount(
                    list_abacus_main[0][Constants.Que]!!,
                    divisor
                )
                for (i in 1 until adapterDivision.getTotalRequiredIteration()) {
                    val data: HashMap<String, String> = HashMap<String, String>()
                    data[Constants.Que] = ""
                    data[Constants.Sign] = ""
                    data[Constants.Hint] = ""
                    list_abacus_main.add(data)
                }
                adapterDivision.setDefaultHighlight()
                binding.recyclerview.layoutManager?.requestLayout()
                adapterDivision.notifyDataSetChanged()
                val topPositions = ArrayList<Int>()
                val bottomPositions = ArrayList<Int>()
                val question = list_abacus_main[0][Constants.Que]
                postfixZero = ""
                /*as first 3 position is occupied for question so do not consider its value. so calculated prefix 0s*/for (i in 0 until question!!.length - 1) {
                    postfixZero += "0"
                }
                val finalAnsLength = finalAns.toString().length
                val queLength = question.length

                /*set question as selected*/
                val totalLength = queLength + finalAnsLength - 1

                // totalLength == 1 ? 2 : totalLength: if question and ans length 1 then add 1 more size for 2 column display.
                for (i in 0 until if (totalLength == 1) 2 else totalLength) {
                    if (i < question.length) {
                        val charAt =
                            question[i] - '1' //convert char to int. minus 1 from question as in abacuse 0 item have 1 value.
                        if (charAt >= 0) {
                            if (charAt >= 4) {
                                topPositions.add(i, 0)
                                bottomPositions.add(i, charAt - 5)
                            } else {
                                topPositions.add(i, -1)
                                bottomPositions.add(i, charAt)
                            }
                        } else {
                            topPositions.add(i, -1)
                            bottomPositions.add(i, -1)
                        }
                    } else {
                        topPositions.add(i, -1)
                        bottomPositions.add(i, -1)
                    }
                }
                val subTop: MutableList<Int> = ArrayList()
                subTop.addAll(topPositions.subList(0, question.length))
                val subBottom: MutableList<Int> = ArrayList()
                subBottom.addAll(bottomPositions.subList(0, question.length))
                for (i in question.indices) {
                    topPositions.removeAt(0)
                    bottomPositions.removeAt(0)
                }
                topPositions.addAll(subTop)
                bottomPositions.addAll(subBottom)
                val noOfRow = queLength + finalAnsLength - 1
                column = if (noOfRow == 1) 2 else noOfRow
                //            column = 7;
                replaceAbacusFragment(column, 0) // divide doesn't have decimal places
                //set table
                setTableDataAndVisiblilty()
                abacusFragment?.setQuestionAndDividerLength(queLength, finalAnsLength)

                abacusFragment?.setSelectedPositions(
                    topPositions,
                    bottomPositions,
                    object : AbacusMasterCompleteListener() {
                        @Synchronized
                        override fun onSetPositionComplete() {
                            noOfTimeCompleted++
                            if (shouldResetAbacus && noOfTimeCompleted == 2) {
                                /*both abacus reset*/
                                abacusFragment?.resetAbacus()
                            }
                        }
                    })
            } else {
                setDataOfDivision(fromTop)
            }
        }
    }

    private fun setDataOfMultiplication(fromTop: Boolean) {
        if (!isPurchased && current_pos >= AppConstants.Purchase.Purchase_limit_free) {
            if (fromTop) {
                completePage(resources.getString(R.string.txt_page_completed_already), "2")
            } else {
                completePage(resources.getString(R.string.txt_page_completed_free), "2")
            }
        } else {
            binding.txtTitle.text =
                (current_pos + 1).toString() + "/" + resources.getString(R.string.unlimited)
            binding.tvAns.text = ""
            list_abacus_main = DataProvider.genrateMultiplication(
                Que2_str,
                Que2_type,
                Que1_digit_type
            )
            if (list_abacus_main.size == 2) {
                abacus_type = 1
                lifecycleScope.launch {
                    delay(500)
                    if (isPurchased && isHintSound) {
                        speakOut(list_abacus_main[0][Constants.Que].toString() + " multiply by " + list_abacus_main[1][Constants.Que])
                    }
                }
                val answer = java.lang.Double.valueOf(list_abacus_main[0][Constants.Que]) * java.lang.Double.valueOf(list_abacus_main[1][Constants.Que])
                var noOfDecimalPlace = 0
                var column = 3
                if (answer == answer.toLong().toDouble()) {
                    val ans = answer.toLong().toString() + ""
                    column = if (ans.length == 1) 3 else ans.length
                    noOfDecimalPlace = 0
                } else {
                    val ans = answer.toString()
                    noOfDecimalPlace = ans.length - ans.indexOf(".") - 1
                    column = ans.length - 1
                }
                binding.cardAbacusQue.visibility = View.VISIBLE
                binding.recyclerview.adapter = adapterMultiplication
                adapterMultiplication.setData(list_abacus_main, isStepByStep)
                //set table
                setTableDataAndVisiblilty()
                replaceAbacusFragment(column, noOfDecimalPlace)
            } else {
                setDataOfMultiplication(fromTop)
            }
        }
    }

    private fun getListAdditionSubtractionApi() {
        if (isPurchased) {
            apiViewModel.getAbacusOfPages(pageId, AppConstants.Purchase.Purchase_limit)
        } else {
            apiViewModel.getAbacusOfPages(pageId, AppConstants.Purchase.Purchase_limit_free)
        }
    }

    // api response
    private fun setAbacusOfPagesAdditionSubtraction(dataList: List<PojoAbacus>) {
        list_abacus = dataList
        setDataOfAdditionSubtraction()
    }

    private fun setDataOfAdditionSubtraction() {
        if (current_pos >= list_abacus.size) {
            if (isPurchased) {
                CompleteResetAlertDialog.showPopup(requireActivity(), this)
            } else {
                completePage(resources.getString(R.string.txt_page_completed_already), "2")
            }
        } else {
            binding.txtTitle.text = "${(current_pos + 1)}/$total"
            binding.tvAns.text = ""

            val datatemp: PojoAbacus = list_abacus[current_pos]

            val list_abacus_main_temp = ArrayList<HashMap<String, String>>()
            var new_column = 0

            var data: HashMap<String, String>
            data = HashMap()
            data[Constants.Que] = datatemp.que0
            data[Constants.Sign] = ""
            data[Constants.Hint] = ""
            list_abacus_main_temp.add(data)

            if (isPurchased && isHintSound) {
                val finalDatatemp: PojoAbacus = datatemp
                lifecycleScope.launch {
                    delay(700)
                    speakOut(resources.getString(R.string.speech_set) + " " + finalDatatemp.que0)
                }
            }
            if (new_column < datatemp.que0.length) {
                new_column = datatemp.que0.length
            }

            if (list_abacus[current_pos].sign1.isNotEmpty()) {
                data = HashMap()
                data[Constants.Que] = datatemp.que1
                data[Constants.Hint] = datatemp.hint1
                data[Constants.Sign] = datatemp.sign1
                list_abacus_main_temp.add(data)
                if (new_column < datatemp.que1.length) {
                    new_column = datatemp.que1.length
                }
                if (datatemp.sign1 == "-"
                    || datatemp.sign1 == "+"
                ) {
                    abacus_type = 0
                    if (list_abacus[current_pos].sign2.isNotEmpty()) {
                        data = HashMap()
                        data[Constants.Que] = datatemp.que2
                        data[Constants.Hint] = datatemp.hint2
                        data[Constants.Sign] = datatemp.sign2
                        list_abacus_main_temp.add(data)
                        if (new_column < datatemp.que2.length) {
                            new_column = datatemp.que2.length
                        }
                        if (list_abacus[current_pos].sign3.isNotEmpty()) {
                            data = HashMap()
                            data[Constants.Que] = datatemp.que3
                            data[Constants.Hint] = datatemp.hint3
                            data[Constants.Sign] = datatemp.sign3
                            list_abacus_main_temp.add(data)
                            if (new_column < datatemp.que3.length) {
                                new_column = datatemp.que3.length
                            }
                            if (list_abacus[current_pos].sign4.isNotEmpty()) {
                                data = HashMap()
                                data[Constants.Que] = datatemp.que4
                                data[Constants.Hint] = datatemp.hint4
                                data[Constants.Sign] = datatemp.sign4
                                list_abacus_main_temp.add(data)
                                if (new_column < datatemp.que4.length) {
                                    new_column = datatemp.que4.length
                                }
                                if (list_abacus[current_pos].sign5.isNotEmpty()) {
                                    data = HashMap()
                                    data[Constants.Que] = datatemp.que5
                                    data[Constants.Hint] = datatemp.hint5
                                    data[Constants.Sign] = datatemp.sign5
                                    list_abacus_main_temp.add(data)
                                    if (new_column < datatemp.que5.length) {
                                        new_column = datatemp.que5.length
                                    }
                                }
                            }
                        }
                    }
                } else if (datatemp.sign1 == "*") {
                    abacus_type = 1
                } else if (datatemp.sign1 == "/") {
                    abacus_type = 2
                }
            } else {
                abacus_type = 0
            }

            val answer = java.lang.Double.valueOf(datatemp.ans)
            var noOfDecimalPlace = 0
            var column = 3
            if (abacus_type == 0) {
                binding.recyclerview.adapter = adapterAdditionSubtraction
                if (answer == answer.toLong().toDouble()) {
                    val ans = answer.toLong().toString() + ""
                    column = if (ans.length == 1) 3 else ans.length
                    noOfDecimalPlace = 0
                } else {
                    val ans = answer.toFloat().toString()
                    noOfDecimalPlace = ans.length - ans.indexOf(".") - 1
                    column = ans.length
                }
            } else if (abacus_type == 1) {
                binding.recyclerview.adapter = adapterMultiplication
                if (answer == answer.toLong().toDouble()) {
                    val ans = answer.toLong().toString() + ""
                    column = if (ans.length == 1) 3 else ans.length
                    noOfDecimalPlace = 0
                } else {
                    val ans = datatemp.ans
                    noOfDecimalPlace = ans.length - ans.indexOf(".") - 1
                    column = ans.length - 1
                }
            } else if (abacus_type == 2) {
                binding.recyclerview.adapter = adapterDivision
            }
            list_abacus_main.clear()
            list_abacus_main.addAll(list_abacus_main_temp)
            binding.cardAbacusQue.visibility = View.VISIBLE
            when (abacus_type) {
                0 -> {
                    adapterAdditionSubtraction.setData(list_abacus_main, isStepByStep)
                    if (column > new_column) {
                        new_column = column
                    }
                    if (new_column <= 2) {
                        new_column = 3
                    }
                    replaceAbacusFragment(new_column, noOfDecimalPlace)
                }
                1 -> {
                    adapterMultiplication.setData(list_abacus_main, isStepByStep)
                    replaceAbacusFragment(column, noOfDecimalPlace)

                    //set table
                    setTableDataAndVisiblilty()
                }
                2 -> {
                    adapterDivision.setData(list_abacus_main, isStepByStep)
                    adapterDivision.clearIterationCount()
                    val divisor = Integer.valueOf(list_abacus_main[1][Constants.Que])
                    val finalAns = adapterDivision.getDivideIterationCount(
                        list_abacus_main[0][Constants.Que]!!,
                        divisor
                    )

                    for (i in 1 until adapterDivision.getTotalRequiredIteration()) {
                        data = HashMap()
                        data[Constants.Que] = ""
                        data[Constants.Sign] = ""
                        data[Constants.Hint] = ""
                        list_abacus_main.add(data)
                    }

                    adapterDivision.setDefaultHighlight()
                    binding.recyclerview.layoutManager?.requestLayout()
                    adapterDivision.notifyDataSetChanged()

                    val topPositions = ArrayList<Int>()
                    val bottomPositions = ArrayList<Int>()

                    val question = list_abacus_main[0][Constants.Que]
                    postfixZero = ""
                    /*as first 3 position is occupied for question so do not consider its value. so calculated prefix 0s*/for (i in 0 until question!!.length - 1) {
                        postfixZero += "0"
                    }

                    val finalAnsLength = finalAns.toString().length
                    val queLength = question.length

                    /*set question as selected*/

                    /*set question as selected*/
                    val totalLength = queLength + finalAnsLength - 1

                    // totalLength == 1 ? 2 : totalLength: if question and ans length 1 then add 1 more size for 2 column display.
                    for (i in 0 until if (totalLength == 1) 2 else totalLength) {
                        if (i < question.length) {
                            val charAt =
                                question[i] - '1' //convert char to int. minus 1 from question as in abacuse 0 item have 1 value.
                            if (charAt >= 0) {
                                if (charAt >= 4) {
                                    topPositions.add(i, 0)
                                    bottomPositions.add(i, charAt - 5)
                                } else {
                                    topPositions.add(i, -1)
                                    bottomPositions.add(i, charAt)
                                }
                            } else {
                                topPositions.add(i, -1)
                                bottomPositions.add(i, -1)
                            }
                        } else {
                            topPositions.add(i, -1)
                            bottomPositions.add(i, -1)
                        }
                    }

                    val subTop: MutableList<Int> = ArrayList()
                    subTop.addAll(topPositions.subList(0, question.length))

                    val subBottom: MutableList<Int> = ArrayList()
                    subBottom.addAll(bottomPositions.subList(0, question.length))

                    for (i in question.indices) {
                        topPositions.removeAt(0)
                        bottomPositions.removeAt(0)
                    }
                    topPositions.addAll(subTop)
                    bottomPositions.addAll(subBottom)

                    val noOfRow = queLength + finalAnsLength - 1
                    column = if (noOfRow == 1) 2 else noOfRow
                    replaceAbacusFragment(column, 0) // divide doesn't have decimal places

                    //set table
                    setTableDataAndVisiblilty()
                    abacusFragment?.setQuestionAndDividerLength(queLength, finalAnsLength)

                    abacusFragment?.setSelectedPositions(
                        topPositions,
                        bottomPositions,
                        object : AbacusMasterCompleteListener() {
                            @Synchronized
                            override fun onSetPositionComplete() {
                                noOfTimeCompleted++
                                if (shouldResetAbacus && noOfTimeCompleted == 2) {
                                    /*both abacus reset*/
                                    abacusFragment?.resetAbacus()
                                }
                            }
                        })
                }
            }
        }
    }

    private fun setTableDataAndVisiblilty() {
        if (list_abacus_main.size >= 2) {
            if (abacus_type == 1) {
                val spannableString = adapterMultiplication.getTable(requireContext())
                if (!TextUtils.isEmpty(spannableString)) {
                    if (final_column > 5) {
                        binding.cardHint.hide()
                        if (isHideTable) {
                            binding.cardTable.hide()
                        } else {
                            binding.cardTable.show()
                        }
                        binding.relativeTable.hide()
                    } else {
                        if (isHideTable) {
                            binding.cardHint.invisible()
                        } else {
                            binding.cardHint.show()
                        }
                        binding.cardTable.hide()
                        binding.relativeTable.hide()
                    }
                    binding.txtHint.text = spannableString
                    binding.txtHintTable.text = spannableString
                }
            } else if (abacus_type == 2) {
                if (final_column > 5) {
                    binding.cardHint.hide()
                    if (isHideTable) {
                        binding.cardTable.hide()
                    } else {
                        binding.cardTable.show()
                    }
                    binding.relativeTable.hide()
                } else {
                    if (isHideTable) {
                        binding.cardHint.invisible()
                    } else {
                        binding.cardHint.show()
                    }
                    binding.cardTable.hide()
                    binding.relativeTable.hide()
                }
                binding.txtHint.text = ViewUtils.getTable(
                    requireContext(), list_abacus_main[1][Constants.Que]!!.toInt(),
                    adapterDivision.currentTablePosition
                )
                binding.txtHintTable.text = ViewUtils.getTable(
                    requireContext(), list_abacus_main[1][Constants.Que]!!
                        .toInt(),
                    adapterDivision.currentTablePosition
                )
            } else {
                binding.cardHint.invisible()
                binding.cardTable.hide()
                binding.relativeTable.hide()
            }
        } else {
            binding.cardHint.invisible()
            binding.cardTable.hide()
            binding.relativeTable.hide()
        }
    }


    override fun onCheckHint(hint: String?, que: String?, Sign: String?) {
        if (isPurchased && isHintSound) {
            if (Sign == "-") {
                speakOut(resources.getString(R.string.speech_set_minus) + " " + que)
            } else {
                speakOut(resources.getString(R.string.speech_set_plus) + " " + que)
            }
        }

        speek_hint = ""
        if (!hint.isNullOrEmpty()) {
            if (isDisplayHelpMessage) {
                binding.cardHint.show()
            } else {
                binding.cardHint.hide()
            }
            binding.cardTable.hide()
            binding.relativeTable.hide()
            binding.txtHint.text = "$Sign$que = $hint"
            val temp_hint = "$Sign$que = $hint"
            speek_hint = temp_hint.replace("-", " minus ").replace("+", " plus ")
                .replace("=", " equal to ")
            binding.txtHint.text = temp_hint
            lifecycleScope.launch {
                delay(1500)
                if (isPurchased && isHintSound) {
                    speakOut(resources.getString(R.string.speech_formula_for) + " " + speek_hint)
                }
            }
        } else {
            binding.cardHint.hide()
            binding.cardTable.hide()
            binding.relativeTable.hide()
        }

    }

    fun getCurrentSumFromPref() {
        try {
            val pageSum: String = prefManager.getCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM, "{}")
            val objJson = JSONObject(pageSum)
            if (objJson.has(pageId)) {
                current_pos = objJson.getInt(pageId)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    private fun saveCurrentSum() {
        try {
            val pageSum: String = prefManager
                .getCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM, "{}")
            val objJson = JSONObject(pageSum)
            objJson.put(pageId, current_pos)
            prefManager.setCustomParam(
                AppConstants.AbacusProgress.PREF_PAGE_SUM,
                objJson.toString()
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun removeSum() {
        try {
            val pageSum: String = prefManager
                .getCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM, "{}")
            val objJson = JSONObject(pageSum)
            if (objJson.has(pageId)) {
                objJson.remove(pageId)
            }
            prefManager.setCustomParam(
                AppConstants.AbacusProgress.PREF_PAGE_SUM,
                objJson.toString()
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun replaceAbacusFragment(column: Int, noOfDecimalPlace: Int) {
        this.final_column = column
        this.noOfDecimalPlace = noOfDecimalPlace

        try {
            abacusFragment = HalfAbacusSubFragment().newInstance(column, noOfDecimalPlace, abacus_type)
            if (abacusFragment != null){
                abacusFragment?.setOnAbacusValueChangeListener(this)
                val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.flAbacus,
                    abacusFragment!!,
                    abacusFragment?.javaClass?.simpleName
                )
                transaction.commit()
            }
            if (binding.relAbacus.visibility == View.GONE){
                lifecycleScope.launch {
                    delay(500)
                    binding.relAbacus.show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAbacusValueChange(abacusView: View, sum: Float) {
        if (isMoveNext) {
            moveToNext()
            isMoveNext = false
            return
        }
        if (abacus_type == 0) {
            if (abacusType == AppConstants.Extras_Comman.AbacusTypeNumber) {
                val finalans = number.toFloat()
                if (sum == finalans) {
                    onAbacusValueSubmit(finalans)
                }
            } else if (adapterAdditionSubtraction.itemCount > 0) {
                if (adapterAdditionSubtraction.getCurrentSumVal() != null) {
                    val sumVal: Float = adapterAdditionSubtraction.getCurrentSumVal()!!.toFloat()
                    if (isStepByStep) {
                        if (sum == sumVal) {
                            adapterAdditionSubtraction.goToNextStep()
                        }
                        if (adapterAdditionSubtraction.getCurrentStep() == list_abacus_main.size) {
                            val finalans =
                                (adapterAdditionSubtraction.getFinalSumVal()
                                    .toString() + "").toFloat()
                            onAbacusValueSubmit(finalans)
                        }
                    } else {
                        val finalans =
                            (adapterAdditionSubtraction.getFinalSumVal().toString() + "").toFloat()
                        if (sum == finalans) {
                            onAbacusValueSubmit(finalans)
                        }
                    }
                }
            }
        } else if (abacus_type == 1) {
            if (adapterMultiplication.getCurrentSumVal() != null) {
                val sumVal: Float = adapterMultiplication.getCurrentSumVal()!!.toFloat()
                if (isStepByStep) {
                    if (sum == sumVal) {
                        adapterMultiplication.goToNextStep()
                        setTableDataAndVisiblilty()
                    }
                    val curVal = adapterMultiplication.getCurrentStep()
                    val finalans =
                        (adapterMultiplication.getFinalSumVal().toString() + "").toFloat()
                    if (sum == finalans && curVal[0]!! >= adapterMultiplication.getItem(0)[Constants.Que]!!
                            .length - 1 && curVal[1]!! >= adapterMultiplication.getItem(1)[Constants.Que]!!.length - 1
                    ) {
                        adapterMultiplication.clearHighlight()
                        onAbacusValueSubmit(finalans)
                    }
                } else {
                    val finalans =
                        (adapterMultiplication.getFinalSumVal().toString() + "").toFloat()
                    if (sum == finalans) {
                        onAbacusValueSubmit(finalans)
                    }
                }
            }
        } else if (abacus_type == 2) {
            if (adapterDivision.getCurrentSumVal() != null) {
                val postfix = adapterDivision.getCurrentSumVal().toString() + postfixZero
                val sumVal = java.lang.Float.valueOf(postfix) + adapterDivision.getNextDivider()
                if (isStepByStep) {
                    if (sum == sumVal) {
                        adapterDivision.goToNextStep()
                        setTableDataAndVisiblilty()
                    }
                    val finalans: Float = java.lang.Float.valueOf(
                        adapterDivision.getFinalSumVal()!!.toInt().toString() + "" + postfixZero
                    )
                    if (sum == finalans && adapterDivision.isLastStep()) {
                        adapterDivision.clearHighlight()
                        onAbacusValueSubmit(finalans)
                    }
                } else {
                    val finalans = (adapterDivision.getFinalSumVal().toString() + "").toFloat()
                    if (sum == finalans) {
                        onAbacusValueSubmit(finalans)
                    }
                }
            }
        }
    }

    private fun moveToNext() {
        updateToFirebase(current_pos)

        if (abacusType == AppConstants.Extras_Comman.AbacusTypeNumber) {
            current_pos++
            saveCurrentSum()
            prefManager.setCustomParamInt(pageId + "value", number + 1)
            setDataOfNumber(false)
        } else if (abacus_type == 0 && current_pos == list_abacus.size - 1) {
            // page complete
            prefManager.setCustomParam(
                AppConstants.AbacusProgress.CompleteAbacusPos + pageId,
                current_pos.toString()
            )
            current_pos++
            saveCurrentSum()
            if (isPurchased) {
                completePage(resources.getString(R.string.txt_page_completed), "1")
            } else {
                completePage(resources.getString(R.string.txt_page_completed_free), "2")
            }
        } else {
            binding.tvAns.text = ""
            binding.cardHint.hide()
            current_pos++
            saveCurrentSum()
            when (abacus_type) {
                0 -> {
                    adapterAdditionSubtraction.reset()
                    setDataOfAdditionSubtraction()
                }
                1 -> {
                    adapterMultiplication.reset()
                    setDataOfMultiplication(false)
                }
                2 -> {
                    shouldResetAbacus = true
                    adapterDivision.reset()
                    setDataOfDivision(false)
                }
            }

        }
    }

    // update progress to Firebase
    private fun updateToFirebase(currentPos: Int) {
        if (requireContext().isNetworkAvailable) {
            // set from value when reset page progress for abacus type number
            if (abacusType == AppConstants.Extras_Comman.AbacusTypeNumber && currentPos == 0){
                prefManager.setCustomParamInt(pageId + "value", From)
            }
            FirebaseDatabase.getInstance().reference.child(AppConstants.AbacusProgress.Track + "/" + prefManager.getDeviceId() + "/" + pageId).child(AppConstants.AbacusProgress.Position).setValue(currentPos)
        }
    }

    override fun onAbacusValueSubmit(sum: Float) {
        when (abacus_type) {
            0 -> {
                val sumVal: Float
                if (abacusType == AppConstants.Extras_Comman.AbacusTypeNumber) {
                    abacus_number = sum.toInt()
                    makeAutoRefresh()
                } else {
                    sumVal = adapterAdditionSubtraction.getFinalSumVal()!!.toFloat()
                    if (sumVal == (sum.toInt().toString()).toFloat()) {
                        binding.tvAns.text = sum.toInt().toString()
                    } else {
                        binding.tvAns.text = sum.toString()
                    }
                    makeAutoRefresh()
                }

            }
            1 -> {
                val sumVal: Float = adapterMultiplication.getFinalSumVal()!!.toFloat()
                if (sumVal == (sum.toInt().toString()).toFloat()) {
                    binding.tvAns.text = sum.toInt().toString()
                } else {
                    binding.tvAns.text = sum.toString()
                }
                makeAutoRefresh()
            }
            2 -> {
                var sumStr: String = sum.toInt().toString()
                if (sumStr.length > postfixZero.length) {
                    sumStr = sumStr.substring(0, sumStr.length - postfixZero.length)
                }
                binding.tvAns.text = sumStr
                makeAutoRefresh()
            }
        }
    }

    private fun makeAutoRefresh() {
        if (isStepByStep && isAutoRefresh){
            abacusFragment?.resetButtonEnable(false)
            lifecycleScope.launch {
                delay(1500)
                abacusFragment?.resetButtonEnable(true)
                onAbacusValueDotReset()
            }
        }else{
            abacusFragment?.resetButtonEnable(true)
            abacusFragment?.showResetToContinue(true)
        }
    }

    override fun onAbacusValueDotReset() {
        resetOrMoveNext()
    }

    private fun resetOrMoveNext() {
        if (abacusType == AppConstants.Extras_Comman.AbacusTypeNumber) {
            if (binding.tvAnsNumber.text.toString() == abacus_number.toString()) {
                isMoveNext = true
            }
        } else {
            if (!TextUtils.isEmpty(binding.tvAns.text.toString())) {
                isMoveNext = true
            }
        }

        reset()
    }

    private fun reset() {
        abacusFragment?.resetAbacus()
        if (abacusType != AppConstants.Extras_Comman.AbacusTypeNumber) {
            binding.tvAns.text = ""
            when (abacus_type) {
                0 -> {
                    adapterAdditionSubtraction.reset()
                }
                1 -> {
                    adapterMultiplication.reset()
                    //set table
                    setTableDataAndVisiblilty()
                }
                2 -> {
                    adapterDivision.reset()
                    //set table
                    setTableDataAndVisiblilty()
                }
            }
        }

    }

    // abacus ui rules
    private fun setRightAbacusRules() {
        val paramsAds = binding.adView.layoutParams as RelativeLayout.LayoutParams
        paramsAds.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        paramsAds.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        binding.adView.layoutParams = paramsAds

        val paramsAbacus = binding.relAbacus.layoutParams as RelativeLayout.LayoutParams
        paramsAbacus.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        paramsAbacus.addRule(RelativeLayout.CENTER_VERTICAL)
        binding.relAbacus.layoutParams = paramsAbacus

        val paramscardTable = binding.cardTable.layoutParams as RelativeLayout.LayoutParams
        paramscardTable.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        paramscardTable.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        binding.cardTable.layoutParams = paramscardTable

        val paramsRelativeTable = binding.relativeTable.layoutParams as RelativeLayout.LayoutParams
        paramsRelativeTable.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        paramsRelativeTable.addRule(RelativeLayout.BELOW, R.id.cardTable)
        binding.relativeTable.layoutParams = paramsRelativeTable

        val paramsQuestionsMain = binding.linearQuestions.layoutParams as RelativeLayout.LayoutParams
        paramsQuestionsMain.addRule(RelativeLayout.CENTER_VERTICAL)
        paramsQuestionsMain.addRule(RelativeLayout.LEFT_OF, R.id.relAbacus)
        binding.linearQuestions.layoutParams = paramsQuestionsMain

        val paramsQueNumber = binding.relativeQueNumber.layoutParams as RelativeLayout.LayoutParams
        paramsQueNumber.addRule(RelativeLayout.ALIGN_PARENT_START)
        paramsQueNumber.addRule(RelativeLayout.CENTER_VERTICAL)
        binding.relativeQueNumber.layoutParams = paramsQueNumber

        val paramsQuestions = binding.cardAbacusQue.layoutParams as RelativeLayout.LayoutParams
        paramsQuestions.addRule(RelativeLayout.ALIGN_PARENT_START)
        paramsQuestions.addRule(RelativeLayout.CENTER_VERTICAL)
        binding.cardAbacusQue.layoutParams = paramsQuestions

        val paramsHint = binding.cardHint.layoutParams as RelativeLayout.LayoutParams
        paramsHint.addRule(RelativeLayout.CENTER_VERTICAL)
        paramsHint.addRule(RelativeLayout.END_OF, R.id.cardAbacusQue)
        binding.cardHint.layoutParams = paramsHint
    }
    private fun setLeftAbacusRules() {
        val paramsAds = binding.adView.layoutParams as RelativeLayout.LayoutParams
        paramsAds.addRule(RelativeLayout.ALIGN_PARENT_START)
        paramsAds.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        binding.adView.layoutParams = paramsAds

        val paramsAbacus = binding.relAbacus.layoutParams as RelativeLayout.LayoutParams
        paramsAbacus.addRule(RelativeLayout.ALIGN_PARENT_START)
        paramsAbacus.addRule(RelativeLayout.CENTER_VERTICAL)
        binding.relAbacus.layoutParams = paramsAbacus

        val paramscardTable = binding.cardTable.layoutParams as RelativeLayout.LayoutParams
        paramscardTable.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        paramscardTable.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        binding.cardTable.layoutParams = paramscardTable

        val paramsRelativeTable = binding.relativeTable.layoutParams as RelativeLayout.LayoutParams
        paramsRelativeTable.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        paramsRelativeTable.addRule(RelativeLayout.BELOW, R.id.cardTable)
        binding.relativeTable.layoutParams = paramsRelativeTable

        val paramsQuestionsMain = binding.linearQuestions.layoutParams as RelativeLayout.LayoutParams
        paramsQuestionsMain.addRule(RelativeLayout.ALIGN_PARENT_END)
        paramsQuestionsMain.addRule(RelativeLayout.CENTER_VERTICAL)
        paramsQuestionsMain.addRule(RelativeLayout.END_OF, R.id.relAbacus)
        binding.linearQuestions.layoutParams = paramsQuestionsMain


        val paramsQueNumber = binding.relativeQueNumber.layoutParams as RelativeLayout.LayoutParams
        paramsQueNumber.addRule(RelativeLayout.ALIGN_PARENT_END)
        paramsQueNumber.addRule(RelativeLayout.CENTER_VERTICAL)
        binding.relativeQueNumber.layoutParams = paramsQueNumber

        val paramsQuestions = binding.cardAbacusQue.layoutParams as RelativeLayout.LayoutParams
        paramsQuestions.addRule(RelativeLayout.ALIGN_PARENT_END)
        paramsQuestions.addRule(RelativeLayout.CENTER_VERTICAL)
        binding.cardAbacusQue.layoutParams = paramsQuestions

        val paramsHint = binding.cardHint.layoutParams as RelativeLayout.LayoutParams
        paramsHint.addRule(RelativeLayout.CENTER_VERTICAL)
        paramsHint.addRule(RelativeLayout.START_OF, R.id.cardAbacusQue)
        binding.cardHint.layoutParams = paramsHint
    }

    fun completePage(str: String, buttonType: String) { // complete page
        val inflater = layoutInflater
        val alertLayout: DialogAlertBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_alert, null, false)
        val alert = AlertDialog.Builder(requireContext())
        alert.setView(alertLayout.root)
        alert.setCancelable(false)
        alertLayout.txtTitle.text = resources.getString(R.string.app_name)
        alertLayout.txtQue.text = str
        if (buttonType == "1") {
            alertLayout.btnNo.hide()
            alertLayout.btnYes.text = resources.getString(R.string.txtOk)
            alertLayout.btnYes.setOnClickListener {
                alertdialog?.dismiss()
            }
        } else {
            alertLayout.btnYes.text = resources.getString(R.string.txt_purchase)

            alertLayout.btnYes.setOnClickListener {
                alertdialog?.dismiss()
                goToPurchase()
            }
        }

        alertLayout.btnNo.setOnClickListener {
            alertdialog?.dismiss()
            goBack()
        }
        alertdialog = alert.create()
        alertdialog?.setCanceledOnTouchOutside(false)
        val windows = alertdialog?.window
        val colorD = ColorDrawable(Color.TRANSPARENT)
        val insetD = InsetDrawable(colorD, 40, 5, 40, 5)
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


}