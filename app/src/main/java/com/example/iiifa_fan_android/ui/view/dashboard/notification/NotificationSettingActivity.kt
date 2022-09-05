package com.example.iiifa_fan_android.ui.view.dashboard.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.iiifa_fan_android.databinding.ActivityNotificationSettingBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationSettingActivity : BaseActivity() {

    private lateinit var binding: ActivityNotificationSettingBinding

    companion object {
        fun getInstance(context: Context?) {
            Intent(context, NotificationSettingActivity::class.java).apply {
                context?.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListener()
        initObserver()
    }

    private fun initViews() {

    }

    private fun initListener() {
        binding.ibBack.onClick { onBackPressed() }

    }

    private fun initObserver() {

    }


}