package com.example.iiifa_fan_android.ui.view.registration.dilogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.iiifa_fan_android.databinding.DialogEmailAlreadyExistBinding
import com.example.iiifa_fan_android.ui.view.login.activities.LoginActivity
import com.example.iiifa_fan_android.utils.extensions.onClick

class EmailAlreadyExist(context: Context,var email: String) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val binding: DialogEmailAlreadyExistBinding = DialogEmailAlreadyExistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.onClick {
            LoginActivity.getInstance(context, email = email)
        }
        binding.tvDismiss.onClick { dismiss() }
    }
}