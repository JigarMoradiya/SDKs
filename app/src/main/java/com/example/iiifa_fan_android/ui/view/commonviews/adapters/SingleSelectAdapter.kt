package com.example.iiifa_fan_android.ui.view.commonviews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.MultiSelect
import com.example.iiifa_fan_android.databinding.RvMultiselectLayoutBinding
import com.example.iiifa_fan_android.ui.view.commonviews.interfaces.SingleSelectClickListner


class SingleSelectAdapter(
    val list: ArrayList<MultiSelect>,
    var previouslySelectedId: String? = null,
    var showSelection: Boolean = false,
    var showArrow: Boolean = false,
    var singleSelectClickListner: SingleSelectClickListner
) :
    RecyclerView.Adapter<SingleSelectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvMultiselectLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.tvName.text = this.name

                binding.checkbox.visibility = View.GONE

                if (showArrow) {
                    binding.tvName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_right_arrow,
                        0
                    )
                } else {
                    binding.tvName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                }

                if (showSelection) {
                    if (previouslySelectedId != null) {

                        if (previouslySelectedId.equals(this.id.trim())) {


                            binding.tvName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_done,
                                0,
                                0,
                                0
                            )
                        } else {

                            binding.tvName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                0,
                                0
                            )
                        }
                    }
                }

                binding.tvName.setOnClickListener {


                    if (showSelection) {
                        if (!previouslySelectedId.equals(this.id.trim())) {
                            previouslySelectedId = (this.id.trim())
                            singleSelectClickListner.onClicked(this)
                        } else {
                            previouslySelectedId = ""
                        }
                    } else {
                        singleSelectClickListner.onClicked(this)
                    }

                    notifyDataSetChanged()
                }

            }
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RvMultiselectLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}