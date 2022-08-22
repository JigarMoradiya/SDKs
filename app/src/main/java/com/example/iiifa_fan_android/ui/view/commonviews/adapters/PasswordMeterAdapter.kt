package com.example.iiifa_fan_android.ui.view.commonviews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.PasswordMeter
import com.example.iiifa_fan_android.data.models.PasswordMeterStatus
import com.example.iiifa_fan_android.databinding.RvPasswordMeterBinding


class PasswordMeterAdapter(
    val context: Context,
    val list: ArrayList<PasswordMeter>,
) :
    RecyclerView.Adapter<PasswordMeterAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvPasswordMeterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {

                when (this.status) {
                    PasswordMeterStatus.ERROR -> {
                        binding.ivType.setImageResource(R.drawable.ic_pass_circle_error)
                        binding.tvName.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.password_text_error
                            )
                        )
                    }

                    PasswordMeterStatus.SELECTED -> {
                        binding.ivType.setImageResource(R.drawable.ic_pass_circle_selected)
                        binding.tvName.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.password_text_selected
                            )
                        )
                    }

                    else -> {
                        binding.ivType.setImageResource(R.drawable.ic_pass_circle_not_filled)
                        binding.tvName.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.password_text_not_selected
                            )
                        )
                    }
                }

                binding.tvName.text = this.name

            }
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RvPasswordMeterBinding) :
        RecyclerView.ViewHolder(binding.root)
}