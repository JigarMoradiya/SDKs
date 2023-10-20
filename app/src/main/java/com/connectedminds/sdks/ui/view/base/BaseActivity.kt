package com.connectedminds.sdks.ui.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.connectedminds.sdks.utils.AppConstants


/**
 * Used for handle common methods of activities
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}