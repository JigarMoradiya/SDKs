package com.example.iiifa_fan_android.ui.view.forgotpassword.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.FragmentResetPasswordBinding
import com.example.iiifa_fan_android.ui.view.commonviews.classes.PasswordMeterClass
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.ui.viewmodel.FanViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomFunctions.containsLowerCase
import com.example.iiifa_fan_android.utils.CustomFunctions.containsNumericCharacter
import com.example.iiifa_fan_android.utils.CustomFunctions.containsUpperCase
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.CustomViews.removeError
import com.example.iiifa_fan_android.utils.CustomViews.setErrortoEditText
import com.example.iiifa_fan_android.utils.Resource
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.example.iiifa_fan_android.utils.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel by activityViewModels<CommonViewModel>()
    private var password: String? = null
    private var confirm_password: String? = null

    private var otp: String? = null
    private var email: String? = null
    private var passwordMeterClass: PasswordMeterClass? = null
    private var type: String? = null
    private var lastProgress = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email")?:""
        otp = arguments?.getString("otp")?:""
        initObserver()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    fun initViews() {
        addTextWatcher()
        passwordMeterClass = PasswordMeterClass(requireContext())
        passwordMeterClass?.setAdapter(binding.rvPassword)
    }

    private fun addTextWatcher() {

        binding.btnNext.setOnClickListener {
            if (validateDetails()) {
                setParamsToCallApi()
            }
        }

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.textInputLayoutPassword.setEndIconTintList(ContextCompat.getColorStateList(context!!, R.color.colorAccent))
                removeError(context, binding.etPassword, binding.textInputLayoutPassword, 0, R.color.text_color, false)
                val len = s.length

                //if user erase everything , then hide hint as well
                if (len == 0) {
                    binding.seekbarPassword.hide()
                    binding.tvHintPassword.hide()
                    binding.etPassword.setBackgroundResource(R.drawable.text_box_underline)
                    binding.tvPasswordType.hide()
                    passwordMeterClass?.setResetAll()
                } else {
                    binding.seekbarPassword.show()
                    passwordMeterClass?.atLeast8charSelected(s.toString().length >= 8)
                    passwordMeterClass?.numericCharSelected(containsNumericCharacter(s.toString()))
                    passwordMeterClass?.smallCharSelected(containsLowerCase(s.toString()))
                    passwordMeterClass?.capitalLetterSelected(containsUpperCase(s.toString()))

                    //check basic condition
                    type = if (passwordMeterClass!!.isAllSelected()) {
                        if (!CustomFunctions.containsSpecialCharacter(s.toString())) {
                            when {
                                s.length < 12 -> {
                                    Constants.WEAK
                                }
                                s.length < 16 -> {
                                    Constants.MODERATED
                                }
                                else -> {
                                    Constants.STRONG
                                }
                            }
                        } else {
                            when {
                                s.length < 12 -> {
                                    Constants.MODERATED
                                }
                                else -> {
                                    Constants.STRONG
                                }
                            }
                        }
                    } else {
                        Constants.WEAK
                    }

                    changeSeekbar()
                }

            }
        })
        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.textInputLayoutConfirmPassword.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.colorAccent))
                removeError(context,binding.etConfirmPassword,binding.textInputLayoutConfirmPassword,0,R.color.text_color)
            }
        })
    }


    private fun validateDetails(): Boolean {
        var validate = true

        confirm_password = Objects.requireNonNull(binding.etConfirmPassword.text).toString()
        password = Objects.requireNonNull(binding.etPassword.text).toString()

        if (TextUtils.isEmpty(password)) {
            validate = false
            binding.tvHintPassword.hide()
            binding.seekbarPassword.hide()
            passwordMeterClass?.setAllError()
            setErrortoEditText(context, binding.etPassword, binding.textInputLayoutPassword, getString(R.string.validation_no_password))
        } else if (!passwordMeterClass!!.setAllError()) {
            validate = false
        } else if (TextUtils.isEmpty(confirm_password)) {
            validate = false
            setErrortoEditText(context, binding.etConfirmPassword,binding.textInputLayoutConfirmPassword,getString(R.string.validation_no_confirmPassword))
        } else if (password != confirm_password) {
            validate = false
            setErrortoEditText(context,binding.etConfirmPassword,binding.textInputLayoutConfirmPassword,getString(R.string.validation_match_password))
        }
        return validate
    }

    private fun initObserver() {
        viewModel.resetPasswordResponse.observe(this) {

            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(requireContext(), false);
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    if (it.value.code == 200){
                        CustomViews.showSuccessToast(layoutInflater, it.value.message)
                        goBack()
                    } else{
                        CustomViews.showFailToast(layoutInflater, it.value.error?.userMessage)
                    }
                }

                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    CustomViews.showFailToast(layoutInflater, getString(R.string.something_went_wrong))
                }
            }
        }
    }


    private fun goBack() {
        requireActivity().finish()
    }

    private fun setParamsToCallApi() {
        val params: MutableMap<String?, Any?> = HashMap()
        params["email"] = email
        params["otp"] = otp
        params["password"] = password
        params["user_type"] = Constants.ENTITY_TYPE

        viewModel.resetPassword(params)
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
                binding.etPassword.setBackgroundResource(R.drawable.text_box_underline)
            }
        }
    }

    private fun animateSeekbar(progress: Int, color: Int, type: String?) {
        binding.seekbarPassword.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))
        binding.seekbarPassword.show()
        binding.etPassword.setBackgroundResource(0)
        binding.tvHintPassword.hide()
        binding.tvPasswordType.show()
        binding.tvPasswordType.text = type
        binding.tvPasswordType.setTextColor(ContextCompat.getColor(requireContext(), color))

        if (lastProgress != progress) {
            binding.seekbarPassword.setProgress(lastProgress, progress)
            lastProgress = progress
        }
    }


}