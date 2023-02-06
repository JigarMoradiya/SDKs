package com.jigar.me.ui.view.dashboard.fragments.exam.doexam

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.MyApplication
import com.jigar.me.R
import com.jigar.me.data.model.dbtable.exam.DailyExamData
import com.jigar.me.data.model.dbtable.exam.ExamHistory
import com.jigar.me.databinding.FragmentExamBinding
import com.jigar.me.ui.view.base.BaseFragment
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
class ExamFragment : BaseFragment(),
    ExamCompleteDialog.TestCompleteDialogInterface{

    private lateinit var mBinding: FragmentExamBinding
    private val apiViewModel by viewModels<AppViewModel>()

    private var examLevel = ""
    private var examLevelLable = ""
    private var list_abacus: List<DailyExamData> = ArrayList()
    private var currentQuestionPos = 0
    private var totalSkip = 0
    private var totalWrong = 0
    private var CorrectAns = ""
    private lateinit var mCalculator: Calculator
    private var total_sec = 0
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        examLevel = ExamFragmentArgs.fromBundle(requireArguments()).examLevel
        examLevelLable = ExamFragmentArgs.fromBundle(requireArguments()).examLevelLable
        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentExamBinding.inflate(inflater, container, false)
        init()
        clickListener()
        ads()
        return mBinding.root
    }

    private fun init() {
        mCalculator = Calculator()
        handler = Handler(Looper.getMainLooper())

        getAndStartExam()
    }

    private fun getAndStartExam() {
        if (requireContext().isNetworkAvailable) {
            if (prefManager.getCustomParam(AppConstants.Extras_Comman.examLevel + examLevel,"").isEmpty()) {
                apiViewModel.getExamAbacus(examLevel)
            } else {
                setDailyExamAbacus()
            }
        } else {
            requireContext().toastS(getString(R.string.no_internet))
            onBack()
        }
    }

    private fun initObserver() {
        apiViewModel.getExamAbacusResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading()
                }
                is Resource.Success -> {
                    hideLoading()
                    it.value.content?.also {
                        val list: ArrayList<DailyExamData> = Gson().fromJson(
                            it.asJsonArray, object : TypeToken<ArrayList<DailyExamData>>() {}.type)
                        prefManager.setCustomParam(AppConstants.Extras_Comman.examLevel + examLevel, Gson().toJson(list))
                        setDailyExamAbacus()
                    }
                }
                is Resource.Failure -> {
                    hideLoading()
                    it.errorBody?.let { it1 -> requireContext().toastL(it1) }
                }
                else -> {}
            }
        }
    }

    private fun clickListener() {
        // blink animation
        mBinding.txtTapCorrectAns.setBlinkAnimation()
        mBinding.cardBack.onClick { onViewClick("back") }
        mBinding.cardSkip.onClick { onViewClick("skip") }
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
        val type = object : TypeToken<List<DailyExamData>>() {}.type
        list_abacus = Gson().fromJson(prefManager.getCustomParam(AppConstants.Extras_Comman.examLevel + examLevel,""), type)
        mBinding.progressHorizontal.max = list_abacus.size
        (list_abacus as ArrayList<DailyExamData>).shuffle()
        total_sec = 0
        currentQuestionPos = 0
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
        if (currentQuestionPos >= list_abacus.size) {
            completePopup()
        } else {
            mBinding.progressHorizontal.progress = (currentQuestionPos + 1)
            mBinding.txtAbacus.text = list_abacus[currentQuestionPos].questions
                .replace("+", " + ")
                .replace("-", " - ")
                .replace("x", " x ")
                .replace("/", " / ")+" = "
            val resultObject = mCalculator.getResult(
                list_abacus[currentQuestionPos].questions,
                list_abacus[currentQuestionPos].questions
            )
            CorrectAns = CommonUtils.removeTrailingZero(resultObject)
            val listAnswerTemp: MutableList<Int> = ArrayList()
            val listAnswer: MutableList<Int?> = ArrayList()
            if (CorrectAns.toInt() - 1 > 0) {
                listAnswerTemp.add(CorrectAns.toInt() - 1)
            }
            if (CorrectAns.toInt() - 2 > 0) {
                listAnswerTemp.add(CorrectAns.toInt() - 2)
            }
            if (CorrectAns.toInt() - 3 > 0) {
                listAnswerTemp.add(CorrectAns.toInt() - 3)
            }
            listAnswerTemp.add(CorrectAns.toInt() + 1)
            listAnswerTemp.add(CorrectAns.toInt() + 2)
            listAnswerTemp.add(CorrectAns.toInt() + 3)
            for (i in 0..2) {
                val pos = Random().nextInt(listAnswerTemp.size)
                listAnswer.add(listAnswerTemp[pos])
                listAnswerTemp.removeAt(pos)
            }
            listAnswer.add(CorrectAns.toInt())
            listAnswer.shuffle()
            try {
                mBinding.txtAnswer1.text = listAnswer[0].toString()
                mBinding.txtAnswer2.text = listAnswer[1].toString()
                mBinding.txtAnswer3.text = listAnswer[2].toString()
                mBinding.txtAnswer4.text = listAnswer[3].toString()
            } catch (e: Exception) {
                e.printStackTrace()
                requireContext().toastL(getString(R.string.some_thing_wrong))
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
        val right = list_abacus.size - totalWrong - totalSkip
//        val totalRight = right.toString() + " (" + 100 * right / list_abacus.size + "%)"
        CoroutineScope(Dispatchers.Default).launch{
            apiViewModel.saveExamResultDB(ExamHistory(0,total_sec,examLevelLable,list_abacus))
        }
        // set empty question list
        prefManager.setCustomParam(AppConstants.Extras_Comman.examLevel + examLevel,"")

        val previousCount = prefManager.getCustomParamInt(AppConstants.Extras_Comman.examGivenCount + examLevel,0)
        prefManager.setCustomParamInt(AppConstants.Extras_Comman.examGivenCount + examLevel,(previousCount+1))

        ExamCompleteDialog.showPopup(
            requireActivity(),
            totalTime,
            totalSkip.toString(),
            totalWrong.toString(),
            right.toString(),list_abacus.size.toString(),
            this
        )
    }

    private fun onViewClick(clickType: String) {
        when (clickType) {
            "back" -> {
                examLeaveAlert()
            }
            "skip" -> {
                totalSkip++
                list_abacus[currentQuestionPos].userAnswer = ""
                if (currentQuestionPos == (list_abacus.size -1)){
                    completePopup()
                }else{
                    if (currentQuestionPos < list_abacus.size){
                        currentQuestionPos++
                        setExamPaper()
                    }
                }


            }
            "answer1" -> {
                if (CorrectAns != mBinding.txtAnswer1.text.toString()) {
                    totalWrong++
                }
                list_abacus[currentQuestionPos].userAnswer = mBinding.txtAnswer1.text.toString()
                if (currentQuestionPos == (list_abacus.size -1)){
                    completePopup()
                }else if (currentQuestionPos < list_abacus.size){
                    currentQuestionPos++
                    setExamPaper()
                }
            }
            "answer2" -> {
                if (CorrectAns != mBinding.txtAnswer2.text.toString()) {
                    totalWrong++
                }
                list_abacus[currentQuestionPos].userAnswer = mBinding.txtAnswer2.text.toString()
                if (currentQuestionPos == (list_abacus.size -1)){
                    completePopup()
                }else if (currentQuestionPos < list_abacus.size){
                    currentQuestionPos++
                    setExamPaper()
                }
            }
            "answer3" -> {
                if (CorrectAns != mBinding.txtAnswer3.text.toString()) {
                    totalWrong++
                }
                list_abacus[currentQuestionPos].userAnswer = mBinding.txtAnswer3.text.toString()
                if (currentQuestionPos == (list_abacus.size -1)){
                    completePopup()
                }else if (currentQuestionPos < list_abacus.size){
                    currentQuestionPos++
                    setExamPaper()
                }
            }
            "answer4" -> {
                if (CorrectAns != mBinding.txtAnswer4.text.toString()) {
                    totalWrong++
                }
                list_abacus[currentQuestionPos].userAnswer = mBinding.txtAnswer4.text.toString()
                if (currentQuestionPos == (list_abacus.size -1)){
                    completePopup()
                }else if (currentQuestionPos < list_abacus.size){
                    currentQuestionPos++
                    setExamPaper()
                }
            }
        }
    }


    // exam leave listner
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
            newInterstitialAdRequestLeaveExam()
        }else{
            onBack()
        }
    }
    // exam complete listner
    override fun testGiveAgain() {
        getAndStartExam()
    }
    override fun testCompleteClose() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" // local
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" && // if yes in firebase
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")) {
            newInterstitialAdRequestExamCompleteClose()
        }else{
            onBack()
        }
    }


    override fun testCompleteGotoResult() {
        val bundle = Bundle()
        bundle.putString(AppConstants.Extras_Comman.examResult, Gson().toJson(list_abacus))
        bundle.putString(AppConstants.Extras_Comman.type, examLevelLable)
        mNavController.navigate(R.id.action_examFragment_to_examResultFragment, bundle)
    }
    private  fun onBack() {
        mNavController.navigateUp()
    }

    // exam complate and close
    private fun newInterstitialAdRequestExamCompleteClose() {
        val adRequest = AdRequest.Builder().build()
        showLoading()
        InterstitialAd.load(requireContext(),getString(R.string.interstitial_ad_unit_id_exam_complete_close), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                hideLoadingAndFinish()
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                showInterstitialRequest(interstitialAd)
            }
        })
    }

    // show leave ads
    private fun newInterstitialAdRequestLeaveExam() {
        val adRequest = AdRequest.Builder().build()
        showLoading()
        InterstitialAd.load(requireContext(),getString(R.string.interstitial_ad_unit_id_exam_leave), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                hideLoadingAndFinish()
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                showInterstitialRequest(interstitialAd)
            }
        })
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