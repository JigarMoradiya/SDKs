package com.jigar.me.ui.view.dashboard.fragments.page.adapter

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.model.pages.DivisionCategory
import com.jigar.me.data.model.pages.DivisionPages
import com.jigar.me.databinding.RawPagelistChildBinding
import com.jigar.me.databinding.RawPagelistParentBinding
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.CommonUtils.getCurrentSumFromPref
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater

class DivisionPageListAdapter(
    private var listData: ArrayList<DivisionCategory>,
    private val mListener: OnItemClickListener
) :
    RecyclerView.Adapter<DivisionPageListAdapter.FormViewHolder>() {
    interface OnItemClickListener {
        fun onDivisionItemClick(data: DivisionPages)
    }

    fun setData(listData: List<DivisionCategory>) {
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
        val data: DivisionCategory = listData[position]
        holder.binding.title = data.category_name
        val childAdapter = DivisionPageListChildAdapter(data.pages, mListener)
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

    class DivisionPageListChildAdapter(
        private var listData: List<DivisionPages>,
        private val mListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<DivisionPageListChildAdapter.FormViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
        ): FormViewHolder {
            val binding = RawPagelistChildBinding.inflate(parent.context.layoutInflater,parent,false)
            return FormViewHolder(binding)
        }

        override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
            val data: DivisionPages = listData[position]
            val context = holder.binding.txtTitle.context
            holder.binding.desc = data.PageName
            holder.binding.txtTitle.hide()

            val pageId = "Devide_Page ${data.id}"
            if (context.getCurrentSumFromPref(pageId) != null){
                CommonUtils.blinkView(holder.binding.imgContinueIndicator)
            }else{
                holder.binding.imgContinueIndicator.hide()
            }

            holder.binding.root.setOnClickListener {
                this.mListener.onDivisionItemClick(data)
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