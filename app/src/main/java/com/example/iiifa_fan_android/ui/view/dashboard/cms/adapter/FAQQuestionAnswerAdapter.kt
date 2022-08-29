package com.example.iiifa_fan_android.ui.view.dashboard.cms.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.FAQModel
import com.example.iiifa_fan_android.databinding.RvFaqQuestionAnswerLayoutBinding
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.show
import java.util.*

class FAQQuestionAnswerAdapter(var questionsList: List<FAQModel>) :
    RecyclerView.Adapter<FAQQuestionAnswerAdapter.ViewHolder>() {

    private var selcted_faq_id: String? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FAQQuestionAnswerAdapter.ViewHolder {
        val binding = RvFaqQuestionAnswerLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FAQQuestionAnswerAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(questionsList[position]) {

                if (this.question != null && !TextUtils.isEmpty(this.question)) binding.tvFaqCards.text =
                    this.question!!.trim { it <= ' ' }
                if (this.answer != null && !TextUtils.isEmpty(this.answer)) binding.tvFaqAnswer.text =
                    this.answer!!.trim { it <= ' ' }
                binding.ivPlus.onClick {
                    binding.tvFaqCards.performClick()
                }
                binding.tvFaqCards.onClick {
                    if (!binding.tvFaqAnswer.isVisible) {
                        binding.tvFaqAnswer.show()
                        binding.ivPlus.setImageResource(R.drawable.ic_drop_down)
                    } else {
                        binding.tvFaqAnswer.hide()
                        binding.ivPlus.setImageResource(R.drawable.ic_right_arrow)
                    }
                }

            }
        }

    }

    fun showAnswer(selcted_faq_id: String?) {
        this.selcted_faq_id = selcted_faq_id
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return questionsList.size
    }

    inner class ViewHolder(val binding: RvFaqQuestionAnswerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}