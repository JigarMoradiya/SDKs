package com.jigar.me.ui.view.dashboard.fragments.practise_material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.R
import com.jigar.me.data.model.ImageData
import com.jigar.me.data.model.DownloadMaterialData
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.databinding.FragmentMaterialDownloadBinding
import com.jigar.me.internal.service.download.PDFDownloadService
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.inapp.BillingRepository
import com.jigar.me.ui.view.confirm_alerts.dialogs.PurchaseMaterialDownloadAlertDialog
import com.jigar.me.ui.viewmodel.AppViewModel
import com.jigar.me.ui.viewmodel.InAppViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Resource
import com.jigar.me.utils.extensions.*
import com.stfalcon.frescoimageviewer.ImageViewer
import com.stfalcon.frescoimageviewer.ImageViewer.OnImageChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MaterialDownloadFragment :
    BaseFragment(),MaterialDownloadAdapter.OnItemClickListener,
    PurchaseMaterialDownloadAlertDialog.PurchaseMaterialAlertDialogInterface {
    private lateinit var binding: FragmentMaterialDownloadBinding
    private val inAppViewModel by viewModels<InAppViewModel>()
    private val appViewModel by viewModels<AppViewModel>()

    private var listDownloadMaterial: List<DownloadMaterialData> = ArrayList()
    private val materialDownloadAdapter: MaterialDownloadAdapter =
        MaterialDownloadAdapter(arrayListOf(), this)
    private lateinit var overlayView: ImageOverlayView
    private var listImages: List<ImageData> = ArrayList()

    private var parentPos = -1
    private var downloadPos = -1
    private var downloadType = ""
    private var isPurchase = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        downloadType = MaterialDownloadFragmentArgs.fromBundle(requireArguments()).downloadType
        initObserver()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMaterialDownloadBinding.inflate(inflater, container, false)
        initView()
        initListener()
        ads()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        isPurchase = false
        with(prefManager){
            if (downloadType == AppConstants.Extras_Comman.DownloadType_Nursery){
                if (getCustomParam(AppConstants.Purchase.Purchase_Material_Nursery, "") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_All, "") == "Y"){
                    isPurchase = true
                }
            }else if (downloadType == AppConstants.Extras_Comman.DownloadType_Maths){
                if (getCustomParam(AppConstants.Purchase.Purchase_Material_Maths, "") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_All, "") == "Y"){
                    isPurchase = true
                }
            }
        }
    }

    private fun initObserver() {
        appViewModel.getPracticeMaterialResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading()
                }
                is Resource.Success -> {
                    hideLoading()
                    if (it.value.status){
                        it.value.content?.also {
                            val list: ArrayList<DownloadMaterialData> = Gson().fromJson(
                                it.asJsonArray, object : TypeToken<ArrayList<DownloadMaterialData>>() {}.type)
                            prefManager.setCustomParam(AppConstants.Extras_Comman.DownloadType +"_"+ downloadType, Gson().toJson(list))
                            setDownloadMaterial()
                        }
                    }else{
                        onBack()
                    }

                }
                is Resource.Failure -> {
                    hideLoading()
                    it.errorBody?.let { it1 -> requireContext().toastL(it1) }
                    onBack()
                }
                else -> {}
            }
        }
    }


    private fun ads() {
        with(prefManager){
            if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" &&
                getCustomParam(AppConstants.AbacusProgress.Ads, "") == "Y" &&
                getCustomParam(AppConstants.Purchase.Purchase_Material_Nursery, "") != "Y" &&
                getCustomParam(AppConstants.Purchase.Purchase_Material_Maths, "") != "Y" &&
                getCustomParam(AppConstants.Purchase.Purchase_All, "") != "Y" &&
                getCustomParam(AppConstants.Purchase.Purchase_Ads, "") != "Y"
            ) {
                showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_practise_material))
            }
        }
    }

    private fun initView() {
        inAppViewModel.inAppInit()

        binding.recyclerviewMaterial.adapter = materialDownloadAdapter
        overlayView = ImageOverlayView(requireContext())
        if (downloadType == AppConstants.Extras_Comman.DownloadType_Nursery){
            binding.txtTitle.text = getString(R.string.nursery_class_material)
            binding.cardDownload.hide()
        }else{
            binding.txtTitle.text = getString(R.string.maths_material)
        }
        binding.txtStoragePath.text = HtmlCompat.fromHtml("<b>Material Storage Path : </b>"+context?.downloadFilePath(),HtmlCompat.FROM_HTML_MODE_COMPACT)

        if (requireContext().isNetworkAvailable) {
            if (prefManager.getCustomParam(AppConstants.Extras_Comman.DownloadType + "_" + downloadType,"").isEmpty()) {
                appViewModel.getPracticeMaterial(downloadType)
            } else {
                setDownloadMaterial()
            }
        } else {
            showToast(R.string.no_internet)
            onBack()
        }
    }

    private fun onBack() {
        mNavController.navigateUp()
    }

    private fun initListener() {
        binding.cardBack.onClick { onBack() }
        binding.cardDownload.onClick { onDownloadItemClick() }
    }

    private fun setDownloadMaterial() {
        val type = object : TypeToken<List<DownloadMaterialData>>() {}.type
        listDownloadMaterial = Gson().fromJson(
            prefManager.getCustomParam(AppConstants.Extras_Comman.DownloadType + "_" + downloadType, ""),
            type
        )
        materialDownloadAdapter.setData(listDownloadMaterial,downloadType)
    }


    override fun onItemClick(parentPos: Int, position: Int) {
        listImages = listDownloadMaterial[parentPos].imagesList
        this.parentPos = parentPos

        ImageViewer.Builder(requireContext(), listImages)
            .setStartPosition(position)
            .setFormatter(ImageViewer.Formatter { customImage: ImageData ->
                listDownloadMaterial[parentPos].imagePath + customImage.image
            })
//            .setImageChangeListener(getImageChangeListener())
//            .setOverlayView(overlayView)
            .show()
    }

    private fun fetchSKUDetail(data: InAppSkuDetails?) {
        if (data != null){
            inAppViewModel.makePurchase(requireActivity(), data)
        }
    }

    // main download
    private fun onDownloadItemClick() {
        downloadPos = -1
        if (isPurchase){
            if (requireContext().isNetworkAvailable) {
                if (listDownloadMaterial.isNotEmpty()){
                    val item = listDownloadMaterial[0]
                    item.groupName = getString(R.string.maths_material)
                    PDFDownloadService.startPDFDownload(requireContext(), listOf(item))
                }
            } else {
                showToast(R.string.no_internet)
            }

        } else {
            PurchaseMaterialDownloadAlertDialog.showPopup(requireActivity(), this)
        }
    }

    // click from adapter
    override fun onItemDownloadClick(position: Int) {

        if (requireContext().isNetworkAvailable) {
            this.downloadPos = position
            if (isPurchase){
                PDFDownloadService.startPDFDownload(requireContext(), listOf(listDownloadMaterial[downloadPos]))
            }else{
                appViewModel.getInAppSKUDetail(BillingRepository.AbacusSku.PRODUCT_ID_material_nursery).observe(viewLifecycleOwner){ list ->
                    if (list.isNotEmpty()){
                        fetchSKUDetail(list[0])
                    }
                }
            }
        } else {
            showToast(R.string.no_internet)
        }
    }

    private fun getImageChangeListener(): OnImageChangeListener {
        return OnImageChangeListener { position ->
            overlayView.setTitleText(listDownloadMaterial[parentPos].groupName)
            overlayView.setDescription(listImages[position].description)
        }
    }

    override fun onPurchaseClick() {
        if (requireContext().isNetworkAvailable) {
            val type = if (downloadType == AppConstants.Extras_Comman.DownloadType_Maths) {
                BillingRepository.AbacusSku.PRODUCT_ID_material_maths
            }else { // if (downloadType == AppConstants.Extras_Comman.DownloadType_Nursery)
                BillingRepository.AbacusSku.PRODUCT_ID_material_nursery
            }
            appViewModel.getInAppSKUDetail(type).observe(viewLifecycleOwner){ list ->
                if (list.isNotEmpty()){
                    fetchSKUDetail(list[0])
                }
            }

        } else {
            showToast(R.string.no_internet)
        }
    }

}