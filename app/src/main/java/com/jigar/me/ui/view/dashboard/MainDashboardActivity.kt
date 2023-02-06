package com.jigar.me.ui.view.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.jigar.me.R
import com.jigar.me.data.model.dbtable.inapp.InAppPurchaseDetails
import com.jigar.me.databinding.ActivityMainDashboardBinding
import com.jigar.me.ui.view.base.BaseActivity
import com.jigar.me.ui.view.base.inapp.BillingRepository
import com.jigar.me.ui.view.dashboard.fragments.abacus.half.HalfAbacusFragment
import com.jigar.me.ui.view.dashboard.fragments.exam.doexam.ExamFragment
import com.jigar.me.ui.view.dashboard.fragments.exam.doexam.Level1ExamFragment
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.ui.viewmodel.InAppViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.hide
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
    }



    private fun initListener() {

    }
    fun setPurchase(listData: List<InAppPurchaseDetails>) {
        with(prefManager){
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
                    }
                }
            }
        }
    }
    private fun showToolbarTitle(id: Int) {
        when (id) {
            R.id.homeFragment -> {
                binding.toolbar.hide()
            }
            R.id.pageFragment -> {
                binding.toolbar.title = "Pages"
                showOnlyBackArrow()
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
            else -> {
                navigationUp()
            }
        }
    }
    private fun navigationUp() {
        navController.navigateUp()
    }
}