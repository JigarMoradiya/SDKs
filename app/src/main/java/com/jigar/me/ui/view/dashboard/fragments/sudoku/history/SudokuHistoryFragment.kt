package com.jigar.me.ui.view.dashboard.fragments.sudoku.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jigar.me.R
import com.jigar.me.databinding.FragmentSudokuHistoryBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.dashboard.fragments.sudoku.history.adapter.SudokuHistoryTabLayoutAdapter
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.sudoku.SudukoConst
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SudokuHistoryFragment : BaseFragment(){
    private lateinit var tabsAdapter: SudokuHistoryTabLayoutAdapter

    private lateinit var binding: FragmentSudokuHistoryBinding
    internal lateinit var mNavController: NavController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSudokuHistoryBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initView()
        initListener()
        ads()
        return binding.root
    }

    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" &&
            prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" &&
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")
        ) {
            showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_sudoku))
        }
    }


    private fun initView() {
        tabsAdapter = SudokuHistoryTabLayoutAdapter(childFragmentManager, lifecycle)
        binding.pager.adapter = tabsAdapter
        binding.tabs.let {
            binding.pager.let { pager ->
                TabLayoutMediator(it, pager) { tab: TabLayout.Tab, position: Int ->
                    if (position == 1) {
                        tab.text =  SudukoConst.Level_6By6+" Sudoku"
                    }else if (position == 2) {
                        tab.text =  SudukoConst.Level_9By9+" Sudoku"
                    } else tab.text = SudukoConst.Level_4By4+" Sudoku"
                }.attach()
            }
        }
    }

    fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
    }

}