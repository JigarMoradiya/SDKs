package com.example.iiifa_fan_android.ui.view.forgotpassword

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.ActivityForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private var selected_index: Int = -1
    private lateinit var binding: ActivityForgotPasswordBinding


    companion object {
        @JvmStatic
        fun getInstance(context: Context?) {
            Intent(context, ForgotPasswordActivity::class.java).apply {
                context?.startActivity(this)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_forgot_password_main) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            selected_index = destination.id
            changeCloseIcon()
        }

        binding.ibClose.setOnClickListener {
            if (selected_index == R.id.forgotFragmentPassword) {
                finish()
            } else {
                navController.navigateUp()
            }
        }
    }

    private fun changeCloseIcon() {
        if (selected_index == R.id.forgotFragmentPassword) {
            binding.ibClose.setImageResource(R.drawable.ic_close)
        } else {
            binding.ibClose.setImageResource(R.drawable.ic_arrow_back)
        }
    }
}