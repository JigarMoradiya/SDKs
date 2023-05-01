package com.jigar.me.ui.view.dashboard.fragments.exercise

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jigar.me.R
import com.jigar.me.data.local.data.*
import com.jigar.me.databinding.FragmentAbacusExerciseBinding
import com.jigar.me.databinding.FragmentAbacusSubBinding
import com.jigar.me.databinding.FragmentExerciseHomeBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.*
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.confirm_alerts.dialogs.ExerciseCompleteDialog
import com.jigar.me.ui.view.dashboard.fragments.exercise.adapter.ExerciseAdditionSubtractionAdapter
import com.jigar.me.ui.view.dashboard.fragments.exercise.adapter.ExerciseLevelPagerAdapter
import com.jigar.me.utils.*
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ExerciseHomeFragment : BaseFragment(), AbacusMasterBeadShiftListener, OnAbacusValueChangeListener,
    ExerciseLevelPagerAdapter.OnItemClickListener,
    ExerciseCompleteDialog.ExerciseCompleteDialogInterface {
    private lateinit var binding: FragmentExerciseHomeBinding
    private var abacusBinding: FragmentAbacusExerciseBinding? = null
    private lateinit var mNavController: NavController
    private var valuesAnswer: Int = -1
    private var currentSumVal = 0f
    private var totalTimeLeft = 0L
    private var isPurchased = false
    private var themeContent : AbacusContent? = null
    private var theme = AppConstants.Settings.theam_Default
    private var isResetRunning = false
    private lateinit var exerciseLevelPagerAdapter: ExerciseLevelPagerAdapter
    private lateinit var exerciseAdditionSubtractionAdapter: ExerciseAdditionSubtractionAdapter
    private var listExerciseAdditionSubtractionQuestion = arrayListOf<String>()
    private var listKeyboardAnswer = arrayListOf<String>()
    private var listExerciseAdditionSubtraction : MutableList<ExerciseList> = arrayListOf()
    private var exercisePosition = 0
    private var currentChildData : ExerciseLevelDetail? = null
    private var currentParentData: ExerciseLevel? = null
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentExerciseHomeBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initViews()
        initListener()
        ads()
        return binding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" // local
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" && // if yes in firebase
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")) {
            showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_exercise))
        }
    }
    private fun initViews() {
        with(prefManager){
            isPurchased = getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
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

        themeContent = DataProvider.findAbacusThemeType(requireContext(),theme,AbacusBeadType.Exercise)
        themeContent?.dividerColor1?.let{
            val finalColor = CommonUtils.mixTwoColors(ContextCompat.getColor(requireContext(),R.color.white), ContextCompat.getColor(requireContext(),it), 0.85f)
            binding.cardMain.backgroundTintList = ColorStateList.valueOf(finalColor)
        }
        themeContent?.resetBtnColor8?.let{
            binding.indicatorPager.selectedDotColor = ContextCompat.getColor(requireContext(),it)
            binding.txtTimer.setTextColor(ContextCompat.getColor(requireContext(),it))
            binding.txtQueLabel.setTextColor(ContextCompat.getColor(requireContext(),it))
        }

        exerciseAdditionSubtractionAdapter = ExerciseAdditionSubtractionAdapter(listExerciseAdditionSubtractionQuestion,themeContent)
        binding.recyclerviewExercise.adapter = exerciseAdditionSubtractionAdapter

        exerciseLevelPagerAdapter = ExerciseLevelPagerAdapter(DataProvider.getExerciseList(requireContext()),prefManager,this@ExerciseHomeFragment,themeContent)
        binding.viewPager.adapter = exerciseLevelPagerAdapter
        binding.indicatorPager.attachToPager(binding.viewPager)
        setAbacus()
    }
    private fun initListener() {
        binding.imgEarse.onClick {
            if (listKeyboardAnswer.isNotNullOrEmpty()){
                listKeyboardAnswer.removeAt(listKeyboardAnswer.lastIndex)
                setKeyboardAnswer()
            }
        }
        binding.txtAllClear.onClick {
            if (listKeyboardAnswer.isNotNullOrEmpty()){
                listKeyboardAnswer.clear()
                setKeyboardAnswer()
            }
        }
        binding.imgBack.onClick { exerciseLeaveAlert() }
        binding.txt0.onClick { addKeyboardValue("0") }
        binding.txt1.onClick { addKeyboardValue("1") }
        binding.txt2.onClick { addKeyboardValue("2") }
        binding.txt3.onClick { addKeyboardValue("3") }
        binding.txt4.onClick { addKeyboardValue("4") }
        binding.txt5.onClick { addKeyboardValue("5") }
        binding.txt6.onClick { addKeyboardValue("6") }
        binding.txt7.onClick { addKeyboardValue("7") }
        binding.txt8.onClick { addKeyboardValue("8") }
        binding.txt9.onClick { addKeyboardValue("9") }

        binding.txtNext.onClick {
            if(requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
                if (!binding.tvAnswer.text.toString().isNullOrEmpty() && binding.tvAnswer.text.toString() != "0"){
                    listExerciseAdditionSubtraction[exercisePosition].userAnswer = binding.tvAnswer.text.toString().toInt()
                }
                abacusBinding?.ivReset?.performClick()
                if (exercisePosition < listExerciseAdditionSubtraction.lastIndex){
                    exercisePosition++
                    setQuestions()
                }else{
                    tickerChannel.cancel()
                    openCompleteDialog()
                }
            }else{
                notOfflineSupportDialog()
            }

        }
    }

    private fun addKeyboardValue(value : String){
        if (listKeyboardAnswer.size < 7){
            if (value == "0"){
                if (listKeyboardAnswer.isNotEmpty()){
                    listKeyboardAnswer.add(value)
                }
            }else{
                listKeyboardAnswer.add(value)
            }
            setKeyboardAnswer()
        }
    }

    private fun setKeyboardAnswer() {
        AbacusMasterSound.playTap(requireContext())
        setNumber()
    }

    private fun setNumber() {
        val questionTemp = if (listKeyboardAnswer.isNotEmpty()){
            listKeyboardAnswer.joinToString("")
        }else{
            "0"
        }
        val topPositions = ArrayList<Int>()
        val bottomPositions = ArrayList<Int>()
        val totalLength = 7
        val remainLength = totalLength - questionTemp.length
        var zero = ""
        for (i in 1..remainLength){
            zero += "0"
        }
        val question = zero+questionTemp
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

        setSelectedPositions(topPositions, bottomPositions,null)
    }

    private fun setSelectedPositions(
        topSelectedPositions: ArrayList<Int>,
        bottomSelectedPositions: ArrayList<Int>,
        setPositionCompleteListener: AbacusMasterCompleteListener?
    ) {
        if (isAdded) {
            //app was crashing if position set before update no of row count. so added this delay.
            abacusBinding?.abacusBottom?.post {
                abacusBinding?.abacusTop?.setSelectedPositions(topSelectedPositions,setPositionCompleteListener)
                abacusBinding?.abacusBottom?.setSelectedPositions(bottomSelectedPositions,setPositionCompleteListener)
            }
        }
    }

    override fun exerciseCompleteCloseDialog() {
        val currentPos = binding.viewPager.currentItem
        val list = exerciseLevelPagerAdapter.listData
        exerciseLevelPagerAdapter = ExerciseLevelPagerAdapter(list,prefManager,this@ExerciseHomeFragment)
        binding.viewPager.adapter = exerciseLevelPagerAdapter
        binding.viewPager.currentItem = currentPos

        binding.linearExerciseAddSub.hide()
        binding.linearTime.hide()
        binding.linearLevel.show()
        binding.imgBack.show()
    }
    override fun onExerciseStartClick() {
        if(requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
            abacusBinding?.ivReset?.performClick()
            lifecycleScope.launch {
                delay(400)
                val parentData = exerciseLevelPagerAdapter.listData[binding.viewPager.currentItem]
                val childData = parentData.list[parentData.selectedChildPos]
                currentChildData = childData
                currentParentData = parentData

                exercisePosition = 0
                binding.linearExerciseAddSub.show()
                binding.linearTime.show()
                binding.linearLevel.hide()
                binding.imgBack.hide()
                when (parentData.id) {
                    "1" -> {
                        binding.recyclerviewExercise.show()
                        binding.txtMultiplication.hide()
                        listExerciseAdditionSubtraction = DataProvider.generateAdditionSubExercise(childData)
                        setQuestions()
                    }
                    "2" -> {
                        binding.recyclerviewExercise.hide()
                        binding.txtMultiplication.show()
                        listExerciseAdditionSubtraction = DataProvider.generateMultiplicationExercise(childData)
                        setQuestions()
                    }
                    "3" -> {
                        binding.recyclerviewExercise.hide()
                        binding.txtMultiplication.show()
                        listExerciseAdditionSubtraction = DataProvider.generateDivisionExercise(childData)

                        setQuestions()
                    }
                }
                totalTimeLeft = TimeUnit.MINUTES.toSeconds(childData.totalTime.toLong())
//                totalTimeLeft = TimeUnit.MINUTES.toMillis(1L)
                startTimer()
            }
        }else{
            notOfflineSupportDialog()
        }

    }
    private var tickerChannel = ticker(delayMillis = 1000, initialDelayMillis = 0)
    private fun startTimer() {
        tickerChannel = ticker(delayMillis = 1000, initialDelayMillis = 0)
        launch {
            for (event in tickerChannel) {
                CoroutineScope(Dispatchers.Main).launch {
                    val time = DateTimeUtils.displayDurationHourMinSec(totalTimeLeft)
                    binding.txtTimer.text = time
                }
                totalTimeLeft--
                if (totalTimeLeft == 0L){
                    CoroutineScope(Dispatchers.Main).launch {
                        openCompleteDialog()
                    }
                    break
                }
            }
            tickerChannel.cancel()
        }
    }

    private fun openCompleteDialog() {
        PlaySound.play(requireContext(), PlaySound.number_puzzle_win)
        binding.txtNext.isEnabled = false
        binding.txtTimer.text = "00:00"
        newInterstitialAdCompleteExercise()
    }

    override fun onPause() {
        super.onPause()
        tickerChannel.cancel()
    }

    override fun onResume() {
        super.onResume()
        continueExercise()
    }

    private fun continueExercise() {
        if (binding.linearExerciseAddSub.isVisible){
            startTimer()
        }
    }

    private fun setQuestions() {
        if (listExerciseAdditionSubtraction.lastIndex >= exercisePosition){
            binding.txtNext.isEnabled = true
            valuesAnswer = listExerciseAdditionSubtraction[exercisePosition].answer
            binding.txtQueLabel.text = "Q".plus((exercisePosition+1))

            if (currentParentData?.id == "1"){
                val question = listExerciseAdditionSubtraction[exercisePosition].question.replace("+"," +").replace("-"," -")
                val list = question.split(" ")
                listExerciseAdditionSubtractionQuestion.clear()
                list.map {
                    listExerciseAdditionSubtractionQuestion.add(it)
                }
                exerciseAdditionSubtractionAdapter.currentStep = 0
                exerciseAdditionSubtractionAdapter.notifyDataSetChanged()
            }else if (currentParentData?.id == "2"){
                binding.txtMultiplication.text = listExerciseAdditionSubtraction[exercisePosition].question.replace("x"," x ").plus(" = ?")
            }else{
                binding.txtMultiplication.text = listExerciseAdditionSubtraction[exercisePosition].question.replace("/"," ÷ ").plus(" = ?")
            }

        }
    }

    private fun resetClick() {
        if (!isResetRunning) {
            AbacusMasterSound.playResetSound(requireContext())
            isResetRunning = true
//            abacusBinding?.ivReset?.y = 0f
            abacusBinding?.ivReset?.animate()?.setDuration(200)
                ?.translationYBy((abacusBinding?.ivReset?.height!! / 2).toFloat())?.withEndAction {
                    abacusBinding?.ivReset?.animate()?.setDuration(200)
                        ?.translationYBy((-abacusBinding?.ivReset?.height!! / 2).toFloat())?.withEndAction {
                            isResetRunning = false
                        }?.start()
                }?.start()
            if (!binding.linearExerciseAddSub.isVisible){
                onAbacusValueDotReset()
            }
        }
    }

    private fun setAbacus() {
        binding.linearAbacus.removeAllViews()
        abacusBinding = FragmentAbacusExerciseBinding.inflate(layoutInflater, null, false)
        binding.linearAbacus.addView(abacusBinding?.root)

//        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" // local
//            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" && // if yes in firebase
//            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
//                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")) {
//            abacusBinding?.imgKidTop?.setImageResource(R.drawable.ic_kids_top_small)
//            abacusBinding?.imgKidTopHand?.setImageResource(R.drawable.ic_kids_top_hand_small)
//        }else{
//            abacusBinding?.imgKidTop?.setImageResource(R.drawable.ic_kids_top)
//            abacusBinding?.imgKidTopHand?.setImageResource(R.drawable.ic_kids_top_hand)
//        }

        abacusBinding?.ivReset?.onClick {
            if (binding.linearExerciseAddSub.isVisible){
                binding.txtAllClear.performClick()
                resetClick()
            }else{
                if (abacusBinding?.tvCurrentVal?.text?.toString() != "0"){
                    resetClick()
                }
            }

        }

        themeContent?.abacusFrame135?.let { abacusBinding?.rlAbacusMain?.setBackgroundResource(it) }
        themeContent?.dividerColor1?.let { abacusBinding?.ivDivider?.setBackgroundColor(ContextCompat.getColor(requireContext(),it)) }
        themeContent?.resetBtnColor8?.let {
            abacusBinding?.ivReset?.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
            abacusBinding?.ivRight?.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
            abacusBinding?.ivLeft?.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
        }

        abacusBinding?.abacusTop?.setNoOfRowAndBeads(0, 7, 1,AbacusBeadType.Exercise)
        abacusBinding?.abacusBottom?.setNoOfRowAndBeads(0, 7, 4,AbacusBeadType.Exercise)

        abacusBinding?.abacusTop?.onBeadShiftListener = this
        abacusBinding?.abacusBottom?.onBeadShiftListener = this

        lifecycleScope.launch {
            delay(300)
            abacusBinding?.relAbacus?.show()
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
                currentSumVal = java.lang.Float.valueOf(strCurVal)
                onAbacusValueChange(abacusView, currentSumVal)
            }
        }
    }

    override fun onAbacusValueChange(abacusView: View, sum: Float) {
        lifecycleScope.launch {

            if (binding.linearExerciseAddSub.isVisible){
                val value = sum.toInt().toString()
                listKeyboardAnswer.clear()
                for (i in value.indices){
                    listKeyboardAnswer.add(value[i].toString())
                }
                binding.tvAnswer.text = sum.toInt().toString()
                abacusBinding?.tvCurrentVal?.text = sum.toInt().toString()
            }else{
                abacusBinding?.tvCurrentVal?.text = sum.toInt().toString()
            }
        }
//        with(prefManager){
//
//            if (!binding.linearLevel.isVisible){
//                if (sum.toInt() == valuesAnswer) {
//
//                }
//            }
//        }

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

    // exercise leave listener
    fun exerciseLeaveAlert() {
        if (binding.linearExerciseAddSub.isVisible){
            CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.leave_exercise_alert),getString(R.string.leave_exercise_msg)
                ,getString(R.string.yes_i_m_sure),getString(R.string.no_please_continue), icon = R.drawable.ic_alert,
                clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                    override fun onConfirmationYesClick(bundle: Bundle?) {
                        goToMainView()
                    }
                    override fun onConfirmationNoClick(bundle: Bundle?) = Unit
                })
        }else{
            mNavController.navigateUp()
        }

    }

    private fun goToMainView() {
        binding.linearExerciseAddSub.hide()
        binding.linearTime.hide()
        binding.linearLevel.show()
        binding.imgBack.show()
        tickerChannel.cancel()
    }

    private fun newInterstitialAdCompleteExercise() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" &&
            prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" &&
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" && // purchase not
                    prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")
        ){
            showLoading()
            val isAdmob = prefManager.getCustomParamBoolean(AppConstants.AbacusProgress.isAdmob,true)
            val adUnit = getString(R.string.interstitial_ad_unit_id_exercise)
            if (isAdmob){
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(requireContext(), adUnit, adRequest, object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        hideLoading()
                        showCompleteDialog()
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        hideLoading()
                        // Show the ad if it's ready. Otherwise toast and reload the ad.
                        interstitialAd.show(requireActivity())
                        lifecycleScope.launch {
                            delay(400)
                            showCompleteDialog()
                        }
                    }
                })
            }else{
                val adRequest = AdManagerAdRequest.Builder().build()
                AdManagerInterstitialAd.load(requireContext(),adUnit, adRequest, object : AdManagerInterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        hideLoading()
                        showCompleteDialog()
                    }

                    override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                        hideLoading()
                        // Show the ad if it's ready. Otherwise toast and reload the ad.
                        interstitialAd.show(requireActivity())
                        lifecycleScope.launch {
                            delay(400)
                            showCompleteDialog()
                        }
                    }
                })
            }

        }else{
            showCompleteDialog()
        }

    }

    private fun showCompleteDialog() {
        if (ExerciseCompleteDialog.alertdialog?.isShowing != true){
            ExerciseCompleteDialog.showPopup(requireContext(),listExerciseAdditionSubtraction,prefManager,currentParentData,currentChildData,this@ExerciseHomeFragment)
        }
    }

    private fun notOfflineSupportDialog() {
        tickerChannel.cancel()
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.no_internet_working),getString(R.string.for_offline_support_msg)
            ,getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_sad_emoji,isCancelable = false,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    goToPurchase()
                }
                override fun onConfirmationNoClick(bundle: Bundle?){
                   if (requireContext().isNetworkAvailable){
                       continueExercise()
                   } else{
                       mNavController.navigateUp()
                   }
                }
            })
    }

    private fun goToPurchase() {
        mNavController.navigate(R.id.action_exerciseHomeFragment_to_purchaseFragment)
    }
}