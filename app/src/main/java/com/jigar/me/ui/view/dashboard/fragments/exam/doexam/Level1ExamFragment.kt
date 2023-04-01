package com.jigar.me.ui.view.dashboard.fragments.exam.doexam

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.MyApplication
import com.jigar.me.R
import com.jigar.me.data.local.data.*
import com.jigar.me.data.model.dbtable.exam.ExamHistory
import com.jigar.me.databinding.FragmentExamLevel1Binding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.AbacusUtils
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.confirm_alerts.dialogs.exam.ExamCompleteDialog
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.utils.*
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
@AndroidEntryPoint
class Level1ExamFragment : BaseFragment(), ExamCompleteDialog.TestCompleteDialogInterface{

    private lateinit var mBinding: FragmentExamLevel1Binding
    private val apiViewModel by viewModels<AppViewModel>()
    private lateinit var mNavController: NavController

    private var listExam: List<BeginnerExamPaper> = ArrayList()
    private var currentQuestionPos = 0
    private var totalWrong = 0
    private var correctAns = ""
    private var theme = ""
    private var examLevel = "1"
    private var examLevelLable = Constants.examLevelBeginner
    private lateinit var mCalculator: Calculator
    private var total_sec = 0
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var objectListAdapter1: ObjectListAdapter = ObjectListAdapter(0, null, DataObjectsSize.Small)
    private var objectListAdapter2: ObjectListAdapter = ObjectListAdapter(0, null,DataObjectsSize.Small)
    private var clickType = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentExamLevel1Binding.inflate(inflater, container, false)
        setNavigationGraph()
        init()
        clickListener()
        ads()
        return mBinding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun init() {
        mCalculator = Calculator()
        handler = Handler(Looper.getMainLooper())

        getAndStartExam()
    }

