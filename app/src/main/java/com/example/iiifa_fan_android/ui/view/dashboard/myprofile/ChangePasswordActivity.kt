package com.example.iiifa_fan_android.ui.view.dashboard.myprofile

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.ActivityChangePasswordBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.commonviews.classes.PasswordMeterClass
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.example.iiifa_fan_android.utils.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var passwordMeterClass: PasswordMeterClass
    private var type: String? = null
    private var oldPassword: String = ""
    private var newPassword: String = ""
    private var confirmPassword: String = ""
    private var lastProgress = 0
    companion object {
        fun getInstance(context: Context?) {
            Intent(context, ChangePasswordActivity::class.java).apply {
                context?.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListener()

    }

    private fun initViews() {
        passwordMeterClass = PasswordMeterClass(this)
        passwordMeterClass.setAdapter(binding.rvPassword)
    }

    private fun initListener() {
        binding.ibBack.onClick { onBackPressed() }
        binding.btnUpdatePassword.onClick {
            if (validateFields()){

            }
        }
        binding.etNewpassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.textInputLayoutNewpassword.setEndIconTintList(ContextCompat.getColorStateList(this@ChangePasswordActivity, R.color.white))
                CustomViews.removeError(this@ChangePasswordActivity,binding.etNewpassword,binding.textInputLayoutNewpassword,0, R.color.text_color,true)
                val len = s.length

                //if user erase everything , then hide hint as well
                if (len == 0) {
                    binding.seekbarPassword.hide()
                    binding.tvHintPassword.hide()
                    binding.tvPasswordType.hide()
                    passwordMeterClass.setResetAll()
                } else {
                    binding.seekbarPassword.show()
                    passwordMeterClass.atLeast8charSelected(s.toString().length >= 8)
                    passwordMeterClass.numericCharSelected(CustomFunctions.containsNumericCharacter(s.toString()))
                    passwordMeterClass.smallCharSelected(CustomFunctions.containsLowerCase(s.toString()))
                    passwordMeterClass.capitalLetterSelected(CustomFunctions.containsUpperCase(s.toString()))

                    //check basic condition
                    if (passwordMeterClass.isAllSelected()) {
                        type = if (!CustomFunctions.containsSpecialCharacter(s.toString())) {
                            if (s.length < 12) {
                                Constants.WEAK
                            } else if (s.length < 16) {
                                Constants.MODERATED
                            } else {
                                Constants.STRONG
                            }
                        } else {
                            if (s.length < 12) {
                                Constants.MODERATED
                            } else {
                                Constants.STRONG
                            }
                        }
                    } else {
                        type = Constants.WEAK
                    }
                    changeSeekbar()
                }
            }
        })


        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.textInputLayoutConfirmPassword.setEndIconTintList(ContextCompat.getColorStateList(this@ChangePasswordActivity, R.color.white))
                CustomViews.removeError(this@ChangePasswordActivity,binding.etConfirmPassword,binding.textInputLayoutConfirmPassword,0,
                    R.color.text_color,true)
            }
        })
        binding.etCpassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.textInputLayoutCpassword.setEndIconTintList(ContextCompat.getColorStateList(this@ChangePasswordActivity, R.color.white))
                CustomViews.removeError(this@ChangePasswordActivity,binding.etCpassword,binding.textInputLayoutCpassword,0,
                    R.color.text_color,true)
            }
        })
    }

    private fun validateFields(): Boolean {
        var validate = true
        confirmPassword = Objects.requireNonNull(binding.etConfirmPassword.text).toString()
        newPassword = Objects.requireNonNull(binding.etNewpassword.text).toString()
        oldPassword = Objects.requireNonNull(binding.etCpassword.text).toString()
        if (TextUtils.isEmpty(oldPassword)) {
            validate = false
            CustomViews.setErrortoEditText(this, binding.etNewpassword, binding.textInputLayoutCpassword, getString(R.string.validation_no_old_password))
        } else if (TextUtils.isEmpty(newPassword)) {
            validate = false
            binding.tvHintPassword.hide()
            binding.seekbarPassword.hide()
            passwordMeterClass.setAllError()
            CustomViews.setErrortoEditText(this, binding.etNewpassword, binding.textInputLayoutNewpassword, getString(R.string.validation_no_new_password))
        } else if (!passwordMeterClass.setAllError()) {
            validate = false
        } else if (TextUtils.isEmpty(confirmPassword)) {
            validate = false
            CustomViews.setErrortoEditText(this, binding.etConfirmPassword, binding.textInputLayoutConfirmPassword, getString(R.string.validation_no_confirmPassword))
        } else if (newPassword != confirmPassword) {
            validate = false
            CustomViews.setErrortoEditText(this, binding.etConfirmPassword, binding.textInputLayoutConfirmPassword, getString(R.string.validation_match_password))
        }
        return validate
    }

    fun changeSeekbar() {
        when (type) {
            Constants.WEAK -> {
                animateSeekbar(30, R.color.password_weak, Constants.WEAK)
            }
            Constants.MODERATED -> {
                animateSeekbar(60, R.color.password_moderated, Constants.MODERATED)
            }
            Constants.STRONG -> {
                animateSeekbar(100, R.color.password_strong, Constants.STRONG)
            }
            Constants.NOT_ACCEPTED -> {
                binding.seekbarPassword.hide()
                binding.tvPasswordType.hide()
                binding.tvHintPassword.show()
            }
        }
    }

    private fun animateSeekbar(progress: Int, color: Int, type: String?) {
        binding.seekbarPassword.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, color))
        binding.seekbarPassword.show()
        binding.tvHintPassword.hide()
        binding.tvPasswordType.show()
        binding.tvPasswordType.text = type
        binding.tvPasswordType.setTextColor(ContextCompat.getColor(this, color))
        if (lastProgress != progress) {
            binding.seekbarPassword.setProgress(lastProgress, progress)
            lastProgress = progress
        }
    }


}