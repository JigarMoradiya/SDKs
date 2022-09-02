package com.example.iiifa_fan_android.ui.view.registration.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.ui.view.forgotpassword.ForgotPasswordActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationHolderActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun getInstance(context: Context?) {
            Intent(context, RegistrationHolderActivity::class.java).apply {
                context?.startActivity(this)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_holder)
    }
}