package com.example.iiifa_fan_android.ui.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.iiifa_fan_android.data.network.MainApiCall
import com.example.iiifa_fan_android.data.pref.PreferencesHelper
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    @Inject
    internal lateinit var prefManager: PreferencesHelper
    val mainApiCall: MainApiCall = MainApiCall.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}