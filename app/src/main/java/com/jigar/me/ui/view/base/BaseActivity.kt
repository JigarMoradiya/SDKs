package com.jigar.me.ui.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.Constants


/**
 * Used for handle common methods of activities
 */
abstract class BaseActivity : AppCompatActivity() {
    lateinit var prefManager : AppPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = AppPreferencesHelper(this, Constants.PREF_NAME)
    }
}