package com.jigar.me.ui.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.setLocale


/**
 * Used for handle common methods of activities
 */
abstract class BaseActivity : AppCompatActivity() {
    lateinit var prefManager : AppPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        prefManager = AppPreferencesHelper(this, AppConstants.PREF_NAME)
//        this.setLocale(prefManager.getCustomParam(Constants.appLanguage,"en"))
        super.onCreate(savedInstanceState)
    }
}