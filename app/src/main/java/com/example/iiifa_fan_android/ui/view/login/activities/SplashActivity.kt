package com.example.iiifa_fan_android.ui.view.login.activities

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.iiifa_fan_android.data.pref.AppPreferencesHelper
import com.example.iiifa_fan_android.databinding.ActivitySplashBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.dashboard.MainDashboardActivity
import com.example.iiifa_fan_android.ui.view.registration.activities.RegistrationHolderActivity
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.MyApplication
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(3000)
            checkUserIsLoggedIn()
        }
    }

    private fun checkUserIsLoggedIn() {
        val prefManager = AppPreferencesHelper(this, Constants.PREF_NAME)
        if (TextUtils.isEmpty(prefManager.getUserId()) || TextUtils.isEmpty(prefManager.getUserData())) {
            LoginActivity.getInstance(this)
        } else {
            MainDashboardActivity.getInstance(this)
//            RegistrationHolderActivity.getInstance(this)
        }
    }

}