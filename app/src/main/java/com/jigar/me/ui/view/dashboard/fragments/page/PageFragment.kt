package com.jigar.me.ui.view.dashboard.fragments.page

import android.os.Bundle
import android.util.Log
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
import com.jigar.me.data.model.pages.*
import com.jigar.me.databinding.FragmentPageBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.AdditionSubtractionPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.DivisionPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.MultiplicationPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.SingleDigitPageListAdapter
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Resource
import com.jigar.me.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PageFragment : BaseFragment(), SingleDigitPageListAdapter.OnItemClickListener,
    MultiplicationPageListAdapter.OnItemClickListener, DivisionPageListAdapter.OnItemClickListener,
    AdditionSubtractionPageListAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPageBinding
    private lateinit var mNavController: NavController
    private var root : View? = null
    private val apiViewModel by viewModels<AppViewModel>()

    private var from = 0
    private var title = ""
    private var listSingleDigitPages: MutableList<SingleDigitCategory> = arrayListOf()
    private val singleDigitPageListAdapter: SingleDigitPageListAdapter =
        SingleDigitPageListAdapter(arrayListOf(), this)

    private var listMultiplicationPages: MutableList<MultiplicationCategory> = arrayListOf()
    private val multiplicationPageListAdapter: MultiplicationPageListAdapter =
        MultiplicationPageListAdapter(arrayListOf(), this)

    private var listDivisionPages: MutableList<DivisionCategory> = arrayListOf()
    private val divisionPageListAdapter: DivisionPageListAdapter =
        DivisionPageListAdapter(arrayListOf(), this)

    private var listAdditionSubtractionsPages: List<AdditionSubtractionCategory> = arrayListOf()
    private val additionSubtractionPageListAdapter: AdditionSubtractionPageListAdapter =
        AdditionSubtractionPageListAdapter(arrayListOf(), this)

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
        title = PageFragmentArgs.fromBundle(requireArguments()).title

        binding.title = resources.getString(R.string.lessons_of) + " " + title
        if(requireContext().isNetworkAvailable || prefManager.getCustomParamBoolean(AppConstants.Purchase.isOfflineSupport, false)){
            getPages()
        } else {
            notOfflineSupportDialog()
        }
    }

    private fun getPages() {
        if (from == AppConstants.HomeClicks.Menu_Number) {
            fillSingleDigitPages()
        } else if (from == AppConstants.HomeClicks.Menu_Multiplication) {
            fillMultiplicationPages()
        } else if (from == AppConstants.HomeClicks.Menu_Division) {
            fillDivisionPages()
        } else if (from == AppConstants.HomeClicks.Menu_Addition || from == AppConstants.HomeClicks.Menu_Addition_Subtraction) {
            val pages = requireContext().readJsonAsset("pages.json")
            if (pages.isEmpty()){
                onBack()
            }else{
                val type = object : TypeToken<List<AdditionSubtractionCategory>>() {}.type
                val listTemp: List<AdditionSubtractionCategory> = Gson().fromJson(pages,type)
                listTemp.filter { it.level_id == from.toString()}.also {
                    if (it.isNotNullOrEmpty()){
                        setAdditionSubtractionPages(it)
                    }
                }
            }

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

    private fun fillSingleDigitPages() {
        if (listSingleDigitPages.isEmpty()) {
            listSingleDigitPages = DataProvider.getSingleDigitPages()
        }
        binding.recyclerviewPage.adapter = singleDigitPageListAdapter
        singleDigitPageListAdapter.setData(listSingleDigitPages)
    }

    private fun fillMultiplicationPages() {
        if (listMultiplicationPages.isEmpty()) {
            listMultiplicationPages = DataProvider.getMultiplicationPages()
        }
        binding.recyclerviewPage.adapter = multiplicationPageListAdapter
        multiplicationPageListAdapter.setData(listMultiplicationPages)
    }

    private fun fillDivisionPages() {
        if (listDivisionPages.isEmpty()) {
            listDivisionPages = DataProvider.getDivisionPages()
        }
        binding.recyclerviewPage.adapter = divisionPageListAdapter
        divisionPageListAdapter.setData(listDivisionPages)
    }

    private fun setAdditionSubtractionPages(dataList: List<AdditionSubtractionCategory>) {
        binding.recyclerviewPage.adapter = additionSubtractionPageListAdapter
        listAdditionSubtractionsPages = dataList
        additionSubtractionPageListAdapter.setData(listAdditionSubtractionsPages)
    }

    override fun onSingleDigitItemClick(data: SingleDigitPages) {
        val bundle = Bundle()
        with(bundle) {
            putString(AppConstants.Extras_Comman.AbacusType,AppConstants.Extras_Comman.AbacusTypeNumber)
            putInt(AppConstants.Extras_Comman.From, data.from)
            putInt(AppConstants.Extras_Comman.To, data.to)
            putBoolean(AppConstants.Extras_Comman.isType_random,data.type_random)
            putString(AppConstants.apiParams.pageId, "SingleDigit_Page ${data.id}")
        }
        mNavController.navigate(R.id.action_pageFragment_to_halfAbacusFragment, bundle)
    }

    override fun onDivisionItemClick(data: DivisionPages) {
        val bundle = Bundle()
        with(bundle) {
            putString(AppConstants.Extras_Comman.AbacusType,AppConstants.Extras_Comman.AbacusTypeDivision)
            putString(AppConstants.Extras_Comman.Que2_str, data.que2_str)
            putString(AppConstants.Extras_Comman.Que2_type,data.que2_type)
            putString(AppConstants.apiParams.pageId, "Devide_Page ${data.id}")
        }
        mNavController.navigate(R.id.action_pageFragment_to_halfAbacusFragment, bundle)
    }

    override fun onMultiplicationItemClick(data: MultiplicationPages) {
        val bundle = Bundle()
        with(bundle) {
            putString(AppConstants.Extras_Comman.AbacusType,AppConstants.Extras_Comman.AbacusTypeMultiplication)
            putString(AppConstants.Extras_Comman.Que2_str,data.que2_str)
            putString(AppConstants.Extras_Comman.Que2_type,data.que2_type)
            putInt(AppConstants.Extras_Comman.Que1_digit_type,data.que1_digit_type)
            putString(AppConstants.apiParams.pageId, "Multilication_Page ${data.id}")
        }
        mNavController.navigate(R.id.action_pageFragment_to_halfAbacusFragment, bundle)
    }

    override fun onAdditionSubtractionItemClick(data: AdditionSubtractionPage) {
        val bundle = Bundle()
        bundle.putString(AppConstants.Extras_Comman.AbacusType,AppConstants.Extras_Comman.AbacusTypeAdditionSubtraction)
        bundle.putString(AppConstants.apiParams.pageId,data.page_id)
        bundle.putString(AppConstants.apiParams.hint,data.hint)
        bundle.putString(AppConstants.apiParams.file,data.file)
        mNavController.navigate(R.id.action_pageFragment_to_halfAbacusFragment, bundle)
    }
}