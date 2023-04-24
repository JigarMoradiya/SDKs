package com.jigar.me.ui.view.dashboard

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.jigar.me.BuildConfig
import com.jigar.me.MyApplication
import com.jigar.me.R
import com.jigar.me.data.model.dbtable.inapp.InAppPurchaseDetails
import com.jigar.me.databinding.ActivityMainDashboardBinding
import com.jigar.me.ui.view.base.BaseActivity
import com.jigar.me.ui.view.base.inapp.BillingRepository
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.dashboard.fragments.abacus.half.HalfAbacusFragment
import com.jigar.me.ui.view.dashboard.fragments.exam.doexam.ExamFragment
import com.jigar.me.ui.view.dashboard.fragments.exam.doexam.Level1ExamFragment
import com.jigar.me.ui.view.dashboard.fragments.exercise.ExerciseHomeFragment
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.ui.viewmodel.InAppViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.checkPermissions
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.log
import com.jigar.me.utils.extensions.toastS
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainDashboardActivity : BaseActivity(){
    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment
    private var selectedFragment: Int = -1
    private val inAppViewModel by viewModels<InAppViewModel>()
    private val appViewModel by viewModels<AppViewModel>()
    private lateinit var binding: ActivityMainDashboardBinding
    companion object {
        @JvmStatic
        fun getInstance(context: Context?) {
            Intent(context, MainDashboardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()
        initViews()
        initListener()
        initObserver()
        if (!prefManager.getCustomParamBoolean(Constants.PREF_IS_REFERRAL_RECORDED,false)){
            recordReferral()
        }
    }

    private fun initObserver() {
        inAppViewModel.inAppInit()
        appViewModel.getInAppPurchase().observe(this){
            setPurchase(it)
        }
    }

    private fun setNavigationGraph() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedFragment = destination.id
            showToolbarTitle(destination.id)
        }
    }
    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun initViews() {
        setNavigationGraph()
        onMainActivityBack()
        this.checkPermissions(Constants.NOTIFICATION_PERMISSION,requestMultiplePermissions)

    }

    // permission result
    private var requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.filter { !it.value }.also{
            if (it.isEmpty()){
            }else{
                notificationPermissionPopup()
            }
        }
    }

    /**
     * Activity Result For Resume Result
     */
    private var resumeActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
            }
        }

    private fun notificationPermissionPopup() {
        CommonConfirmationBottomSheet.showPopup(this,getString(R.string.permission_alert),getString(R.string.notification_permission_msg)
            ,getString(R.string.okay),getString(R.string.give_later), icon = R.drawable.ic_alert,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    resumeActivityResultLauncher.launch(intent)
                }
                override fun onConfirmationNoClick(bundle: Bundle?) = Unit
            })
    }


    private fun initListener() {

    }
    private fun setPurchase(listData: List<InAppPurchaseDetails>) {
        with(prefManager){
            setCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)
            setCustomParam(AppConstants.Purchase.Purchase_All, "N")
            setCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1, "N")
            setCustomParam(AppConstants.Purchase.Purchase_Add_Sub_level2, "N")
            setCustomParam(AppConstants.Purchase.Purchase_Mul_Div_level3, "N")
            setCustomParam(AppConstants.Purchase.Purchase_Ads, "N")
            setCustomParam(AppConstants.Purchase.Purchase_Material_Maths, "N")
            setCustomParam(AppConstants.Purchase.Purchase_Material_Nursery, "N")

            if (listData.isNotEmpty()) {
                listData.map {
                    when (it.sku) {
                        BillingRepository.AbacusSku.PRODUCT_ID_material_maths -> {
                            setCustomParam(AppConstants.Purchase.Purchase_Material_Maths, "Y")
                        }
                        BillingRepository.AbacusSku.PRODUCT_ID_material_nursery -> {
                            setCustomParam(AppConstants.Purchase.Purchase_Material_Nursery, "Y")
                        }
                        BillingRepository.AbacusSku.PRODUCT_ID_All_lifetime, BillingRepository.AbacusSku.PRODUCT_ID_All_lifetime_old -> {
                            setCustomParam(AppConstants.Purchase.Purchase_All, "Y")
                            setCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, true)
                        }
                        BillingRepository.AbacusSku.PRODUCT_ID_level1_lifetime -> {
                            setCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"Y")
                        }
                        BillingRepository.AbacusSku.PRODUCT_ID_level2_lifetime -> {
                            setCustomParam(AppConstants.Purchase.Purchase_Add_Sub_level2, "Y")
                        }
                        BillingRepository.AbacusSku.PRODUCT_ID_level3_lifetime -> {
                            setCustomParam(AppConstants.Purchase.Purchase_Mul_Div_level3, "Y")
                        }
                        BillingRepository.AbacusSku.PRODUCT_ID_ads -> {
                            setCustomParam(AppConstants.Purchase.Purchase_Ads, "Y")
                        }
                        BillingRepository.AbacusSku.PRODUCT_ID_Subscription_Weekly_Test2,
                        BillingRepository.AbacusSku.PRODUCT_ID_Subscription_Weekly_Test1,
                        BillingRepository.AbacusSku.PRODUCT_ID_Subscription_Weekly,
                        BillingRepository.AbacusSku.PRODUCT_ID_Subscription_Month1,
                        BillingRepository.AbacusSku.PRODUCT_ID_Subscription_Month3 -> {
                            setCustomParam(AppConstants.Purchase.Purchase_Ads, "Y")
                            setCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"Y")
                            setCustomParam(AppConstants.Purchase.Purchase_Add_Sub_level2, "Y")
                            setCustomParam(AppConstants.Purchase.Purchase_Mul_Div_level3, "Y")
                        }
                    }
                }
            }

