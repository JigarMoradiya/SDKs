package com.example.iiifa_fan_android.ui.view.login.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.ActivityLoginBinding
import com.example.iiifa_fan_android.ui.view.dashboard.MainDashboardActivity
import com.example.iiifa_fan_android.ui.view.registration.activities.RegistrationHolderActivity
import com.example.iiifa_fan_android.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    companion object {
        @JvmStatic
        fun getInstance(
            context: Context?,
            is_from_non_logged_in_flow: Boolean? = false, showingAfterLogout: Boolean? = false
        ): Intent {
            val intent = Intent(context, LoginActivity::class.java).apply {
                putExtra("is_from_non_logged_in_flow", is_from_non_logged_in_flow)
                putExtra("showing_after_logout", showingAfterLogout)
            }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        binding.tvSignUp.onClick {
            val intent = Intent(this@LoginActivity, RegistrationHolderActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.onClick {
            val intent = Intent(this@LoginActivity, MainDashboardActivity::class.java)
            startActivity(intent)
        }
    }
}