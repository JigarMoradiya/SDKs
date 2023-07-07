package com.jigar.me.ui.view.dashboard.fragments.page

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.R
import com.jigar.me.data.local.data.AbacusBeadType
import com.jigar.me.data.local.data.AbacusContent
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.model.pages.*
import com.jigar.me.databinding.FragmentPageBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.CategoryPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.PageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.PageListAdapterOld
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PageFragment : BaseFragment(), CategoryPageListAdapter.OnItemClickListener, PageListAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPageBinding
    private lateinit var mNavController: NavController
    private var root : View? = null

    private var from = 0
    private var listCategory: List<CategoryPages> = arrayListOf()
//    private lateinit var pageListAdapter: PageListAdapterOld

    private var themeContent : AbacusContent? = null
    private lateinit var categoryPageListAdapter: CategoryPageListAdapter
    private lateinit var pageListAdapter: PageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        if (root == null){
            binding = FragmentPageBinding.inflate(inflater, container, false)
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
        from = PageFragmentArgs.fromBundle(requireArguments()).from
        binding.title = PageFragmentArgs.fromBundle(requireArguments()).title

        val theme = prefManager.getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default)
        themeContent = DataProvider.findAbacusThemeType(requireContext(),theme, AbacusBeadType.Exercise)
        themeContent?.dividerColor1?.let{
            val finalColor = CommonUtils.mixTwoColors(ContextCompat.getColor(requireContext(),R.color.white), ContextCompat.getColor(requireContext(),it), 0.90f)
            binding.cardCategory.setCardBackgroundColor(ColorStateList.valueOf(finalColor))
        }

        if(requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
            getPages()
        } else {
            notOfflineSupportDialog()
        }
    }

    private fun getPages() {

        if (from == AppConstants.HomeClicks.Menu_Number) {
            if (listCategory.isEmpty()) {
                listCategory = DataProvider.getSingleDigitPages(requireContext())
            }
            fillCategory()
        } else if (from == AppConstants.HomeClicks.Menu_Multiplication) {
            binding.recyclerviewPage.layoutManager = GridLayoutManager(requireContext(),3)
            if (listCategory.isEmpty()) {
                listCategory = DataProvider.getMultiplicationPages(requireContext())
            }
            fillCategory()
        } else if (from == AppConstants.HomeClicks.Menu_Division) {
            if (listCategory.isEmpty()) {
                listCategory = DataProvider.getDivisionPages(requireContext())
            }
            fillCategory()
        } else if (from == AppConstants.HomeClicks.Menu_Addition || from == AppConstants.HomeClicks.Menu_Addition_Subtraction) {
            val pages = requireContext().readJsonAsset("pages.json")
            if (pages.isEmpty()){
                onBack()
            }else{
                val type = object : TypeToken<List<CategoryPages>>() {}.type
                val listTemp: List<CategoryPages> = Gson().fromJson(pages,type)
                listTemp.filter { it.level_id == from.toString()}.also {
                    if (it.isNotNullOrEmpty()){
                        listCategory = it
                        fillCategory()
                    }
                }
            }

        }
    }

    private fun fillCategory() {
        categoryPageListAdapter = CategoryPageListAdapter(listCategory, this,themeContent)
        binding.recyclerviewCategory.adapter = categoryPageListAdapter
        if (listCategory.isNotNullOrEmpty()){
            onCategoryItemClick(listCategory.first())
        }

    }

    private fun notOfflineSupportDialog() {
        CommonConfirmationBottomSheet.showPopup(requireActivity(),getString(R.string.no_internet_working),getString(R.string.for_offline_support_msg)
            ,getString(R.string.yes_i_want_to_purchase),getString(R.string.no_purchase_later), icon = R.drawable.ic_alert_sad_emoji,isCancelable = false,
            clickListener = object : CommonConfirmationBottomSheet.OnItemClickListener{
                override fun onConfirmationYesClick(bundle: Bundle?) {
                    binding.cardPurchase.performClick()
                }
                override fun onConfirmationNoClick(bundle: Bundle?){
                    if (requireContext().isNetworkAvailable){
                        getPages()
                    } else{
                        mNavController.navigateUp()
                    }
                }
            })
    }


    private fun initListener() {
        binding.cardBack.onClick { onBack() }
        binding.cardSettingTop.onClick { mNavController.navigate(R.id.action_pageFragment_to_settingsFragment) }
        binding.cardPurchase.onClick { mNavController.navigate(R.id.action_pageFragment_to_purchaseFragment) }
        binding.cardYoutube.onClick { requireContext().openYoutube() }
    }
    private fun onBack() {
        mNavController.navigateUp()
    }

    private fun initObserver() {
    }

    override fun onResume() {
        super.onResume()
        if (::pageListAdapter.isInitialized){
            pageListAdapter.notifyDataSetChanged()
        }
    }

    override fun onCategoryItemClick(data: CategoryPages) {
        pageListAdapter = PageListAdapter(data.pages, this,prefManager,from)
        binding.recyclerviewPage.adapter = pageListAdapter
    }

    override fun onPageItemClick(data: Pages,pageId : String) {
        val bundle = Bundle()
        with(bundle){
            when (from) {
                AppConstants.HomeClicks.Menu_Number -> {
                    putString(AppConstants.Extras_Comman.AbacusType,AppConstants.Extras_Comman.AbacusTypeNumber)
                    putInt(AppConstants.Extras_Comman.From, data.from)
                    putInt(AppConstants.Extras_Comman.To, data.to)
                    putBoolean(AppConstants.Extras_Comman.isType_random,data.type_random)
                    putString(AppConstants.apiParams.pageId, pageId)
                }
                AppConstants.HomeClicks.Menu_Multiplication -> {
                    putString(AppConstants.Extras_Comman.AbacusType,AppConstants.Extras_Comman.AbacusTypeMultiplication)
                    putString(AppConstants.Extras_Comman.Que2_str,data.que2_str)
                    putString(AppConstants.Extras_Comman.Que2_type,data.que2_type)
                    putInt(AppConstants.Extras_Comman.Que1_digit_type,data.que1_digit_type)
                    putString(AppConstants.apiParams.pageId, pageId)
                }
                AppConstants.HomeClicks.Menu_Division -> {
                    putString(AppConstants.Extras_Comman.AbacusType,AppConstants.Extras_Comman.AbacusTypeDivision)
                    putString(AppConstants.Extras_Comman.Que2_str, data.que2_str)
                    putString(AppConstants.Extras_Comman.Que2_type,data.que2_type)
                    putString(AppConstants.apiParams.pageId, pageId)
                }
                AppConstants.HomeClicks.Menu_Addition, AppConstants.HomeClicks.Menu_Addition_Subtraction -> {
                    putString(AppConstants.Extras_Comman.AbacusType,AppConstants.Extras_Comman.AbacusTypeAdditionSubtraction)
                    putString(AppConstants.apiParams.pageId,data.page_id)
                    putString(AppConstants.apiParams.hint,data.hint)
                    putString(AppConstants.apiParams.file,data.file)
                }
            }
            mNavController.navigate(R.id.action_pageFragment_to_halfAbacusFragment, this)
        }
    }
}