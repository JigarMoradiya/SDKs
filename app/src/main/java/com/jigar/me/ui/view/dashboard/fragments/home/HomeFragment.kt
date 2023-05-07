package com.jigar.me.ui.view.dashboard.fragments.home

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.eftimoff.viewpagertransformers.DepthPageTransformer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.BuildConfig
import com.jigar.me.MyApplication
import com.jigar.me.R
import com.jigar.me.data.local.data.AvatarImages
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.local.data.HomeBanner
import com.jigar.me.data.local.data.HomeMenu
import com.jigar.me.data.model.PojoAbacus
import com.jigar.me.data.model.VideoData
import com.jigar.me.databinding.FragmentHomeBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.OtherApplicationBottomSheet
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.SelelctAvatarProfileDialog
import com.jigar.me.ui.view.confirm_alerts.dialogs.SelectThemeDialog
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.openURL
import com.jigar.me.utils.extensions.openYoutube
import com.jigar.me.utils.extensions.shareIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.samlss.lighter.IntroProvider
import me.samlss.lighter.Lighter
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment(), BannerPagerAdapter.OnItemClickListener,
    HomeMenuAdapter.OnItemClickListener, SelelctAvatarProfileDialog.AvatarProfileDialogInterface {
    private lateinit var binding: FragmentHomeBinding
    private var root : View? = null
    private val appViewModel by viewModels<AppViewModel>()
    private var mNavController: NavController? = null

    private lateinit var bannerPagerAdapter: BannerPagerAdapter
    private lateinit var homeMenuAdapter: HomeMenuAdapter
    //handler for run auto scroll thread
    private var handler : Handler? = null
    private var runnable: Runnable? = null

    private var currentPage = 0
    private var timer: Timer? = null
    private val DELAY_MS: Long = 5000 //delay in milliseconds before task is to be executed
    private val PERIOD_MS: Long = 5000 // time in milliseconds between successive task executions.

    private lateinit var mFirebaseRemoteConfig  : FirebaseRemoteConfig
    private var lighter : Lighter? = null
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        if (root == null){
            binding = FragmentHomeBinding.inflate(inflater, container, false)
            root = binding.root
            setNavigationGraph()
            initViews()
            initListener()
        }
        return root!!
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun initViews() {
        // set avatar profile and name
        avatarProfileCloseDialog()

        homeMenuAdapter = HomeMenuAdapter(DataProvider.getHomeMenuList(requireContext()),this)
        binding.recyclerviewMenu.adapter = homeMenuAdapter
        getTrackData()
        setViewPager()
        getFBConstant()
        firebaseConfig()

        if (prefManager.getCustomParamBoolean(AppConstants.Settings.isSetTheam, false)) {
            if (!prefManager.getCustomParamBoolean(AppConstants.Settings.isTourWatch, false)) {
                showTour()
            }
        }
    }

    private fun showTour() {
        lifecycleScope.launch {
            delay(1000)
            lighter = Lighter.with(binding.root)
            val freeModeViewHolder = binding.recyclerviewMenu.findViewHolderForAdapterPosition(0)
            val videoTutorialViewHolder = binding.recyclerviewMenu.findViewHolderForAdapterPosition(10)
            val exerciseViewHolder = binding.recyclerviewMenu.findViewHolderForAdapterPosition(6)
            val examViewHolder = binding.recyclerviewMenu.findViewHolderForAdapterPosition(7)
            val numberPuzzleViewHolder = binding.recyclerviewMenu.findViewHolderForAdapterPosition(9)
            if (freeModeViewHolder != null && exerciseViewHolder != null && examViewHolder != null && videoTutorialViewHolder != null && numberPuzzleViewHolder != null){
                IntroProvider.videoTutorialIntro(prefManager,lighter, (freeModeViewHolder as HomeMenuAdapter.FormViewHolder).binding.conMain,
                    (videoTutorialViewHolder as HomeMenuAdapter.FormViewHolder).binding.conMain,
                    (exerciseViewHolder as HomeMenuAdapter.FormViewHolder).binding.conMain,
                    (examViewHolder as HomeMenuAdapter.FormViewHolder).binding.conMain,
                    (numberPuzzleViewHolder as HomeMenuAdapter.FormViewHolder).binding.conMain
                )
            }
        }
    }

    override fun onItemHomeMenuClick(data: HomeMenu) {
        moveToClick(data.type)
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
        binding.cardEditImage.onClick {
            SelelctAvatarProfileDialog.showPopup(requireActivity(),prefManager,this@HomeFragment)
        }
        binding.cardSettingTop.onClick { moveToClick(AppConstants.HomeClicks.Menu_Setting) }
        binding.cardAboutUs.onClick { moveToClick(AppConstants.HomeClicks.Menu_AboutUs) }
        binding.txtOtherApps.onClick {
            OtherApplicationBottomSheet.showPopup(requireActivity())
        }

        binding.txtWelcomeTitle.onClick {
            if (BuildConfig.DEBUG) {
                showTour()
            }
        }
    }
    override fun avatarProfileCloseDialog() {
        val id = prefManager.getCustomParamInt(Constants.avatarId,0)
        if (id == 0){
            binding.txtWelcomeTitle.text = CommonUtils.getCurrentTimeMessage()
        }else{
            val avatarList = DataProvider.getAvatarList()
            val avatar : AvatarImages? = avatarList.find { it.id == id }
            if (avatar != null){
                binding.imgUserProfile.setImageResource(avatar.image)
                binding.txtWelcomeTitle.text = CommonUtils.getCurrentTimeMessage().plus(" "+prefManager.getCustomParam(Constants.childName,"")+"!")
            }else{
                binding.txtWelcomeTitle.text = CommonUtils.getCurrentTimeMessage()
            }

        }
    }
    private fun checkPurchase() {
        appViewModel.getInAppPurchase().observe(viewLifecycleOwner) { listData ->
            if (listData.isNullOrEmpty()) {
                if (::bannerPagerAdapter.isInitialized){
                    bannerPagerAdapter.addPurchaseBanner(HomeBanner(AppConstants.HomeBannerTypes.banner_purchase,R.drawable.banner_purchase))
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
                requireContext().openURL("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
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
                mNavController?.navigate(R.id.action_homeFragment_to_fullAbacusFragment)
            }
            AppConstants.HomeClicks.Menu_Number -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.Number))
                mNavController?.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Addition -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.Addition))
                mNavController?.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Addition_Subtraction -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.AdditionSubtraction))
                mNavController?.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Multiplication -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.Multiplication))
                mNavController?.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Division -> {
                val action = HomeFragmentDirections.actionHomeFragmentToPageFragment(clickType,resources.getString(R.string.Division))
                mNavController?.navigate(action)
            }
            AppConstants.HomeClicks.Menu_Exercise -> {
                mNavController?.navigate(R.id.action_homeFragment_to_exerciseHomeFragment)
            }
            AppConstants.HomeClicks.Menu_DailyExam -> {
                mNavController?.navigate(R.id.action_homeFragment_to_examHomeFragment)
            }
            AppConstants.HomeClicks.Menu_PractiseMaterial -> {
                mNavController?.navigate(R.id.action_homeFragment_to_materialHomeFragment)
            }
            AppConstants.HomeClicks.Menu_Number_Puzzle -> {
                mNavController?.navigate(R.id.action_homeFragment_to_puzzleNumberHomeFragment)
            }
            AppConstants.HomeClicks.Menu_Setting -> {
                mNavController?.navigate(R.id.action_homeFragment_to_settingsFragment)
            }
            AppConstants.HomeClicks.Menu_Purchase -> {
                mNavController?.navigate(R.id.action_homeFragment_to_purchaseFragment)
            }
            AppConstants.HomeClicks.Menu_AboutUs -> {
                mNavController?.navigate(R.id.action_homeFragment_to_aboutFragment)
            }
            AppConstants.HomeClicks.Menu_Share -> {
                requireContext().shareIntent()
            }
            AppConstants.HomeClicks.Menu_Click_Youtube -> {
                if (prefManager.getCustomParam(AppConstants.RemoteConfig.videoList,"").isEmpty()){
                    requireContext().openYoutube()
                }else{
                    mNavController?.navigate(R.id.action_homeFragment_to_youtubeVideoFragment)
                }
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

    private fun getTrackData() {
        FirebaseDatabase.getInstance().reference.child(
            AppConstants.AbacusProgress.Track + "/" + prefManager.getDeviceId()
        ).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                for (snapshotdata in snapshot.children) {
                    val pageId = snapshotdata.key!!
                    val mapMessage = snapshotdata.value as HashMap<*, *>
                    val position = mapMessage[AppConstants.AbacusProgress.Position] as Long
                    try {
                        val pageSum: String = prefManager.getCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM, "{}")
                        val objJson = JSONObject(pageSum)
                        objJson.put(pageId, (position + 1))
                        prefManager.setCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM,objJson.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                prefManager.setCustomParam(AppConstants.AbacusProgress.TrackFetch, "Y")
            }

            override fun onCancelled(@NonNull error: DatabaseError) {
            }
        })
    }

    private fun getFBConstant() {
        val databaseReference = FirebaseDatabase.getInstance().reference
            .child(AppConstants.AbacusProgress.Settings)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val mapMessage = dataSnapshot.value as HashMap<*, *>?
                    val versionCode = mapMessage!![AppConstants.AbacusProgress.versionCode] as Long
                    val resetImage = mapMessage[AppConstants.AbacusProgress.resetImage] as Long
                    val privacyPolicy = mapMessage[AppConstants.AbacusProgress.Privacy_Policy_data] as String
                    val baseUrl = mapMessage[AppConstants.AbacusProgress.BaseUrl] as String
                    val iPath = mapMessage[AppConstants.AbacusProgress.iPath] as String
                    val ads = mapMessage[AppConstants.AbacusProgress.Ads] as String
                    val isAdmob = if (BuildConfig.DEBUG){
                        false
                    }else{
                        mapMessage[AppConstants.AbacusProgress.isAdmob] as Boolean
                    }
                    with(prefManager){
                        setCustomParamBoolean(AppConstants.AbacusProgress.isAdmob,isAdmob)
                        setBaseUrl(baseUrl)
                        setCustomParam(AppConstants.AbacusProgress.iPath,iPath)

                        setCustomParam(AppConstants.AbacusProgress.Privacy_Policy_data,privacyPolicy)
                        setCustomParam(AppConstants.AbacusProgress.Ads,ads)

                        if (resetImage.toInt() > getCustomParamInt(AppConstants.AbacusProgress.resetImage, 0)) {
                            setCustomParamInt(AppConstants.AbacusProgress.resetImage, resetImage.toInt())
                            setCustomParam(AppConstants.Extras_Comman.DownloadType+"_"+AppConstants.Extras_Comman.DownloadType_Maths, "")
                            setCustomParam(AppConstants.Extras_Comman.DownloadType+"_"+AppConstants.Extras_Comman.DownloadType_Nursery, "")
                        }

                        checkVersion(versionCode)

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun checkVersion(versionCode: Long) {
        try {
            val pInfo =
                requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            val version = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                pInfo.longVersionCode
            }else{
                pInfo.versionCode.toLong()
            }

            if (versionCode > version){
                CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.app_update),getString(R.string.new_version_msg)
                    ,getString(R.string.yes_i_want_to_update),getString(R.string.no_thanks), icon = R.drawable.ic_alert,
                    clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                        override fun onConfirmationYesClick(bundle: Bundle?) {
                            requireContext().openURL("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
                        }
                        override fun onConfirmationNoClick(bundle: Bundle?){
                            requireActivity().finish()
                        }
                    })
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    private fun firebaseConfig() {
        var tries = 0
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(5)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(requireActivity()) { task ->
                tries++
                if (tries>3){
                    mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings.toBuilder().setFetchTimeoutInSeconds(20).build())
                }
                if (task.isSuccessful) {
                    val video: String = mFirebaseRemoteConfig.getString(AppConstants.RemoteConfig.videoList)
                    with(prefManager){
                        if (video.length > 5){
                            setCustomParam(AppConstants.RemoteConfig.videoList,video)
                        }else{
                            setCustomParam(AppConstants.RemoteConfig.videoList,"")
                        }

                    }
                }
            }
    }



}