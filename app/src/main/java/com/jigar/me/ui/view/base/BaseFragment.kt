package com.jigar.me.ui.view.base

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jigar.me.R
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.show
import com.jigar.me.utils.extensions.toastS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {
    lateinit var prefManager : AppPreferencesHelper

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default


    //   TTS
    private var textToSpeech: TextToSpeech? = null
    private var speak = false
    override fun onCreate(savedInstanceState: Bundle?) {
        prefManager = AppPreferencesHelper(requireContext(), AppConstants.PREF_NAME)
//        requireContext().setLocale(prefManager.getCustomParam(Constants.appLanguage,"en"))
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtToSpeechInit()
    }

    fun showToast(id : Int){
        requireContext().toastS(getString(id))
    }
    fun showToast(msg : String){
        requireContext().toastS(msg)
    }


    fun showLoading() {
        if (progressDialog != null && progressDialog?.isShowing == false) {
            progressDialog?.show()
        } else {
            initProgressDialog()
            progressDialog?.show()
        }
    }

    fun hideLoading() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }

    private var progressDialog: AlertDialog? = null

    open fun initProgressDialog() {
        val inflater = layoutInflater
        val alertLayout: View = inflater.inflate(R.layout.dialog_loading, null)
        val builder1 = AlertDialog.Builder(requireContext())
        builder1.setView(alertLayout)
        builder1.setCancelable(true)
        progressDialog = builder1.create()
        progressDialog?.setCancelable(true)
        progressDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    // TODO Speech
    open fun txtToSpeechInit() {
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    requireContext().toastS(getString(R.string.language_not_supported))
                } else {
                    textToSpeech?.setPitch(1f)
                    textToSpeech?.setSpeechRate(0.9f)
                    speak = true
                }
            }
        }
    }


    open fun speakOut(txt: String) {
        if (speak) {
            requireActivity().runOnUiThread {
                if (textToSpeech != null){
                    textToSpeech?.speak(txt, TextToSpeech.QUEUE_FLUSH, null, null)
                }else{
                    txtToSpeechInit()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        if (textToSpeech != null) {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }
    // load Interstitial ads
    fun showAMFullScreenAds(adUnit: String, isShowAdsDirect : Boolean = false) {
        val isShowAd = if (isShowAdsDirect) true else{
            val count = prefManager.getCustomParamInt(AppConstants.Purchase.AdsShowCount, 0)
            if (count == 7) {
                true
            } else {
                val newCount = if (count > 8){
                    1
                }else{
                    count + 1
                }
                prefManager.setCustomParamInt(AppConstants.Purchase.AdsShowCount, newCount)
                false
            }
        }
        if (isShowAd){
            val isAdmob = prefManager.getCustomParamBoolean(AppConstants.AbacusProgress.isAdmob,true)
            if (isAdmob){
                newInterstitialAd(adUnit,true)
            }else{
                newAdxInterstitialAd(adUnit,true)
            }
        }

    }

    fun newInterstitialAd(adUnit: String,isAdsCountReset : Boolean = false) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),adUnit, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                // Show the ad if it's ready. Otherwise toast and reload the ad.
                interstitialAd.show(requireActivity())
                if (isAdsCountReset){
                    prefManager.setCustomParamInt(AppConstants.Purchase.AdsShowCount, 0)
                }
            }
        })
    }

    fun newAdxInterstitialAd(adUnit: String,isAdsCountReset : Boolean = false) {
        val adRequest = AdManagerAdRequest.Builder().build()
        AdManagerInterstitialAd.load(requireContext(),adUnit, adRequest, object : AdManagerInterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
            }

            override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                // Show the ad if it's ready. Otherwise toast and reload the ad.
                interstitialAd.show(requireActivity())
                if (isAdsCountReset){
                    prefManager.setCustomParamInt(AppConstants.Purchase.AdsShowCount, 0)
                }
            }
        })
    }
    // load banner ads
    fun showAMBannerAds(adViewLayout: LinearLayoutCompat, adUnit : String) {
        try {
            adViewLayout.gravity = Gravity.CENTER or Gravity.BOTTOM
//            adViewLayout.minimumHeight = resources.displayMetrics.heightPixels / 11
            // Request for Ads
            val adRequest = AdRequest.Builder().build()
            val listener: AdListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adViewLayout.show()
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    adViewLayout.hide()
                }
            }
            val isAdmob = prefManager.getCustomParamBoolean(AppConstants.AbacusProgress.isAdmob,true)
            val adView = if (isAdmob){
                AdView(requireContext())
            }else{
                AdManagerAdView(requireContext())
            }
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = adUnit
            if (adView.adUnitId != "NA") {
                adViewLayout.addView(adView)
                adView.loadAd(adRequest)

                adView.adListener = listener
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
//    fun showAMBannerAds1(adViewLayout: TemplateView, adUnit : String) {
//        Log.e("jigarLogs","showAMBannerAds welcome")
//
////        val adLoader = AdLoader.Builder(requireContext(), "ca-app-pub-3940256099942544/2247696110")
//        val adLoader = AdLoader.Builder(requireContext(), getString(R.string.native_ad_unit_id_test))
//            .forNativeAd { ad : NativeAd ->
//                // Show the ad.
//                Log.e("jigarLogs","unifiedNativeAd adChoicesInfo = "+Gson().toJson(ad.adChoicesInfo?.images))
//                Log.e("jigarLogs","unifiedNativeAd adChoicesInfo = "+Gson().toJson(ad.adChoicesInfo?.text))
//                Log.e("jigarLogs","unifiedNativeAd advertiser = "+Gson().toJson(ad.advertiser))
//                Log.e("jigarLogs","unifiedNativeAd price = "+Gson().toJson(ad.price))
//                Log.e("jigarLogs","unifiedNativeAd responseInfo = "+Gson().toJson(ad.responseInfo?.responseExtras))
//                Log.e("jigarLogs","unifiedNativeAd starRating = "+Gson().toJson(ad.starRating))
//                Log.e("jigarLogs","unifiedNativeAd store = "+Gson().toJson(ad.store))
//                Log.e("jigarLogs","unifiedNativeAd body = "+Gson().toJson(ad.body))
//                Log.e("jigarLogs","unifiedNativeAd callToAction = "+Gson().toJson(ad.callToAction))
//                Log.e("jigarLogs","unifiedNativeAd headline = "+Gson().toJson(ad.headline))
//                Log.e("jigarLogs","unifiedNativeAd images = "+Gson().toJson(ad.images))
//                Log.e("jigarLogs","unifiedNativeAd extras = "+Gson().toJson(ad.extras))
////                Log.e("jigarLogs","unifiedNativeAd mediaContent = "+Gson().toJson(ad.mediaContent))
//                Log.e("jigarLogs","unifiedNativeAd muteThisAdReasons = "+Gson().toJson(ad.muteThisAdReasons))
//                for (key in ad.responseInfo?.responseExtras?.keySet()!!) {
//                    Log.e("jigarLogs","unifiedNativeAd extras vallue = "+ad.responseInfo?.responseExtras!!.getString(key))
//
//                }
//                adViewLayout.setNativeAd(ad)
//            }
//            .withAdListener(object : AdListener() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    // Handle the failure by logging, altering the UI, and so on.
//                    Log.e("jigarLogs","onAdFailedToLoad = "+adError.message)
//                }
//            })
//            .withNativeAdOptions(
//                NativeAdOptions.Builder()
//                // Methods in the NativeAdOptions.Builder class can be
//                // used here to specify individual options settings.
//                .build())
//            .build()
//        adLoader.loadAd(AdRequest.Builder().build())
//
//    }
}