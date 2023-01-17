package com.jigar.me.ui.view.dashboard.fragments.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.R
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.model.pages.*
import com.jigar.me.databinding.FragmentPageBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.AdditionSubtractionPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.DivisionPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.MultiplicationPageListAdapter
import com.jigar.me.ui.view.dashboard.fragments.page.adapter.SingleDigitPageListAdapter
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Resource
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.openYoutube
import com.jigar.me.utils.extensions.toastL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PageFragment : BaseFragment(), SingleDigitPageListAdapter.OnItemClickListener,
    MultiplicationPageListAdapter.OnItemClickListener, DivisionPageListAdapter.OnItemClickListener,
    AdditionSubtractionPageListAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPageBinding
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
        binding = FragmentPageBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
    }

    private fun initViews() {
        from = PageFragmentArgs.fromBundle(requireArguments()).from
        title = PageFragmentArgs.fromBundle(requireArguments()).title

        binding.title = resources.getString(R.string.lessons_of) + " " + title
        if (requireContext().isNetworkAvailable) {
            if (from == AppConstants.HomeClicks.Menu_Number) {
                fillSingleDigitPages()
            } else if (from == AppConstants.HomeClicks.Menu_Multiplication) {
                fillMultiplicationPages()
            } else if (from == AppConstants.HomeClicks.Menu_Division) {
                fillDivisionPages()
            } else if (from == AppConstants.HomeClicks.Menu_Addition) {
                if (listAdditionSubtractionsPages.isEmpty()) {
                    if (prefManager.getCustomParam(AppConstants.Extras_Comman.Level + from, "").isEmpty()) {
                        apiViewModel.getPages(from)
                    } else {
                        val type = object : TypeToken<List<AdditionSubtractionCategory>>() {}.type
                        val listTemp: List<AdditionSubtractionCategory> =
                            Gson().fromJson(prefManager.getCustomParam(AppConstants.Extras_Comman.Level + from, ""),type)
                        setAdditionSubtractionPages(listTemp)
                    }
                } else {
                    setAdditionSubtractionPages(listAdditionSubtractionsPages)
                }

            } else if (from == AppConstants.HomeClicks.Menu_Addition_Subtraction) {
                if (listAdditionSubtractionsPages.isEmpty()) {
                    if (prefManager.getCustomParam(AppConstants.Extras_Comman.Level + from, "").isEmpty()) {
                        apiViewModel.getPages(from)
                    } else {
                        val type = object : TypeToken<List<AdditionSubtractionCategory>>() {}.type
                        val listTemp: List<AdditionSubtractionCategory> =
                            Gson().fromJson(prefManager.getCustomParam(AppConstants.Extras_Comman.Level + from, ""),type)
                        setAdditionSubtractionPages(listTemp)
                    }
                } else {
                    setAdditionSubtractionPages(listAdditionSubtractionsPages)
                }

            }
        } else {
            requireContext().toastL(getString(R.string.no_internet))
            onBack()
        }
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
        apiViewModel.getPagesResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
//                    CustomViews.startButtonLoading(requireActivity())
                }
                is Resource.Success -> {
//                    CustomViews.hideButtonLoading()
                    it.value.content?.also {
                        val list: ArrayList<AdditionSubtractionCategory> = Gson().fromJson(
                            it.asJsonArray, object : TypeToken<ArrayList<AdditionSubtractionCategory>>() {}.type)
                        prefManager.setCustomParam(AppConstants.Extras_Comman.Level + from, Gson().toJson(list))
                        setAdditionSubtractionPages(list)
                    }
                }
                is Resource.Failure -> {
//                    CustomViews.hideButtonLoading()
                    it.errorBody?.let { it1 -> requireContext().toastL(it1) }
                }
                else -> {}
            }
        }
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
        bundle.putInt(AppConstants.apiParams.total,data.total_abacus?.toInt()?:0)
        mNavController.navigate(R.id.action_pageFragment_to_halfAbacusFragment, bundle)
    }
}