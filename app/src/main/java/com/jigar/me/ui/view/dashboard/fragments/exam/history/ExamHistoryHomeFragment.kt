package com.jigar.me.ui.view.dashboard.fragments.exam.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jigar.me.R
import com.jigar.me.databinding.FragmentExamHistoryBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamHistoryHomeFragment : BaseFragment() {

    private lateinit var binding: FragmentExamHistoryBinding
    private lateinit var tabsAdapter: ExamHistoryTabLayoutAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExamHistoryBinding.inflate(inflater, container, false)
        init()
        clickListener()
        ads()
        return binding.root
    }
    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" // local
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" && // if yes in firebase
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")) {
            showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_exam))
        }
    }


    private fun init() {
        tabsAdapter = ExamHistoryTabLayoutAdapter(childFragmentManager, lifecycle)
        binding.pager.adapter = tabsAdapter
        binding.tabs.let {
            binding.pager.let { pager ->
                TabLayoutMediator(it, pager) { tab: TabLayout.Tab, position: Int ->
                    if (position == 1) {
                        tab.text =  Constants.examLevelIntermediate+" Level"
                    }else if (position == 2) {
                        tab.text =  Constants.examLevelExpert+" Level"
                    } else tab.text = Constants.examLevelBeginner+" Level"
                }.attach()
            }
        }
    }

    private fun clickListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
    }

}