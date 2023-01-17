package com.jigar.me.ui.view.dashboard.fragments.purchase

import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.databinding.RawPagelistChildBinding
import com.jigar.me.databinding.RawPurchaseBinding
import com.jigar.me.ui.view.base.inapp.BillingRepository.AbacusSku.PRODUCT_ID_All_lifetime
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
            val color = when (data.sku) {
                PRODUCT_ID_All_lifetime -> {
                    binding.btnRecommended.show()
                    binding.spaceTop.show()
                    binding.btnRecommended.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.dark_bg_red))
                    binding.btnRecommended.text = context.getString(R.string.recommended)

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
                            binding.txtOriginalPrice.setText("$newString$originalPrice.00")

                        } catch (e: NumberFormatException) {
                        }
                    }else{
                        binding.txtDiscount.hide()
                        binding.txtOriginalPrice.hide()
                    }
                    ContextCompat.getColor(context,R.color.light_bg_red)
                }
                PRODUCT_ID_level2_lifetime -> {
                    binding.btnRecommended.show()
                    binding.spaceTop.show()
                    binding.btnRecommended.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.dark_bg_blue))
                    binding.btnRecommended.text = context.getString(R.string.favourite)
                    ContextCompat.getColor(context,R.color.light_bg_blue)
                }
                PRODUCT_ID_level1_lifetime -> {
                    binding.btnRecommended.show()
                    binding.spaceTop.show()
                    binding.btnRecommended.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.dark_bg_orange))
                    binding.btnRecommended.text = context.getString(R.string.hot)
                    ContextCompat.getColor(context,R.color.light_bg_orange)
                }
                PRODUCT_ID_material_nursery -> {
                    binding.btnRecommended.show()
                    binding.spaceTop.show()
                    binding.btnRecommended.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.dark_bg_purple))
                    binding.btnRecommended.text = context.getString(R.string.new_)
                    ContextCompat.getColor(context,R.color.light_bg_purple)
                }
                PRODUCT_ID_material_maths -> {
                    binding.btnRecommended.show()
                    binding.spaceTop.show()
                    binding.btnRecommended.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.dark_bg_tin))
                    binding.btnRecommended.text = context.getString(R.string.new_)
                    ContextCompat.getColor(context,R.color.light_bg_tin)
                }
                else -> {
                    binding.btnRecommended.hide()
                    binding.spaceTop.hide()
                    when (position) {
                        0 -> ContextCompat.getColor(context,R.color.light_bg_red)
                        1 -> ContextCompat.getColor(context,R.color.light_bg_blue)
                        2 -> ContextCompat.getColor(context,R.color.light_bg_orange)
                        3 -> ContextCompat.getColor(context,R.color.light_bg_pink)
                        4 -> ContextCompat.getColor(context,R.color.light_bg_green)
                        5 -> ContextCompat.getColor(context,R.color.light_bg_purple)
                        6 -> ContextCompat.getColor(context,R.color.light_bg_tin)
                        else -> ContextCompat.getColor(context,R.color.light_bg_other)
                    }
                }
            }
            binding.cardColor = color
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