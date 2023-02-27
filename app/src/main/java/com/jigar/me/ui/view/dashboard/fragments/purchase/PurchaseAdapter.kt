package com.jigar.me.ui.view.dashboard.fragments.purchase

import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.google.gson.Gson
import com.jigar.me.R
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.databinding.RawPagelistChildBinding
import com.jigar.me.databinding.RawPurchaseBinding
import com.jigar.me.ui.view.base.inapp.BillingRepository
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_All_lifetime
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_Subscription_Month1
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_Subscription_Month3
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_Subscription_Weekly
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_level1_lifetime
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_level2_lifetime
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_material_maths
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_material_nursery
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.show

class PurchaseAdapter(
    private var listData: List<InAppSkuDetails>,
    private val prefManager : AppPreferencesHelper,
    private val mListener: OnItemClickListener
) :
    RecyclerView.Adapter<PurchaseAdapter.FormViewHolder>() {
    interface OnItemClickListener {
        fun onPurchaseItemClick(position: Int)
    }

    fun setData(listData: List<InAppSkuDetails>) {
        this.listData = listData
        notifyItemRangeChanged(0,listData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val binding = RawPurchaseBinding.inflate(parent.context.layoutInflater,parent,false)
        return FormViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val data: InAppSkuDetails = listData[position]
        with(holder){
            val context = binding.cardMain.context
            binding.sku = data
            val colorList = DataProvider.getColorsList()
            val colorPosition = (position % colorList.size)
            binding.btnRecommended.show()
            binding.spaceTop.show()

            binding.txtDiscount.hide()
            binding.txtOriginalPrice.hide()
            if (data.type == BillingClient.ProductType.SUBS){
                binding.txtPrice.text = data.price
                if (!data.originalPrice.isNullOrEmpty()){
                    binding.txtOriginalPrice.text = data.originalPrice
                    binding.txtOriginalPrice.paintFlags = binding.txtOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.txtOriginalPrice.show()
                }
            }else{
                binding.txtPrice.text = data.price
                when (data.sku) {
                    PRODUCT_ID_All_lifetime -> {
                        val pricee = (data.price?:"").replace(".","")
                        var newString = ""
                        for(i in pricee.indices){
                            if (!pricee[i].isDigit()){
                                newString += pricee[i]
                            }
                        }
                        val discount = prefManager.getCustomParamInt(AppConstants.AbacusProgress.Discount,0)
                        if (discount > 0){
                            binding.txtOriginalPrice.paintFlags = binding.txtOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            val tempPer = 100 - discount
                            try {
                                binding.txtDiscount.show()
                                binding.txtOriginalPrice.show()
                                binding.txtDiscount.text = "$discount% OFF"
                                val discountedPrice : Long = if (data.price_amount_micros != null){
                                    data.price_amount_micros/1000000
                                }else{
                                    data.price_amount_micros?:0L
                                }
                                val originalPrice = 100 * discountedPrice / tempPer
                                binding.txtOriginalPrice.text = "$newString$originalPrice.00"

                            } catch (e: NumberFormatException) {
                            }
                        }else{
                            binding.txtDiscount.hide()
                            binding.txtOriginalPrice.hide()
                        }
                    }
                }
            }
            when (data.sku) {
                PRODUCT_ID_Subscription_Month3 -> {
                    binding.btnRecommended.text = context.getString(R.string.popular)
                }
                PRODUCT_ID_Subscription_Weekly-> {
                    binding.btnRecommended.text = context.getString(R.string.hot)
                }
                PRODUCT_ID_Subscription_Month1 -> {
                    binding.btnRecommended.text = context.getString(R.string.new_)
                }
                PRODUCT_ID_All_lifetime -> {
                    binding.btnRecommended.text = context.getString(R.string.recommended)
                }
                PRODUCT_ID_level2_lifetime -> {
                    binding.btnRecommended.text = context.getString(R.string.favourite)
                }
                PRODUCT_ID_level1_lifetime -> {
                    binding.btnRecommended.text = context.getString(R.string.hot)
                }
                PRODUCT_ID_material_nursery -> {
                    binding.btnRecommended.text = context.getString(R.string.hot)
                }
                else -> {
                    binding.btnRecommended.hide()
                    binding.spaceTop.hide()
                }
            }
            binding.btnRecommended.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,colorList[colorPosition].darkColor))
            binding.cardColor = ContextCompat.getColor(context,colorList[colorPosition].color)
        }

    }

    class FormViewHolder(
        itemBinding: RawPurchaseBinding,
        private val mListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RawPurchaseBinding = itemBinding

        init {
            this.binding.txtPurchase.setOnClickListener {
                this.mListener.onPurchaseItemClick(
                    layoutPosition
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}