package com.example.iiifa_fan_android.ui.view.dashboard.cms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.FAQModel
import com.example.iiifa_fan_android.databinding.ActivityFaqBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.dashboard.cms.adapter.FAQAdapter
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.widget.MiddleDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class FAQActivity : BaseActivity() {

    private lateinit var binding: ActivityFaqBinding
    private lateinit var faqAdapter: FAQAdapter
    private val faqModelList = ArrayList<FAQModel>()
    companion object {
        fun getInstance(context: Context?) {
            Intent(context, FAQActivity::class.java).apply {
                context?.startActivity(this)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListener()

    }

    private fun initViews() {
        var faqModel = FAQModel(name = "Appointment Related")
        faqModelList.add(faqModel)
        faqModel = FAQModel(name = "Notification")
        faqModelList.add(faqModel)
        faqModel = FAQModel(name = "Expert Related")
        faqModelList.add(faqModel)

        val faqModel1 = FAQModel(question = "How secure are my card details?",answer = "All card detail are held and processed by one of largest and most trusted payment provides in the world. we do not have direct access to any of your details.")
        val faqModel2 = FAQModel(question = "How do I know if my payment has been processed?",answer = "All card detail are held and processed by one of largest and most trusted payment provides in the world. we do not have direct access to any of your details.")
        val faqModel3 = FAQModel(question = "How to add a credit card?",answer = "All card detail are held and processed by one of largest and most trusted payment provides in the world. we do not have direct access to any of your details.")
        faqModel = FAQModel(name = "Credit/Debit Card", faqs = listOf(faqModel1,faqModel2,faqModel3))
        faqModelList.add(faqModel)

        faqAdapter = FAQAdapter(faqModelList)
        binding.rvFaq.adapter = faqAdapter
        val decoration = MiddleDividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decoration.setDividerColor(ContextCompat.getColor(this, R.color.color_divider_grey))
        binding.rvFaq.addItemDecoration(decoration)
    }

    private fun initListener() {
        binding.ibBack.onClick { onBackPressed() }
    }


}