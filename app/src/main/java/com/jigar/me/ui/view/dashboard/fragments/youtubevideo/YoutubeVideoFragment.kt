package com.jigar.me.ui.view.dashboard.fragments.youtubevideo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.R
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.model.VideoData
import com.jigar.me.data.model.pages.*
import com.jigar.me.databinding.FragmentPageBinding
import com.jigar.me.databinding.FragmentVideoBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.AdditionSubtractionPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.DivisionPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.MultiplicationPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.SingleDigitPageListAdapter
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YoutubeVideoFragment : BaseFragment(), YoutubeVideoListAdapter.OnItemClickListener {
    private lateinit var binding: FragmentVideoBinding
    private lateinit var mNavController: NavController
    private var root : View? = null

    private lateinit var singleDigitPageListAdapter: YoutubeVideoListAdapter
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        if (root == null){
            binding = FragmentVideoBinding.inflate(inflater, container, false)
            root = binding.root
            setNavigationGraph()
            initViews()
            initListener()
            bannerAds()
        }
        return root!!
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun initViews() {
        val type = object : TypeToken<ArrayList<VideoData>>() {}.type
        val videoList: List<VideoData> = Gson().fromJson(prefManager.getCustomParam(AppConstants.RemoteConfig.videoList,""),type)
        val sortedList = videoList.sortedWith { videoList1, videoList2 -> videoList1.so - videoList2.so }
        singleDigitPageListAdapter = YoutubeVideoListAdapter(sortedList,this)
        binding.recyclerview.adapter = singleDigitPageListAdapter
    }

    private fun initListener() {
        binding.cardBack.onClick { onBack() }
        binding.cardSettingTop.onClick { mNavController.navigate(R.id.action_youtubeVideoFragment_to_settingsFragment) }
        binding.cardPurchase.onClick { mNavController.navigate(R.id.action_youtubeVideoFragment_to_purchaseFragment) }
    }
    private fun onBack() {
        mNavController.navigateUp()
    }
    private fun bannerAds() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y"
            && prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y"
            && prefManager.getCustomParam(AppConstants.Purchase.Purchase_All, "") != "Y"
            && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y") { // if not purchased
            showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_abacus))
        }
    }
    override fun onVideoItemClick(data: VideoData) {
        requireContext().openYoutube("https://youtu.be/${data.id}")
    }
}