    private fun getAndStartExam(){
        lifecycleScope.launch {
            theme = AbacusUtils.setAbacusColumn(prefManager,AbacusBeadType.Exam,mBinding.layoutAbacus1.abacusTop,mBinding.layoutAbacus1.abacusBottom,mBinding.layoutAbacus2.abacusTop,mBinding.layoutAbacus2.abacusBottom)
        }
        if (prefManager.getCustomParam(AppConstants.Extras_Comman.examLevel + examLevel,"").isEmpty()) {
            listExam = DataProvider.generateBegginerExamPaper(requireContext(),examLevel )
            prefManager.setCustomParam(AppConstants.Extras_Comman.examLevel + examLevel,Gson().toJson(listExam))
        } else {
            val type = object : TypeToken<List<BeginnerExamPaper>>() {}.type
            listExam = Gson().fromJson(prefManager.getCustomParam(AppConstants.Extras_Comman.examLevel + examLevel,""), type)
        }
        if(requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
            setDailyExamAbacus()
        } else {
            notOfflineSupportDialog()
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
                        setDailyExamAbacus()
                    } else{
                        mNavController.navigateUp()
                    }
                }
            })
    }
    private fun goToPurchase() {
        mNavController.navigate(R.id.action_level1ExamFragment_to_purchaseFragment)
    }

    private fun clickListener() {
        // blink animation
        mBinding.txtTapCorrectAns.setBlinkAnimation()
        mBinding.cardBack.onClick { examLeaveAlert() }
        mBinding.cardAnswer1.onClick { onViewClick("answer1") }
        mBinding.cardAnswer2.onClick { onViewClick("answer2") }
        mBinding.cardAnswer3.onClick { onViewClick("answer3") }
        mBinding.cardAnswer4.onClick { onViewClick("answer4") }
    }

    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" // local
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" && // if yes in firebase
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")) {
            showAMBannerAds(mBinding.adView,getString(R.string.banner_ad_unit_id_exam))
        }
    }

    private fun setDailyExamAbacus() {
        mBinding.progressHorizontal.max = listExam.size
        total_sec = 0
        currentQuestionPos = 0
        totalWrong = 0
        mBinding.cardAnswer1.isEnabled = true
        mBinding.cardAnswer2.isEnabled = true
        mBinding.cardAnswer3.isEnabled = true
        mBinding.cardAnswer4.isEnabled = true
        val delay = 1000 //milliseconds

        runnable = object : Runnable {
            override fun run() {
                //do something
                total_sec++
                val minutes = total_sec / 60
                val seconds = total_sec % 60
                mBinding.txtTimer.text = resources.getString(R.string.Time) + " : " + String.format("%d:%02d",minutes,seconds)
                handler?.postDelayed(this, delay.toLong())
            }
        }
        handler?.postDelayed(runnable!!, delay.toLong())
        setExamPaper()

        // event log
        MyApplication.logEvent(AppConstants.FirebaseEvents.DailyExam, Bundle().apply {
            putString(AppConstants.FirebaseEvents.deviceId, prefManager.getDeviceId())
            putString(AppConstants.FirebaseEvents.DailyExamLevel, examLevelLable)
        })
    }

    private fun setExamPaper() {
        if (currentQuestionPos >= listExam.size) {
            completePopup()
        } else {
            mBinding.recyclerviewObjects2.hide()
            mBinding.spaceBetween.hide()
            mBinding.imgSign.hide()

            if (listExam[currentQuestionPos].type == BeginnerExamQuestionType.Count){
                val str = getString(R.string.count_the)+" <b><font color='#E14A4D'>Abacus Beads</font></b>"
                mBinding.txtHeaderTitle.text = HtmlCompat.fromHtml(str,HtmlCompat.FROM_HTML_MODE_COMPACT)

                if (listExam[currentQuestionPos].isAbacusQuestion == true){
                    mBinding.linearQuestion.hide()
                    mBinding.relAbacus.show()
                    mBinding.layoutAbacus1.relAbacus.show()
                    mBinding.layoutAbacus2.relAbacus.hide()
                    mBinding.layoutAbacus2.abacusTop.hide()
                    mBinding.layoutAbacus2.abacusBottom.hide()
                    mBinding.layoutAbacus1.abacusTop.show()
                    mBinding.layoutAbacus1.abacusBottom.show()
                    mBinding.imgSign1.hide()
                    lifecycleScope.launch {
                        AbacusUtils.setNumber(listExam[currentQuestionPos].value,mBinding.layoutAbacus1.abacusTop,mBinding.layoutAbacus1.abacusBottom)
                    }
                }else{
                    mBinding.layoutAbacus1.relAbacus.hide()
                    mBinding.layoutAbacus2.relAbacus.hide()
                    mBinding.layoutAbacus1.abacusTop.hide()
                    mBinding.layoutAbacus1.abacusBottom.hide()
                    mBinding.layoutAbacus2.abacusTop.hide()
                    mBinding.layoutAbacus2.abacusBottom.hide()
                    mBinding.relAbacus.hide()
                    mBinding.linearQuestion.show()
                    val str = getString(R.string.count_the)+" <b><font color='#E14A4D'>"+listExam[currentQuestionPos].imageData.name+"</font></b>"
                    mBinding.txtHeaderTitle.text = HtmlCompat.fromHtml(str,HtmlCompat.FROM_HTML_MODE_COMPACT)

                    var list1ImageCount = 0
                    var list2ImageCount = 0
                    val totalCount = listExam[currentQuestionPos].value.toInt()
                    if (totalCount > 10){
                        list1ImageCount = totalCount/2
                        list2ImageCount = totalCount - list1ImageCount
                    }else if (totalCount < 6){
                        list1ImageCount = totalCount
                    }else{
                        list1ImageCount = 5
                        list2ImageCount = totalCount - 5
                    }
                    val size = if (list1ImageCount < 5 && list2ImageCount == 0){
                        DataObjectsSize.Large
                    }else if (list1ImageCount == 5 && list2ImageCount == 0){
                        DataObjectsSize.Medium
                    }else{
                        DataObjectsSize.Small
                    }

                    if (list1ImageCount > 0){
                        objectListAdapter1= ObjectListAdapter(list1ImageCount, listExam[currentQuestionPos].imageData,size)
                        mBinding.recyclerviewObjects1.layoutManager = GridLayoutManager(requireContext(),list1ImageCount)
                        mBinding.recyclerviewObjects1.adapter = objectListAdapter1
                    }
                    if (list2ImageCount > 0){
                        objectListAdapter2= ObjectListAdapter(list2ImageCount, listExam[currentQuestionPos].imageData,size)
                        mBinding.recyclerviewObjects2.layoutManager = GridLayoutManager(requireContext(),list2ImageCount)
                        mBinding.recyclerviewObjects2.adapter = objectListAdapter2
                        mBinding.recyclerviewObjects2.show()
                        mBinding.spaceBetween.show()
                    }
                }
            }else if (listExam[currentQuestionPos].type == BeginnerExamQuestionType.Additions || listExam[currentQuestionPos].type == BeginnerExamQuestionType.Subtractions){

                val list1ImageCount = listExam[currentQuestionPos].value.toInt()
                val list2ImageCount = listExam[currentQuestionPos].value2.toInt()

                if (listExam[currentQuestionPos].isAbacusQuestion == true){
                    mBinding.relAbacus.show()
                    mBinding.linearQuestion.hide()
                    mBinding.imgSign1.show()
                    mBinding.layoutAbacus2.relAbacus.show()
                    mBinding.layoutAbacus1.relAbacus.show()
                    mBinding.layoutAbacus2.abacusTop.show()
                    mBinding.layoutAbacus2.abacusBottom.show()
                    mBinding.layoutAbacus1.abacusTop.show()
                    mBinding.layoutAbacus1.abacusBottom.show()

                    if (listExam[currentQuestionPos].type == BeginnerExamQuestionType.Additions){
                        val str = getString(R.string.additions_of)+" <b><font color='#E14A4D'>Abacus Beads</font></b>"
                        mBinding.txtHeaderTitle.text = HtmlCompat.fromHtml(str,HtmlCompat.FROM_HTML_MODE_COMPACT)
                        mBinding.imgSign1.setImageResource(R.drawable.cal_plus)
                    }else if (listExam[currentQuestionPos].type == BeginnerExamQuestionType.Subtractions){
                        val str = getString(R.string.subtraction_of)+" <b><font color='#E14A4D'>Abacus Beads</font></b>"
                        mBinding.txtHeaderTitle.text = HtmlCompat.fromHtml(str,HtmlCompat.FROM_HTML_MODE_COMPACT)
                        mBinding.imgSign1.setImageResource(R.drawable.cal_minus)
                    }

                    lifecycleScope.launch {
                        AbacusUtils.setNumber(list1ImageCount.toString(),mBinding.layoutAbacus1.abacusTop,mBinding.layoutAbacus1.abacusBottom,list2ImageCount.toString(),mBinding.layoutAbacus2.abacusTop,mBinding.layoutAbacus2.abacusBottom)
                    }
                }else{
                    mBinding.layoutAbacus1.relAbacus.hide()
                    mBinding.layoutAbacus2.relAbacus.hide()
                    mBinding.layoutAbacus1.abacusTop.hide()
                    mBinding.layoutAbacus1.abacusBottom.hide()
                    mBinding.layoutAbacus2.abacusTop.hide()
                    mBinding.layoutAbacus2.abacusBottom.hide()
                    mBinding.relAbacus.hide()
                    mBinding.linearQuestion.show()
                    if (listExam[currentQuestionPos].type == BeginnerExamQuestionType.Additions){
                        val str = getString(R.string.additions_of)+" <b><font color='#E14A4D'>"+listExam[currentQuestionPos].imageData.name+"</font></b>"
                        mBinding.txtHeaderTitle.text = HtmlCompat.fromHtml(str,HtmlCompat.FROM_HTML_MODE_COMPACT)
                        mBinding.imgSign.setImageResource(R.drawable.cal_plus)
                    }else if (listExam[currentQuestionPos].type == BeginnerExamQuestionType.Subtractions){
                        val str = getString(R.string.subtraction_of)+" <b><font color='#E14A4D'>"+listExam[currentQuestionPos].imageData.name+"</font></b>"
                        mBinding.txtHeaderTitle.text = HtmlCompat.fromHtml(str,HtmlCompat.FROM_HTML_MODE_COMPACT)
                        mBinding.imgSign.setImageResource(R.drawable.cal_minus)
                    }

                    objectListAdapter1= ObjectListAdapter(list1ImageCount, listExam[currentQuestionPos].imageData,DataObjectsSize.ExtraSmall)
                    mBinding.recyclerviewObjects1.layoutManager = GridLayoutManager(requireContext(),list1ImageCount)
                    mBinding.recyclerviewObjects1.adapter = objectListAdapter1

                    objectListAdapter2= ObjectListAdapter(list2ImageCount, listExam[currentQuestionPos].imageData,DataObjectsSize.ExtraSmall)
                    mBinding.recyclerviewObjects2.layoutManager = GridLayoutManager(requireContext(),list2ImageCount)
                    mBinding.recyclerviewObjects2.adapter = objectListAdapter2

                    mBinding.recyclerviewObjects2.show()
                    mBinding.imgSign.show()
                }

            }
            mBinding.progressHorizontal.progress = (currentQuestionPos + 1)

            val tempAns = when (listExam[currentQuestionPos].type) {
                BeginnerExamQuestionType.Additions -> {
                    listExam[currentQuestionPos].value+"+"+listExam[currentQuestionPos].value2
                }
                BeginnerExamQuestionType.Subtractions -> {
                    listExam[currentQuestionPos].value+"-"+listExam[currentQuestionPos].value2
                }
                else -> {
                    listExam[currentQuestionPos].value
                }
            }
            val resultObject = mCalculator.getResult(tempAns,tempAns)
            correctAns = CommonUtils.removeTrailingZero(resultObject)
            val listAnswerTemp: MutableList<Int> = ArrayList()
            val listAnswer: MutableList<Int?> = ArrayList()
            if (correctAns.toInt() - 1 > 0) {
                listAnswerTemp.add(correctAns.toInt() - 1)
            }
            if (correctAns.toInt() - 2 > 0) {
                listAnswerTemp.add(correctAns.toInt() - 2)
            }
            if (correctAns.toInt() - 3 > 0) {
                listAnswerTemp.add(correctAns.toInt() - 3)
            }
            listAnswerTemp.add(correctAns.toInt() + 1)
            listAnswerTemp.add(correctAns.toInt() + 2)
            listAnswerTemp.add(correctAns.toInt() + 3)
            for (i in 0..2) {
                val pos = Random().nextInt(listAnswerTemp.size)
                listAnswer.add(listAnswerTemp[pos])
                listAnswerTemp.removeAt(pos)
            }
            listAnswer.add(correctAns.toInt())
            listAnswer.shuffle()
            try {
                mBinding.txtAnswer1.text = listAnswer[0].toString()
                mBinding.txtAnswer2.text = listAnswer[1].toString()
                mBinding.txtAnswer3.text = listAnswer[2].toString()
                mBinding.txtAnswer4.text = listAnswer[3].toString()
            } catch (e: Exception) {
                e.printStackTrace()
                requireContext().getString(R.string.some_thing_wrong)
                onBack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (runnable != null){
            handler?.removeCallbacks(runnable!!)
        }
    }
    private fun completePopup() {
        mBinding.cardAnswer1.isEnabled = false
        mBinding.cardAnswer2.isEnabled = false
        mBinding.cardAnswer3.isEnabled = false
        mBinding.cardAnswer4.isEnabled = false
        if (runnable != null){
            handler?.removeCallbacks(runnable!!)
        }
        val minutes = total_sec / 60
        val seconds = total_sec % 60
        val totalTime = String.format("%d:%02d", minutes, seconds)
        val right = listExam.size - totalWrong
        CoroutineScope(Dispatchers.Main).launch{
            apiViewModel.saveExamResultDB(ExamHistory(0,total_sec,examLevelLable, arrayListOf(), listExam,theme = theme))
        }
        // set empty question list
        prefManager.setCustomParam(AppConstants.Extras_Comman.examLevel + examLevel,"")
        val previousCount = prefManager.getCustomParamInt(AppConstants.Extras_Comman.examGivenCount + examLevel,0)
        prefManager.setCustomParamInt(AppConstants.Extras_Comman.examGivenCount + examLevel,(previousCount+1))

        ExamCompleteDialog.showPopup(requireActivity(),totalTime,"0",totalWrong.toString(),right.toString(),listExam.size.toString(),this)
    }

    private fun onViewClick(clickType: String) {
        this.clickType = clickType
        if(requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
            clickOtions()
        }else{
            notOfflineSupportDialog2()
        }
    }
    private fun notOfflineSupportDialog2() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.no_internet_working),getString(R.string.for_offline_support_msg)
            ,getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_sad_emoji,isCancelable = false,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    goToPurchase()
                }
                override fun onConfirmationNoClick(bundle: Bundle?){
                    if (requireContext().isNetworkAvailable){
                        clickOtions()
                    } else{
                        mNavController.navigateUp()
                    }
                }
            })
    }
    private fun clickOtions() {
        when (clickType) {
            "answer1" -> {
                if (correctAns != mBinding.txtAnswer1.text.toString()) {
                    totalWrong++
                }
                listExam[currentQuestionPos].userAnswer = mBinding.txtAnswer1.text.toString()
                if (currentQuestionPos == (listExam.size -1)){
                    completePopup()
                }else if (currentQuestionPos < listExam.size){
                    currentQuestionPos++
                    setExamPaper()
                }
            }
            "answer2" -> {
                if (correctAns != mBinding.txtAnswer2.text.toString()) {
                    totalWrong++
                }
                listExam[currentQuestionPos].userAnswer = mBinding.txtAnswer2.text.toString()
                if (currentQuestionPos == (listExam.size -1)){
                    completePopup()
                }else if (currentQuestionPos < listExam.size){
                    currentQuestionPos++
                    setExamPaper()
                }
            }
            "answer3" -> {
                if (correctAns != mBinding.txtAnswer3.text.toString()) {
                    totalWrong++
                }
                listExam[currentQuestionPos].userAnswer = mBinding.txtAnswer3.text.toString()
                if (currentQuestionPos == (listExam.size -1)){
                    completePopup()
                }else if (currentQuestionPos < listExam.size){
                    currentQuestionPos++
                    setExamPaper()
                }
            }
            "answer4" -> {
                if (correctAns != mBinding.txtAnswer4.text.toString()) {
                    totalWrong++
                }
                listExam[currentQuestionPos].userAnswer = mBinding.txtAnswer4.text.toString()
                if (currentQuestionPos == (listExam.size -1)){
                    completePopup()
                }else if (currentQuestionPos < listExam.size){
                    currentQuestionPos++
                    setExamPaper()
                }
            }
        }
    }


    // exam leave listener
    override fun testGiveAgain() {
        getAndStartExam()
    }

    fun examLeaveAlert() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.leave_exam_alert),getString(R.string.leave_exam_msg)
            ,getString(R.string.yes_i_m_sure),getString(R.string.no_please_continue), icon = R.drawable.ic_alert,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    testLeaveConfirm()
                }
                override fun onConfirmationNoClick(bundle: Bundle?) = Unit
            })
    }

    private fun testLeaveConfirm() {
        if (handler != null && runnable != null) {
            handler?.removeCallbacks(runnable!!)
        }
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" // local
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" && // if yes in firebase
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")) {
            newInterstitialAdRequest(getString(R.string.interstitial_ad_unit_id_exam_leave))
        }else{
            onBack()
        }
    }
    // exam complete listner
    override fun testCompleteClose() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" // local
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" && // if yes in firebase
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")) {
            newInterstitialAdRequest(getString(R.string.interstitial_ad_unit_id_exam_complete_close))
        }else{
            onBack()
        }
    }


    override fun testCompleteGotoResult() {
        val bundle = Bundle()
        bundle.putString(AppConstants.Extras_Comman.examResult, Gson().toJson(listExam))
        bundle.putString(AppConstants.Extras_Comman.type, Constants.examLevelBeginner)
        bundle.putString(AppConstants.Extras_Comman.examAbacusType, theme)
        mNavController.navigate(R.id.action_level1ExamFragment_to_examResultFragment, bundle)
    }
    private fun onBack() {
        mNavController.navigateUp()
    }

    // exam complate and close, leave exam
    private fun newInterstitialAdRequest(adUnit : String) {
        showLoading()
        val isAdmob = prefManager.getCustomParamBoolean(AppConstants.AbacusProgress.isAdmob,true)
        if (isAdmob){
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(requireContext(),adUnit, adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    hideLoadingAndFinish()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    showInterstitialRequest(interstitialAd)
                }
            })
        }else{
            val adRequest = AdManagerAdRequest.Builder().build()
            AdManagerInterstitialAd.load(requireContext(),adUnit, adRequest, object : AdManagerInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    hideLoadingAndFinish()
                }

                override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                    showInterstitialRequest(interstitialAd)
                }
            })
        }
    }

    fun showInterstitialRequest(mInterstitialAd: AdManagerInterstitialAd) {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        mInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("AdmobInterStitialAds", "exam Ad was dismissed.")
                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d("AdmobInterStitialAds", "exam Ad failed to show.")
                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
                hideLoadingAndFinish()
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("AdmobInterStitialAds", "exam Ad showed fullscreen content.")
                // Called when ad is dismissed.
                hideLoadingAndFinish()
            }
        }
        mInterstitialAd.show(requireActivity())
    }

    fun showInterstitialRequest(mInterstitialAd: InterstitialAd) {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        mInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("AdmobInterStitialAds", "exam Ad was dismissed.")
                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d("AdmobInterStitialAds", "exam Ad failed to show.")
                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
                hideLoadingAndFinish()
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("AdmobInterStitialAds", "exam Ad showed fullscreen content.")
                // Called when ad is dismissed.
                hideLoadingAndFinish()
            }
        }
        mInterstitialAd.show(requireActivity())
    }

    private fun hideLoadingAndFinish() {
        hideLoading()
        onBack()
    }

}