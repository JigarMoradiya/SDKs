package com.jigar.me.ui.view.dashboard.fragments.abacus.half

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.RelativeLayout
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.R
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.model.PojoAbacus
import com.jigar.me.databinding.FragmentHalfAbacusBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.AbacusMasterCompleteListener
import com.jigar.me.ui.view.base.abacus.OnAbacusValueChangeListener
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter.AbacusAdditionSubtractionTypeAdapter
import com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter.AbacusDivisionTypeAdapter
import com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter.AbacusMultiplicationTypeAdapter
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.utils.*
import com.jigar.me.utils.CommonUtils.getCurrentSumFromPref
import com.jigar.me.utils.CommonUtils.saveCurrentSum
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@AndroidEntryPoint
class HalfAbacusFragment : BaseFragment(), OnAbacusValueChangeListener, AbacusAdditionSubtractionTypeAdapter.HintListener{
    private lateinit var binding: FragmentHalfAbacusBinding
    private var hintPage : String? = null
    private var fileAbacus : String? = null
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
    private lateinit var mNavController: NavController
    private var adapterAdditionSubtraction: AbacusAdditionSubtractionTypeAdapter = AbacusAdditionSubtractionTypeAdapter(arrayListOf(), this, true)
    private var adapterMultiplication: AbacusMultiplicationTypeAdapter = AbacusMultiplicationTypeAdapter(arrayListOf(), true)
    private var adapterDivision: AbacusDivisionTypeAdapter = AbacusDivisionTypeAdapter(arrayListOf(), true)

