package com.jigar.me.ui.view.base

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jigar.me.R
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.show
import com.jigar.me.utils.extensions.toastS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {
    lateinit var prefManager : AppPreferencesHelper
    lateinit var mNavController: NavController

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default


    //   TTS
    private var textToSpeech: TextToSpeech? = null
    private var speak = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = AppPreferencesHelper(requireContext(), Constants.PREF_NAME)
        job = Job()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavigationGraph()
        txtToSpeechInit()
    }

    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
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
    fun showAMFullScreenAds(adUnit: String) {
        val count = prefManager.getCustomParamInt(AppConstants.Purchase.AdsShowCount, 0)
        if (count == 5) {
            newInterstitialAd(adUnit)
        } else {
            val newCount = count + 1
            prefManager.setCustomParamInt(AppConstants.Purchase.AdsShowCount, newCount)
        }
    }

    fun newInterstitialAd(adUnit: String) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),adUnit, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                showInterstitial(interstitialAd)
            }
        })
    }
    fun showInterstitial(interstitialAd: InterstitialAd) {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        interstitialAd.show(requireActivity())
        prefManager.setCustomParamInt(AppConstants.Purchase.AdsShowCount, 0)
    }
    // load banner ads
    fun showAMBannerAds(adViewLayout: LinearLayoutCompat, adUnit : String) {
        try {
            adViewLayout.gravity = Gravity.CENTER or Gravity.BOTTOM
            adViewLayout.minimumHeight = resources.displayMetrics.heightPixels / 11
            // Request for Ads
            val adRequest = AdRequest.Builder()
                .build()
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

            val adView = AdView(requireContext())
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
}