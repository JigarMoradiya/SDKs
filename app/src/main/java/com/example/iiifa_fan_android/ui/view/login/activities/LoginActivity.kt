package com.example.iiifa_fan_android.ui.view.login.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.ActivityLoginBinding
import com.example.iiifa_fan_android.ui.view.dashboard.MainDashboardActivity
import com.example.iiifa_fan_android.ui.view.login.component.GoogleLoginComponent
import com.example.iiifa_fan_android.ui.view.login.viewmodel.SocialLoginViewModel
import com.example.iiifa_fan_android.ui.view.registration.activities.RegistrationHolderActivity
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews.removeError
import com.example.iiifa_fan_android.utils.CustomViews.setErrortoEditText
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), GoogleLoginComponent.StartActivityResult {
    private lateinit var binding: ActivityLoginBinding
    private var email: String = ""
    private var password: String = ""
    private val socialLoginViewModel by viewModels<SocialLoginViewModel>()
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
        initView()
        initListener()
        addTextWatcher()
        initObserver()
    }

    private fun initView() {
        /**
         * set ViewModel For social login component
         */
        binding.ivFacebook.setViewModel(socialLoginViewModel)
        binding.ivGoogle.setViewModel(socialLoginViewModel)
        binding.ivGoogle.setListener(this)
        binding.ivApple.setViewModel(socialLoginViewModel)
        binding.ivApple.setListener(this)
    }

    private fun initListener() {
        binding.tvSignUp.onClick {
            val intent = Intent(this@LoginActivity, RegistrationHolderActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.onClick {
//            if (validateFields()){
                val intent = Intent(this@LoginActivity, MainDashboardActivity::class.java)
                startActivity(intent)
//            }
        }
    }

    private fun initObserver() {
        socialLoginViewModel.isSocialDataSet.observe(this) {
            if (it) {
                val socialMediaModel = socialLoginViewModel.getSocialMediaLoginData()
                Log.e("loginActivity","socialMediaModel : "+Gson().toJson(socialMediaModel))
                if (!socialMediaModel?.email.isNullOrEmpty()) {

                }
            }
        }
    }

    private fun validateFields(): Boolean {
        var validate = true
        email = Objects.requireNonNull(binding.etEmail.text).toString().trim { it <= ' ' }
        password = Objects.requireNonNull(binding.etPassword.text).toString()
        if (TextUtils.isEmpty(email)) {
            validate = false
            setErrortoEditText(this, binding.etEmail, binding.textInputLayoutEmail,getString(R.string.validation_no_username))
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validate = false
            setErrortoEditText(this,binding.etEmail,binding.textInputLayoutEmail,getString(R.string.validation_invalid_username))
        }else if (TextUtils.isEmpty(password)) {
            validate = false
            setErrortoEditText(this, binding.etPassword, binding.textInputLayoutPassword, getString(R.string.validation_no_password))
        }
        return validate
    }

    private fun addTextWatcher() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                removeError(this@LoginActivity, binding.etEmail, binding.textInputLayoutEmail)
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                removeError(this@LoginActivity, binding.etPassword, binding.textInputLayoutPassword)
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.ivFacebook.activityResult(requestCode, resultCode, data)
    }

    /*
    * google login intent launch
    * */
    override fun onStart(signInIntent: Intent?) {
        googleActivityResultLauncher.launch(signInIntent)
    }

    private var googleActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                binding.ivGoogle.activityResult(activityResult.data)
            }
        }
    override fun showHideProgress(isShow: Boolean) {

    }
}