package com.example.iiifa_fan_android.ui.view.dashboard.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.iiifa_fan_android.data.models.dataprovider.SideMenu
import com.example.iiifa_fan_android.databinding.RawDashboardSideSubmenuBinding
import com.example.iiifa_fan_android.databinding.RawDashboardSidemenuBinding
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.layoutInflater
import com.example.iiifa_fan_android.utils.extensions.show

class SideMenuListAdapter(
    private var listData: List<SideMenu>,
    private val mListener: OnItemClickListener
) :
    RecyclerView.Adapter<SideMenuListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onMenuItemClick(menuItem: SideMenu)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = RawDashboardSidemenuBinding.inflate(parent.context.layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listData[position]) {
                binding.dataModel = this
                val sideMenuListAdapter = SubMenuListAdapter(subMenu,mListener)
                binding.rvMenu.adapter = sideMenuListAdapter
                binding.rvMenu.hide()
                binding.imgArrow.rotation = 0f
                binding.root.setOnClickListener {
                    if (subMenu.isEmpty()){
                        mListener.onMenuItemClick(listData[position])
                    }else{
                        if (binding.rvMenu.isVisible){
                            binding.rvMenu.hide()
                            binding.imgArrow.rotation = 0f
                        }else{
                            binding.rvMenu.show()
                            binding.imgArrow.rotation = 90f
                        }
                    }
                }
            }
        }
    }

    class ViewHolder(val binding: RawDashboardSidemenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return listData.size
    }

    class SubMenuListAdapter(
        private var listData: List<SideMenu>,
        private val mListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<SubMenuListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
        ): ViewHolder {
            val binding = RawDashboardSideSubmenuBinding.inflate(parent.context.layoutInflater, parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                with(listData[position]) {
                    binding.dataModel = this
                    binding.root.setOnClickListener {
                        mListener.onMenuItemClick(listData[position])
                    }
                }
            }
        }

        class ViewHolder(val binding: RawDashboardSideSubmenuBinding) : RecyclerView.ViewHolder(binding.root)

        override fun getItemCount(): Int {
            return listData.size
        }

    }
}