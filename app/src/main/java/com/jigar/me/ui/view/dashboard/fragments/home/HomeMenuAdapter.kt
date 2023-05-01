package com.jigar.me.ui.view.dashboard.fragments.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.local.data.HomeMenu
import com.jigar.me.databinding.RawHomeMenuBinding
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.show

class HomeMenuAdapter(
    private var listData: List<HomeMenu>,private val mListener: OnItemClickListener
) : RecyclerView.Adapter<HomeMenuAdapter.FormViewHolder>() {
    interface OnItemClickListener {
        fun onItemHomeMenuClick(data: HomeMenu)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FormViewHolder {
        val binding = RawHomeMenuBinding.inflate(parent.context.layoutInflater,parent,false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val data = listData[position]
        holder.binding.imgMenu.setImageResource(data.image)
        if (data.tag.isEmpty()){
            holder.binding.txtTag.hide()
        }else {
            holder.binding.txtTag.show()
            holder.binding.txtTag.text = data.tag
        }
        holder.binding.root.onClick {
            mListener.onItemHomeMenuClick(data)
        }
    }

    class FormViewHolder(itemBinding: RawHomeMenuBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RawHomeMenuBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}