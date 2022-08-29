package com.example.iiifa_fan_android.ui.view.dashboard.cms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.iiifa_fan_android.databinding.ActivityContactUsBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactusActivity : BaseActivity() {

    private lateinit var binding: ActivityContactUsBinding

    companion object {
        fun getInstance(context: Context?) {
            Intent(context, ContactusActivity::class.java).apply {
                context?.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListener()

    }

    private fun initViews() {
    }

    private fun initListener() {
        binding.ibBack.onClick { onBackPressed() }
    }

}