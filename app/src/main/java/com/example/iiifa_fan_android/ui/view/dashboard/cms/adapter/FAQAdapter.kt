package com.example.iiifa_fan_android.ui.view.dashboard.cms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.FAQModel
import com.example.iiifa_fan_android.databinding.RvFaqLayoutBinding
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.show

class FAQAdapter(var questionsList: List<FAQModel>) : RecyclerView.Adapter<FAQAdapter.ViewHolder>() {

    private var selcted_faq_id: String? = null
    private var answerAdapter: FAQQuestionAnswerAdapter? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FAQAdapter.ViewHolder {
        val binding = RvFaqLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FAQAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(questionsList[position]) {
                if (this.faqs.isNotEmpty()) {
                    answerAdapter = FAQQuestionAnswerAdapter(this.faqs)
                    binding.rvFaqSubLayout.adapter = answerAdapter
                }
                binding.tvFaqCards.text = this.name!!.trim { it <= ' ' }

                binding.ivPlus.onClick {
                    binding.tvFaqCards.performClick()
                }

                binding.tvFaqCards.onClick {
                    if (!binding.rvFaqSubLayout.isVisible) {
                        binding.rvFaqSubLayout.show()
                        binding.ivPlus.setImageResource(R.drawable.ic_iv_minus_faq)
                    } else {
                        binding.rvFaqSubLayout.hide()
                        binding.ivPlus.setImageResource(R.drawable.ic_iv_plus_faq)
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

    inner class ViewHolder(val binding: RvFaqLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}