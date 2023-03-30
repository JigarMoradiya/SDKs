package com.jigar.me.ui.view.dashboard.fragments.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jigar.me.MyApplication
import com.jigar.me.R
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.databinding.FragmentPurchaseBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.PurchaseInfoBottomSheetDialog
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.ui.viewmodel.InAppViewModel
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.isNotNullOrEmpty
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.toastS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseFragment : BaseFragment(), PurchaseAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPurchaseBinding
    private val apiViewModel by viewModels<AppViewModel>()
    private val inAppViewModel by viewModels<InAppViewModel>()
    private var listSKU: MutableList<InAppSkuDetails> = arrayListOf()
    private lateinit var skuListAdapter: PurchaseAdapter
    private lateinit var mNavController: NavController
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initViews()
        initListener()
        return binding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    private fun initViews() {
        skuListAdapter = PurchaseAdapter(listSKU,prefManager, this)
        binding.recyclerview.adapter = skuListAdapter
        apiViewModel.getInAppSKU().observe(viewLifecycleOwner){
            if (listSKU.isNullOrEmpty()){
                setSKU(it)
            }
        }
        inAppViewModel.inAppInit()
    }

    private fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
        binding.cardInfo.onClick { PurchaseInfoBottomSheetDialog.showPopup(requireActivity()) }
    }

    private fun setSKU(listData: List<InAppSkuDetails>) {
        listSKU.clear()
        listSKU.addAll(listData)
        skuListAdapter.notifyItemRangeChanged(0,listSKU.size)
    }

    override fun onPurchaseItemClick(position: Int) {
        if (requireContext().isNetworkAvailable) {
            if (!listSKU[position].isPurchase) {
                // firebase event
                MyApplication.logEvent("Purchase_"+listSKU[position].sku, null)
                inAppViewModel.makePurchase(requireActivity(), listSKU[position])
            }
        }else{
            requireContext().toastS(getString(R.string.no_internet))
        }
    }
}