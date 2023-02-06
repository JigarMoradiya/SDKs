package com.jigar.me.ui.view.dashboard.fragments.page.adapter

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.model.pages.SingleDigitCategory
import com.jigar.me.data.model.pages.SingleDigitPages
import com.jigar.me.databinding.RawPagelistChildBinding
import com.jigar.me.databinding.RawPagelistParentBinding
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.CommonUtils.getCurrentSumFromPref
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick

class SingleDigitPageListAdapter(
    private var listData: ArrayList<SingleDigitCategory>,
    private val mListener: OnItemClickListener
) :
    RecyclerView.Adapter<SingleDigitPageListAdapter.FormViewHolder>() {
    interface OnItemClickListener {
        fun onSingleDigitItemClick(data: SingleDigitPages)
    }

    fun setData(listData: List<SingleDigitCategory>) {
        this.listData.clear()
        this.listData.addAll(listData)
        notifyItemRangeChanged(0,this.listData.size)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FormViewHolder {
        val binding = RawPagelistParentBinding.inflate(parent.context.layoutInflater,parent,false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val data: SingleDigitCategory = listData[position]
        holder.binding.title = data.category_name
        val childAdapter = SingleDigitPageListChildAdapter(data.pages,mListener)
        holder.binding.recyclerviewPage.adapter = childAdapter
    }

    class FormViewHolder(itemBinding: RawPagelistParentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RawPagelistParentBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class SingleDigitPageListChildAdapter(
        private var listData: List<SingleDigitPages>,
        private val mListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<SingleDigitPageListChildAdapter.FormViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
        ): FormViewHolder {
            val binding = RawPagelistChildBinding.inflate(parent.context.layoutInflater,parent,false)
            return FormViewHolder(binding)
        }

        override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
            val data: SingleDigitPages = listData[position]
            val context = holder.binding.txtTitle.context
            holder.binding.title = data.PageName
            holder.binding.desc = data.from.toString() + " - " + data.to

            if (data.PageName == null) {
                holder.binding.title = data.from.toString() + " " + context.resources.getString(R.string.to_) + " " + data.to+" "+context.resources.getString(R.string.numbers)
            } else {
                holder.binding.title = data.PageName
            }
            val pageId = "SingleDigit_Page ${data.id}"
            if (context.getCurrentSumFromPref(pageId) != null){
                CommonUtils.blinkView(holder.binding.imgContinueIndicator)
            }else{
                holder.binding.imgContinueIndicator.hide()
            }

            holder.binding.root.onClick {
                mListener.onSingleDigitItemClick(data)
            }
        }

        class FormViewHolder(itemBinding: RawPagelistChildBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            var binding: RawPagelistChildBinding = itemBinding
        }

        override fun getItemCount(): Int {
            return listData.size
        }

    }
}