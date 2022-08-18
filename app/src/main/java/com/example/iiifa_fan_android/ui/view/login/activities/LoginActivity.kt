package com.example.iiifa_fan_android.ui.view.login.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iiifa_fan_android.R

class LoginActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun getInstance(
            context: Context?,
            is_from_non_logged_in_flow: Boolean? = false, showingAfterLogout: Boolean? = false
        ): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("is_from_non_logged_in_flow", is_from_non_logged_in_flow)
            intent.putExtra("showing_after_logout", showingAfterLogout)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}