    private var list_abacus: List<PojoAbacus> = arrayListOf()
    private var list_abacus_main = ArrayList<HashMap<String, String>>()

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
                isRandom = requireArguments().getBoolean(AppConstants.Extras_Comman.isType_random,false)
            }
            AppConstants.Extras_Comman.AbacusTypeAdditionSubtraction -> {
                hintPage = requireArguments().getString(AppConstants.apiParams.hint)
                fileAbacus = requireArguments().getString(AppConstants.apiParams.file)
            }
            else -> { // for multiplication and division
                Que2_str = requireArguments().getString(AppConstants.Extras_Comman.Que2_str, "")
                Que2_type = requireArguments().getString(AppConstants.Extras_Comman.Que2_type, "")
                if (abacusType == AppConstants.Extras_Comman.AbacusTypeMultiplication) {
                    Que1_digit_type = requireArguments().getInt(AppConstants.Extras_Comman.Que1_digit_type, 0)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentHalfAbacusBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initViews()
        initListener()
        ads()
        return binding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
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
        lifecycleScope.launch {
            delay(400)
            bannerAds()
        }
    }
    private fun bannerAds() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y"
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y"
            && !isPurchased && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y") { // if not purchased
            showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_abacus))
        }
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
            paidResetPageProgressDialog()
        } else {
            resetPurchaseDialog()
        }
    }
    private fun goToPurchase() {
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

    private fun startAbacus() {
        if(requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
            startAbacusNow()
        }else{
            notOfflineSupportDialog2()
        }

    }
    private fun notOfflineSupportDialog2() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.no_internet_working),getString(R.string.for_offline_support_msg)
            ,getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_sad_emoji,isCancelable = false,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    binding.cardPurchase.performClick()
                }
                override fun onConfirmationNoClick(bundle: Bundle?){
                    if (requireContext().isNetworkAvailable){
                        startAbacusNow()
                    } else{
                        mNavController.navigateUp()
                    }
                }
            })
    }

    private fun startAbacusNow() {
        val currentPosTemp = prefManager.getCurrentSumFromPref(pageId)
        if (currentPosTemp!= null){
            current_pos = currentPosTemp
        }
        if (abacusType == AppConstants.Extras_Comman.AbacusTypeAdditionSubtraction) {
            isPurchased = (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
                    || prefManager.getCustomParam(AppConstants.Purchase.Purchase_Add_Sub_level2,"") == "Y")
            setTempTheme()
            val abacus = if (!fileAbacus.isNullOrEmpty()){
                requireContext().readJsonAsset(fileAbacus)
            }else{
                requireContext().readJsonAsset("abacus.json")
            }

            val type = object : TypeToken<List<PojoAbacus>>() {}.type
            val temp: List<PojoAbacus> = Gson().fromJson(abacus,type)
            temp.filter { it.id == pageId}.also {
                if (it.isNotNullOrEmpty()){
                    total = it.size
                    val listTemp = if (isPurchased){it}else{it.take(20)}
                    setAbacusOfPagesAdditionSubtraction(listTemp)
                }
            }
        } else if (abacusType == AppConstants.Extras_Comman.AbacusTypeMultiplication) {
            isPurchased = (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
                    || prefManager.getCustomParam(AppConstants.Purchase.Purchase_Mul_Div_level3,"") == "Y")
            setTempTheme()
            setDataOfMultiplication(true)
        } else if (abacusType == AppConstants.Extras_Comman.AbacusTypeDivision) {
            isPurchased = (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
                    || prefManager.getCustomParam(AppConstants.Purchase.Purchase_Mul_Div_level3,"") == "Y")
            setTempTheme()
            setDataOfDivision(true)
        } else if (abacusType == AppConstants.Extras_Comman.AbacusTypeNumber) {
            isPurchased = (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
                    || prefManager.getCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"") == "Y")
            setTempTheme()
            setDataOfNumber(true)
        } else {
            goBack()
        }
    }

    private fun setTempTheme() {
        if (isPurchased){
            prefManager.setCustomParam(
                AppConstants.Settings.TheamTempView,
                prefManager.getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default)
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
                freePageCompleteDialog(getString(R.string.txt_page_completed_already))
            } else {
                freePageCompleteDialog(getString(R.string.txt_page_completed_free))
            }
        } else {
            binding.txtTitle.text =
                (current_pos + 1).toString() + "/" + resources.getString(R.string.unlimited)
            binding.tvAnsNumber.text = ""
            abacus_type = 0
            number = if (isRandom) {
                DataProvider.generateSingleDigit(From, To)
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
            binding.relativeQueNumber.show()
            replaceAbacusFragment(column, noOfDecimalPlace)
        }
    }

    private fun setDataOfDivision(fromTop: Boolean) {
        if (!isPurchased && current_pos >= AppConstants.Purchase.Purchase_limit_free) {
            if (fromTop) {
                freePageCompleteDialog(getString(R.string.txt_page_completed_already))
            } else {
                freePageCompleteDialog(getString(R.string.txt_page_completed_free))
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
                binding.cardAbacusQue.show()
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
                        val charAt = question[i] - '1' //convert char to int. minus 1 from question as in abacuse 0 item have 1 value.
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
                freePageCompleteDialog(getString(R.string.txt_page_completed_already))
            } else {
                freePageCompleteDialog(getString(R.string.txt_page_completed_free))
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
                binding.cardAbacusQue.show()
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

    // api response
    private fun setAbacusOfPagesAdditionSubtraction(dataList: List<PojoAbacus>) {
        list_abacus = dataList
        setDataOfAdditionSubtraction()
    }
    private fun setDataOfAdditionSubtraction() {
        if (current_pos >= list_abacus.size) {
            if (isPurchased) {
                paidPageAlreadyCompletedDialog()
            } else {
                freePageCompleteDialog(getString(R.string.txt_page_completed_already))
            }
        } else {
            binding.txtTitle.text = "${(current_pos + 1)}/$total"
            binding.tvAns.text = ""

            val datatemp: PojoAbacus = list_abacus[current_pos]

            if (!hintPage.isNullOrEmpty()) {
                if (isDisplayHelpMessage) {
                    binding.cardHint.show()
                } else {
                    binding.cardHint.hide()
                }
                binding.cardTable.hide()
                binding.relativeTable.hide()
                binding.txtHint.text = HtmlCompat.fromHtml(hintPage!!,HtmlCompat.FROM_HTML_MODE_LEGACY)
            }


            val list_abacus_main_temp = ArrayList<HashMap<String, String>>()
            var new_column = 0

            var data: HashMap<String, String>
            data = HashMap()
            data[Constants.Que] = datatemp.q0
            data[Constants.Sign] = ""
            data[Constants.Hint] = ""
            list_abacus_main_temp.add(data)

            if (isPurchased && isHintSound) {
                val finalDatatemp: PojoAbacus = datatemp
                lifecycleScope.launch {
                    delay(700)
                    speakOut(resources.getString(R.string.speech_set) + " " + finalDatatemp.q0)
                }
            }
            if (new_column < datatemp.q0.length) {
                new_column = datatemp.q0.length
            }

            if (list_abacus[current_pos].s1.isNotEmpty()) {
                data = HashMap()
                data[Constants.Que] = datatemp.q1
                data[Constants.Hint] = datatemp.h1?:""
                data[Constants.Sign] = datatemp.s1
                list_abacus_main_temp.add(data)
                if (new_column < datatemp.q1.length) {
                    new_column = datatemp.q1.length
                }
                if (datatemp.s1 == "-"
                    || datatemp.s1 == "+"
                ) {
                    abacus_type = 0
                    if (!list_abacus[current_pos].s2.isNullOrEmpty()) {
                        data = HashMap()
                        data[Constants.Que] = datatemp.q2?:""
                        data[Constants.Hint] = datatemp.h2?:""
                        data[Constants.Sign] = datatemp.s2?:""
                        list_abacus_main_temp.add(data)
                        if (new_column < (datatemp.q2?:"").length) {
                            new_column = (datatemp.q2?:"").length
                        }
                        if (!list_abacus[current_pos].s3.isNullOrEmpty()) {
                            data = HashMap()
                            data[Constants.Que] = datatemp.q3?:""
                            data[Constants.Hint] = datatemp.h3?:""
                            data[Constants.Sign] = datatemp.s3?:""
                            list_abacus_main_temp.add(data)
                            if (new_column < (datatemp.q3?:"").length) {
                                new_column = (datatemp.q3?:"").length
                            }
                            if (!list_abacus[current_pos].s4.isNullOrEmpty()) {
                                data = HashMap()
                                data[Constants.Que] = datatemp.q4?:""
                                data[Constants.Hint] = datatemp.h4?:""
                                data[Constants.Sign] = datatemp.s4?:""
                                list_abacus_main_temp.add(data)
                                if (new_column < (datatemp.q4?:"").length) {
                                    new_column = (datatemp.q4?:"").length
                                }
                                if (!list_abacus[current_pos].s5.isNullOrEmpty()) {
                                    data = HashMap()
                                    data[Constants.Que] = datatemp.q5?:""
                                    data[Constants.Hint] = datatemp.h5?:""
                                    data[Constants.Sign] = datatemp.s5?:""
                                    list_abacus_main_temp.add(data)
                                    if (new_column < (datatemp.q5?:"").length) {
                                        new_column = (datatemp.q5?:"").length
                                    }
                                }
                            }
                        }
                    }
                } else if (datatemp.s1 == "*") {
                    abacus_type = 1
                } else if (datatemp.s1 == "/") {
                    abacus_type = 2
                }
            } else {
                abacus_type = 0
            }

//            val answer = java.lang.Double.valueOf(datatemp.ans)
            val answer = datatemp.getAnswer().toDouble()
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
                    val ans = datatemp.getAnswer().toString()
                    noOfDecimalPlace = ans.length - ans.indexOf(".") - 1
                    column = ans.length - 1
                }
            } else if (abacus_type == 2) {
                binding.recyclerview.adapter = adapterDivision
            }
            list_abacus_main.clear()
            list_abacus_main.addAll(list_abacus_main_temp)
            binding.cardAbacusQue.show()
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
    // not purchased and page completed
    private fun resetPurchaseDialog() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.txt_purchase_alert),getString(R.string.txt_page_reset_not)
            ,getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_not_purchased,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    goToPurchase()
                }
                override fun onConfirmationNoClick(bundle: Bundle?) = Unit
            })
    }
    // not purchased and reset dialog click
    private fun freePageCompleteDialog(msg : String) {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.txt_purchase_alert), msg,
            getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_not_purchased,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    goToPurchase()
                }
                override fun onConfirmationNoClick(bundle: Bundle?) {
                    goBack()
                }
            })
    }
    // purchased and page already completed
    private fun paidPageAlreadyCompletedDialog() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.txt_page_complete),getString(R.string.txt_page_completed_already_purchased)
            ,getString(R.string.yes_i_want_to_start),getString(R.string.no_thanks), icon = R.drawable.ic_alert_complete_page,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    resetProgressConfirm()
                }

                override fun onConfirmationNoClick(bundle: Bundle?) {
                    goBack()
                }
            })
    }
    // purchased and page completed
    private fun paidPageCompleteDialog() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.txt_page_complete),getString(R.string.txt_page_completed_msg)
            ,getString(R.string.yes_i_want_to_start),getString(R.string.no_thanks_continue_later), icon = R.drawable.ic_complete,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    resetProgressConfirm()
                }

                override fun onConfirmationNoClick(bundle: Bundle?) {
                    goBack()
                }
            })
    }
    // purchased and reset page progress
    private fun paidResetPageProgressDialog() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.txt_reset_page),getString(R.string.txt_reset_page_alert)
            ,getString(R.string.yes_i_m_sure),getString(R.string.no_please_continue), icon = R.drawable.ic_alert,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    resetProgressConfirm()
                }
                override fun onConfirmationNoClick(bundle: Bundle?) = Unit
            })
    }
    // reset page progress and start from 1st abacus
    private fun resetProgressConfirm() {
        updateToFirebase(0)
        removeSum()
        current_pos = 0
        startAbacus()
    }
    private fun goBack() {
        mNavController.navigateUp()
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
            lifecycleScope.launch {
                delay(1500)
                if (isPurchased && isHintSound) {
                    speakOut(resources.getString(R.string.speech_formula_for) + " " + speek_hint)
                }
            }
        } else if (hintPage.isNullOrEmpty()){
            binding.cardHint.hide()
            binding.cardTable.hide()
            binding.relativeTable.hide()
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
            if(requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
                moveToNext()
                isMoveNext = false
            }else{
                notOfflineSupportDialog()
            }

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
            prefManager.saveCurrentSum(pageId,current_pos)
            prefManager.setCustomParamInt(pageId + "value", number + 1)
            setDataOfNumber(false)
        } else if (abacus_type == 0 && current_pos == list_abacus.size - 1) {
            // page complete
            prefManager.setCustomParam(
                AppConstants.AbacusProgress.CompleteAbacusPos + pageId,
                current_pos.toString()
            )
            current_pos++
            prefManager.saveCurrentSum(pageId,current_pos)
            if (isPurchased) {
                paidPageCompleteDialog()
            } else {
                freePageCompleteDialog(getString(R.string.txt_page_completed_free))
            }
        } else {
            binding.tvAns.text = ""
            binding.cardHint.hide()
            current_pos++
            prefManager.saveCurrentSum(pageId,current_pos)
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

    private fun notOfflineSupportDialog() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.no_internet_working),getString(R.string.for_offline_support_msg)
            ,getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_sad_emoji,isCancelable = false,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    binding.cardPurchase.performClick()
                }
                override fun onConfirmationNoClick(bundle: Bundle?){
                    if (requireContext().isNetworkAvailable){
                        moveToNext()
                        isMoveNext = false
                    } else{
                        mNavController.navigateUp()
                    }
                }
            })
    }

    // update progress to Firebase
    private fun updateToFirebase(currentPos: Int) {
        // set from value when reset page progress for abacus type number
        if (abacusType == AppConstants.Extras_Comman.AbacusTypeNumber && currentPos == 0){
            prefManager.setCustomParamInt(pageId + "value", From)
        }
        FirebaseDatabase.getInstance().reference.child(AppConstants.AbacusProgress.Track + "/" + prefManager.getDeviceId() + "/" + pageId).child(AppConstants.AbacusProgress.Position).setValue(currentPos)
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
        ads()
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

    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y"
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y"
            && !isPurchased && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y") { // if not purchased
            showAMFullScreenAds(getString(R.string.interstitial_ad_unit_id_abacus_half_screen))
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
}