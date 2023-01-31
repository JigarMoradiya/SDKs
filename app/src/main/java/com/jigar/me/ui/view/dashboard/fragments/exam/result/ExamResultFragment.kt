package com.jigar.me.ui.view.dashboard.fragments.exam.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.R
import com.jigar.me.data.local.data.BeginnerExamPaper
import com.jigar.me.data.model.dbtable.exam.DailyExamData
import com.jigar.me.databinding.FragmentExamResultBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamResultFragment : BaseFragment() {

    private lateinit var binding: FragmentExamResultBinding

    private var examResult = ""
    private var listAbacus: List<DailyExamData> = ArrayList<DailyExamData>()
    private var listAbacusLevel1: List<BeginnerExamPaper> = ArrayList<BeginnerExamPaper>()

    private val dailyExamResultAdapter: ExamResultAdapter = ExamResultAdapter(arrayListOf())
    private val dailyExamResultLevel1Adapter: ExamResultLevel1Adapter = ExamResultLevel1Adapter(arrayListOf())
    private var examType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        examResult = requireArguments().getString(AppConstants.Extras_Comman.examResult, "")
        examType = requireArguments().getString(AppConstants.Extras_Comman.type,"")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExamResultBinding.inflate(inflater, container, false)
        init()
        clickListener()
        return binding.root
    }


    private fun init() {
        if (examType == Constants.examLevelBeginner){
            binding.recyclerviewResult.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerviewResult.adapter = dailyExamResultLevel1Adapter
            val type = object : TypeToken<List<BeginnerExamPaper>>() {}.type
            listAbacusLevel1 = Gson().fromJson(examResult, type)
            dailyExamResultLevel1Adapter.setData(listAbacusLevel1)
        }else{
            binding.recyclerviewResult.adapter = dailyExamResultAdapter
            val type = object : TypeToken<List<DailyExamData>>() {}.type
            listAbacus = Gson().fromJson(examResult, type)
            dailyExamResultAdapter.setData(listAbacus)
        }

        ads()
    }
    private fun clickListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
    }
    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" // local
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" && // if yes in firebase
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")) {
            newInterstitialAdRequest()
            showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_exam_result))
        }
    }

    // show leave ads
    private fun newInterstitialAdRequest() {
        val adRequest = AdRequest.Builder().build()
        showLoading()
        InterstitialAd.load(requireContext(),getString(R.string.interstitial_ad_unit_id_exam_complete_show_result), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                hideLoading()
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                hideLoading()
                interstitialAd.show(requireActivity())
            }
        })
    }



}