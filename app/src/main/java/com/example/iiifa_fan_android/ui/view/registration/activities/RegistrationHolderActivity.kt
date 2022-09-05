package com.example.iiifa_fan_android.ui.view.registration.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationHolderActivity : BaseActivity() {
    var navController: NavController? = null
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
        initView()
    }

    private fun initView() {
        val navHostFragment = (supportFragmentManager.findFragmentById(R.id.fragment_main) as NavHostFragment?)!!
        navController = navHostFragment.navController
        if (prefManager.getUserId() != null && !TextUtils.isEmpty(prefManager.getUserId())) {
            navController?.navigate(R.id.personalDetailsFragment)
        }
    }
}