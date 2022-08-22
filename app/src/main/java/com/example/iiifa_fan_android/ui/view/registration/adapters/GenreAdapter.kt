package com.example.iiifa_fan_android.ui.view.registration.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.MultiSelect
import com.example.iiifa_fan_android.data.models.PasswordMeter
import com.example.iiifa_fan_android.data.models.PasswordMeterStatus
import com.example.iiifa_fan_android.data.models.Preferences
import com.example.iiifa_fan_android.databinding.LayoutGenreBinding
import com.example.iiifa_fan_android.databinding.RvPasswordMeterBinding
import com.example.iiifa_fan_android.utils.extensions.onClick


class GenreAdapter(
    val context: Context,
    val list: ArrayList<Preferences>
) :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    interface ClickListener {
        fun onItemClicked(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = LayoutGenreBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.tvTitle.text = this.name
                if (list[position].is_selected){
                    binding.tvTitle.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.chipSelected))
                    binding.tvTitle.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorAccent))
                }else{
                    binding.tvTitle.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.color_primary))
                    binding.tvTitle.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.color_primary))
                }
                binding.tvTitle.onClick { 
                    if (list[position].is_selected){
                        list[position].is_selected = false
                        notifyItemChanged(position)
                    }else{
                        list[position].is_selected = true
                        notifyItemChanged(position)
                    }
                }
            }
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: LayoutGenreBinding) :
        RecyclerView.ViewHolder(binding.root)
}