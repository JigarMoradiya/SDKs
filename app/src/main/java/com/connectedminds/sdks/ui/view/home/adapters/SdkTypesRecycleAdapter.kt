package com.connectedminds.sdks.ui.view.home.adapters

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.connectedminds.sdks.databinding.RvSdkChildBinding
import com.connectedminds.sdks.databinding.RvSdkParentBinding
import com.connectedminds.sdks.utils.extensions.hide
import com.connectedminds.sdks.utils.extensions.onClick
import com.connectedminds.sdks.utils.extensions.show
import com.sdk.data.SdkChild
import com.sdk.data.SdkTypes
import com.sdk.utils.extensions.layoutInflater


class SdkTypesRecycleAdapter(
    private val list: ArrayList<SdkTypes>,
    private val listener: ItemClickListener? = null
) : RecyclerView.Adapter<SdkTypesRecycleAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onChildItemClicked(id : String)
    }
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val binding = RvSdkParentBinding.inflate(parent.context.layoutInflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            with(list[position]) {
                data = this
                val adapter = SdkTypesChildRecycleAdapter(this.childs,listener)
                rv.adapter = adapter
                tvTitle.onClick {
                    if (rv.isVisible){
                        rv.hide()
                    }else{
                        rv.show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RvSdkParentBinding) : RecyclerView.ViewHolder(binding.root)

    class SdkTypesChildRecycleAdapter(
        private val list: ArrayList<SdkChild>,
        private val listener: ItemClickListener? = null
    ) : RecyclerView.Adapter<SdkTypesChildRecycleAdapter.ViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val binding = RvSdkChildBinding.inflate(parent.context.layoutInflater, parent, false)
            return ViewHolder(binding)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder.binding) {
                with(list[position]) {
                    val dataModel = this
                    data = dataModel
                    tvTitle.onClick {
                        listener?.onChildItemClicked(dataModel.id)
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class ViewHolder(val binding: RvSdkChildBinding) : RecyclerView.ViewHolder(binding.root)
    }
}