//            if (BuildConfig.DEBUG){
//                setCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)
//                setCustomParam(AppConstants.Purchase.Purchase_All, "N")
//                setCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1, "N")
//                setCustomParam(AppConstants.Purchase.Purchase_Add_Sub_level2, "N")
//                setCustomParam(AppConstants.Purchase.Purchase_Mul_Div_level3, "N")
//                setCustomParam(AppConstants.Purchase.Purchase_Ads, "N")
//                setCustomParam(AppConstants.Purchase.Purchase_Material_Maths, "N")
//                setCustomParam(AppConstants.Purchase.Purchase_Material_Nursery, "N")
//            }
        }
    }
    private fun showToolbarTitle(id: Int) {
        when (id) {
            R.id.homeFragment -> {
                binding.toolbar.hide()
            }
            else ->{
                binding.toolbar.hide()
            }
        }
    }
    private fun showOnlyBackArrow() { //  Hide bottom navigation bar, Show toolbar back icon
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }
    private fun onMainActivityBack() {
        onBackPressedDispatcher.addCallback(
            this, // lifecycle owner
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (selectedFragment) {
                        R.id.homeFragment -> {
                            finish()
                        }
                        else -> {
                            onBackOfHalfAbacusFragment()
                        }
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackOfHalfAbacusFragment()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onBackOfHalfAbacusFragment() {
        val fragment = navHostFragment.childFragmentManager.fragments[0]
        when (fragment) {
            is HalfAbacusFragment -> {
                fragment.onBackClick()
            }
            is Level1ExamFragment -> {
                fragment.examLeaveAlert()
            }
            is ExamFragment -> {
                fragment.examLeaveAlert()
            }
            is ExerciseHomeFragment -> {
                fragment.exerciseLeaveAlert()
            }
            else -> {
                navigationUp()
            }
        }
    }
    private fun navigationUp() {
        navController.navigateUp()
    }

    private fun recordReferral() {
        log("welcome referral")
        val referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                        val response: ReferrerDetails = referrerClient.installReferrer
                        val referrerUrl = response.installReferrer
//                        val referrerClickTime: Long = response.referrerClickTimestampSeconds*1000
//                        val appInstallTime: Long = response.installBeginTimestampSeconds*1000

                        log("referrerUrl : "+referrerUrl)
//                        if (referrerUrl.contains("utm_source=referral")){
                            referrerUrl.split("&").forEach { term ->
                                if (term.contains("=") && term.split("=").size>1){
                                    val param = term.split("=")[0]
                                    val value = term.split("=")[1]
                                    when(param){
                                        "utm_source" -> {
                                            log("Found utm_source in Installation and value is $value")
                                            MyApplication.logEvent(value, null)
                                            prefManager.setCustomParamBoolean(Constants.PREF_IS_REFERRAL_RECORDED,true)
                                        }
                                    }
                                }
                            }
//                        }else{
//                            prefManager.setCustomParamBoolean(Constants.PREF_IS_REFERRAL_RECORDED,true)
//                        }


                        referrerClient.endConnection()
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                        log("Feature Not Supported")
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                        log("Service is Unavailable.")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                log("Service is disconnected.")
            }
        })
    }
}