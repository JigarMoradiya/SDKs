package com.jigar.me.ui.view.dashboard.fragments.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.eftimoff.viewpagertransformers.DepthPageTransformer
import com.jigar.me.MyApplication
import com.jigar.me.R
import com.jigar.me.data.local.data.HomeBanner
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.databinding.FragmentHomeBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.inapp.BillingRepository
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment(), BannerPagerAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val appViewModel by viewModels<AppViewModel>()

    private lateinit var bannerPagerAdapter: BannerPagerAdapter
    //handler for run auto scroll thread
    private var handler : Handler? = null
    private var runnable: Runnable? = null

    private var currentPage = 0
    private var timer: Timer? = null
    private val DELAY_MS: Long = 5000 //delay in milliseconds before task is to be executed
    private val PERIOD_MS: Long = 5000 // time in milliseconds between successive task executions.

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
    }
    private fun initViews() {
        setViewPager()
    }

    private fun setViewPager() {
        handler = Handler(Looper.getMainLooper())
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int,positionOffset: Float,positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                currentPage = position
            }
        })
        val bannerListData: ArrayList<HomeBanner> = arrayListOf()
        bannerListData.add(HomeBanner(AppConstants.HomeBannerTypes.banner_rate_us,R.drawable.banner_rate))
        bannerListData.add(HomeBanner(AppConstants.HomeBannerTypes.banner_share,R.drawable.banner_share))
        bannerPagerAdapter = BannerPagerAdapter(bannerListData,this@HomeFragment)
        binding.viewPager.adapter = bannerPagerAdapter
        binding.viewPager.setPageTransformer( true , DepthPageTransformer() )
        binding.indicatorPager.attachToPager(binding.viewPager)
        checkPurchase()
    }

    private fun initListener() {
        binding.cardAboutUs.onClick { moveToClick(AppConstants.HomeClicks.Menu_AboutUs) }
        binding.cardSettingTop.onClick { moveToClick(AppConstants.HomeClicks.Menu_Setting) }
        binding.cardYoutube.onClick { moveToClick(AppConstants.HomeClicks.Menu_Click_Youtube) }
        binding.cardPurchase.onClick { moveToClick(AppConstants.HomeClicks.Menu_Purchase) }

        binding.cardAbacusStarter.onClick { moveToClick(AppConstants.HomeClicks.Menu_Starter) }
        binding.cardAbacusNumber.onClick { moveToClick(AppConstants.HomeClicks.Menu_Number) }
        binding.cardAddition.onClick { moveToClick(AppConstants.HomeClicks.Menu_Addition) }
        binding.cardSubtractions.onClick { moveToClick(AppConstants.HomeClicks.Menu_Addition_Subtraction) }
        binding.cardMultiplication.onClick { moveToClick(AppConstants.HomeClicks.Menu_Multiplication) }

        binding.cardDivision.onClick { moveToClick(AppConstants.HomeClicks.Menu_Division) }
        binding.cardPracticeMaterial.onClick { moveToClick(AppConstants.HomeClicks.Menu_PractiseMaterial) }
        binding.cardExam.onClick { moveToClick(AppConstants.HomeClicks.Menu_DailyExam) }
        binding.cardSudoku.onClick { moveToClick(AppConstants.HomeClicks.Menu_Sudoku) }
        binding.cardNumberSequence.onClick { moveToClick(AppConstants.HomeClicks.Menu_Number_Puzzle) }
    }
    private fun checkPurchase() {
        appViewModel.getInAppPurchase().observe(viewLifecycleOwner) { listData ->
            if (listData.isNullOrEmpty()) {
                if (::bannerPagerAdapter.isInitialized){
                    bannerPagerAdapter.addPurchaseBanner(HomeBanner(AppConstants.HomeBannerTypes.banner_purchase,R.drawable.banner_purchase))
                }
            }
            if (prefManager.getCustomParamInt(AppConstants.AbacusProgress.Discount,0)> 0){
                appViewModel.getInAppSKUDetailLive(BillingRepository.AbacusSku.PRODUCT_ID_All_lifetime)
                    .observe(viewLifecycleOwner){ data ->
                        if (data.isNullOrEmpty()){
                            fetchSKUDetail(null)
                        }else{
                            fetchSKUDetail(data[0])
                        }
                    }
            }
        }
    }
    private fun fetchSKUDetail(data: InAppSkuDetails?) {
        if (data == null || !data.isPurchase){
            val baseUrl = prefManager.getBaseUrl()
            if (baseUrl.isNotEmpty()){
                val url = baseUrl.replace("https://","").split("/")
                if (url.isNotNullOrEmpty()){
                    val imageUrl = "https://"+url[0]+"/"+AppConstants.AbacusProgress.OfferBannerValue
                    Glide.with(requireContext()).asBitmap().load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onLoadCleared(placeholder: Drawable?) {}
                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                super.onLoadFailed(errorDrawable)
                            }
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                            ) {
                                if (::bannerPagerAdapter.isInitialized){
                                    bannerPagerAdapter.addOfferBanner(HomeBanner(AppConstants.HomeBannerTypes.banner_offer,R.drawable.banner_purchase,resource))
                                }
                            }
                        })
                }
            }
        }
    }
    private fun autoScrollBanner() {
        /*After setting the adapter use the timer */
        runnable?.let { handler?.removeCallbacks(it) }
        runnable = Runnable {
            currentPage++
            if (currentPage == (bannerPagerAdapter.listData.size)) {
                currentPage = 0
            }
            if (currentPage == 0){
                binding.viewPager.setCurrentItem(currentPage, false)
            }else{
                binding.viewPager.setCurrentItem(currentPage, true)
            }
        }

        timer = Timer() // This will create a new Thread
        timer?.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                runnable?.let { handler?.post(it) }
            }
        }, DELAY_MS, PERIOD_MS)
    }

    override fun onBannerItemClick(data: HomeBanner) {
        // firebase event
        MyApplication.logEvent(data.type, null)
        when (data.type) {
            AppConstants.HomeBannerTypes.banner_rate_us -> {
                requireContext().openPlayStore()
            }
            AppConstants.HomeBannerTypes.banner_share -> {
                moveToClick(AppConstants.HomeClicks.Menu_Share)
            }
            AppConstants.HomeBannerTypes.banner_purchase, AppConstants.HomeBannerTypes.banner_offer -> {
                moveToClick(AppConstants.HomeClicks.Menu_Purchase)
            }
        }
    }

    private fun moveToClick(clickType: Int) {
        when (clickType) {
            AppConstants.HomeClicks.Menu_Starter -> {
                mNavController.navigate(R.id.action_homeFragment_to_fullAbacusFragment)
            }
            AppConstants.HomeClicks.Menu_Number -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.Number))
                mNavController.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Addition -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.Addition))
                mNavController.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Addition_Subtraction -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.AdditionSubtraction))
                mNavController.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Multiplication -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.Multiplication))
                mNavController.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Division -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.Division))
                mNavController.navigate(action)
            }
            AppConstants.HomeClicks.Menu_DailyExam -> {
                mNavController.navigate(R.id.action_homeFragment_to_examHomeFragment)
            }
            AppConstants.HomeClicks.Menu_PractiseMaterial -> {
                mNavController.navigate(R.id.action_homeFragment_to_materialHomeFragment)
            }
            AppConstants.HomeClicks.Menu_Sudoku -> {
                mNavController.navigate(R.id.action_homeFragment_to_sudokuHomeFragment)
            }
            AppConstants.HomeClicks.Menu_Number_Puzzle -> {
                mNavController.navigate(R.id.action_homeFragment_to_puzzleNumberHomeFragment)
            }
            AppConstants.HomeClicks.Menu_Setting -> {
                mNavController.navigate(R.id.action_homeFragment_to_settingsFragment)
            }
            AppConstants.HomeClicks.Menu_Purchase -> {
                mNavController.navigate(R.id.action_homeFragment_to_purchaseFragment)
            }
            AppConstants.HomeClicks.Menu_AboutUs -> {
                mNavController.navigate(R.id.action_homeFragment_to_aboutFragment)
            }
            AppConstants.HomeClicks.Menu_Share -> {
                val msg = "Abacus child learning application!\n" +
                        "Abacus will help your children learn about Abacus, Numbers, Addition, Subtraction, Multiplication, Division.\n\n" +
                        "Download Abacus App from Google Playstore \uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDE0D\uD83D\uDE07\n" +
                        "https://play.google.com/store/apps/details?id=${requireContext().packageName}"
                requireContext().shareIntent(msg)
            }
            AppConstants.HomeClicks.Menu_Click_Youtube -> {
                requireContext().openYoutube()
            }
        }
    }
    override fun onPause() {
        super.onPause()
        timer?.cancel()
        runnable?.let{ handler?.removeCallbacks(it) }
    }

    override fun onResume() {
        super.onResume()
        if (::bannerPagerAdapter.isInitialized){
            autoScrollBanner()
        }
    }


}