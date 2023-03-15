package com.jigar.me.ui.view.dashboard.fragments.home

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.jigar.me.R
import com.jigar.me.data.local.data.HomeBanner
import com.jigar.me.databinding.RawHomeBannerPagerBinding
import com.jigar.me.databinding.RawPurchaseBinding
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick

class BannerPagerAdapter(var listData: ArrayList<HomeBanner>,
                         private val mListener: OnItemClickListener
) : PagerAdapter() {
    interface OnItemClickListener {
        fun onBannerItemClick(data: HomeBanner)
    }


    fun addPurchaseBanner(data : HomeBanner){
        listData.filter { it.type == AppConstants.HomeBannerTypes.banner_purchase }.also {
            if (it.isNullOrEmpty()){
                listData.add(data)
                notifyDataSetChanged()
            }
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = RawHomeBannerPagerBinding.inflate(container.context.layoutInflater,container,false)
        with(listData[position]){
            if (this.bitmap != null){
                binding.img.setImageBitmap(this.bitmap)
            }else{
                this.image?.let { binding.img.setImageResource(it) }
            }
            binding.root.onClick {
                mListener.onBannerItemClick(this@with)
            }
        }
        container.addView(binding.root)
        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayoutCompat
    }

    override fun getCount(): Int {
        return listData.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayoutCompat)
    }
}