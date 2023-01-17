package com.jigar.me.ui.view.dashboard.fragments.page.adapter

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.model.pages.MultiplicationCategory
import com.jigar.me.data.model.pages.MultiplicationPages
import com.jigar.me.databinding.RawPagelistChildBinding
import com.jigar.me.databinding.RawPagelistParentBinding
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick

class MultiplicationPageListAdapter(
    private var listData: ArrayList<MultiplicationCategory>,
    private val mListener: OnItemClickListener
) :
    RecyclerView.Adapter<MultiplicationPageListAdapter.FormViewHolder>() {
    interface OnItemClickListener {
        fun onMultiplicationItemClick(data: MultiplicationPages)
    }

    fun setData(listData: List<MultiplicationCategory>) {
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
        val data: MultiplicationCategory = listData[position]
        holder.binding.title = data.category_name
        val childAdapter = MultiplicationPageListChildAdapter(data.pages, mListener)
        holder.binding.recyclerviewPage.adapter = childAdapter
    }

    class FormViewHolder(
        itemBinding: RawPagelistParentBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RawPagelistParentBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return listData.size
    }


    class MultiplicationPageListChildAdapter(
        private var listData: List<MultiplicationPages>,
        private val mListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<MultiplicationPageListChildAdapter.FormViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
        ): FormViewHolder {
            val binding = RawPagelistChildBinding.inflate(parent.context.layoutInflater,parent,false)
            return FormViewHolder(
                binding
            )
        }

        override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
            val data: MultiplicationPages = listData[position]
            val context = holder.binding.txtTitle.context
            holder.binding.desc = data.PageName
            holder.binding.txtTitle.hide()

            val pageId = "Multilication_Page ${data.id}"
            if (CommonUtils.getCurrentSumFromPref(context,pageId) != null){
                CommonUtils.blinkView(holder.binding.imgContinueIndicator)
            }else{
                holder.binding.imgContinueIndicator.hide()
            }

            holder.binding.root.onClick {
                mListener.onMultiplicationItemClick(data)
            }
        }

        class FormViewHolder(
            itemBinding: RawPagelistChildBinding
        ) :
            RecyclerView.ViewHolder(itemBinding.root) {
            var binding: RawPagelistChildBinding = itemBinding
        }

        override fun getItemCount(): Int {
            return listData.size
        }

    }
}