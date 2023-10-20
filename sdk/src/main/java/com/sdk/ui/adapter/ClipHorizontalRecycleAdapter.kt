package com.sdk.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sdk.data.HealingClip
import com.sdk.databinding.RvClipHorizontalBinding
import com.sdk.ui.interfaces.ClipClickListener
import com.sdk.utils.extensions.layoutInflater
import com.sdk.utils.extensions.onClick


class ClipHorizontalRecycleAdapter(
        private val list: ArrayList<HealingClip>,
        private val listener: ClipClickListener? = null
) : RecyclerView.Adapter<ClipHorizontalRecycleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val binding = RvClipHorizontalBinding.inflate(parent.context.layoutInflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            with(list[position]) {
                data = this
                holder.itemView.onClick {
                    listener?.onClipClicked(position, this@with)
                }
            }
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RvClipHorizontalBinding) :
            RecyclerView.ViewHolder(binding.root)
}