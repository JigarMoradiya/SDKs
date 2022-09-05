package com.example.iiifa_fan_android.ui.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.iiifa_fan_android.data.pref.AppPreferencesHelper
import com.example.iiifa_fan_android.data.pref.PreferencesHelper
import com.example.iiifa_fan_android.utils.Constants
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
//    @Inject
//    internal lateinit var prefManager: PreferencesHelper
    lateinit var prefManager : AppPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = AppPreferencesHelper(requireContext(), Constants.PREF_NAME)
    }
}