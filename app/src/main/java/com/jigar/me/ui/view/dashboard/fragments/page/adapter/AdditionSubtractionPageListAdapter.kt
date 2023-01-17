package com.jigar.me.ui.view.dashboard.fragments.page.adapter

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.model.pages.AdditionSubtractionCategory
import com.jigar.me.data.model.pages.AdditionSubtractionPage
import com.jigar.me.databinding.RawPagelistChildBinding
import com.jigar.me.databinding.RawPagelistParentBinding
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater

class AdditionSubtractionPageListAdapter(private var listData: ArrayList<AdditionSubtractionCategory>, private val mListener: OnItemClickListener) :
    RecyclerView.Adapter<AdditionSubtractionPageListAdapter.FormViewHolder>() {
    interface OnItemClickListener {
        fun onAdditionSubtractionItemClick(data: AdditionSubtractionPage)
    }

    fun setData(listData: List<AdditionSubtractionCategory>) {
        this.listData.clear()
        this.listData.addAll(listData)
        notifyItemRangeChanged(0,listData.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val binding = RawPagelistParentBinding.inflate(parent.context.layoutInflater,parent,false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val data: AdditionSubtractionCategory = listData[position]
        holder.binding.title = data.category_name
        val additionSubtractionPageListAdapter = AdditionSubtractionPageListChildAdapter(data.pages,mListener)
        holder.binding.recyclerviewPage.adapter = additionSubtractionPageListAdapter
    }

    class FormViewHolder(itemBinding: RawPagelistParentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RawPagelistParentBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return listData.size
    }


    class AdditionSubtractionPageListChildAdapter(private var listData: ArrayList<AdditionSubtractionPage>,private val mListener: OnItemClickListener) :
        RecyclerView.Adapter<AdditionSubtractionPageListChildAdapter.FormViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
            val binding = RawPagelistChildBinding.inflate(parent.context.layoutInflater,parent,false)
            return FormViewHolder(binding)
        }

        override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
            val data: AdditionSubtractionPage = listData[position]
            val context = holder.binding.txtTitle.context
            holder.binding.title = data.PageName
            holder.binding.desc = data.SortDesc

            if (data.page_id?.let { CommonUtils.getCurrentSumFromPref(context, it) } != null){
                CommonUtils.blinkView(holder.binding.imgContinueIndicator)
            }else{
                holder.binding.imgContinueIndicator.hide()
            }

            holder.binding.root.setOnClickListener {
                this.mListener.onAdditionSubtractionItemClick(data)
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