package com.example.iiifa_fan_android.ui.view.forgotpassword.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.FragmentForgotPasswordBinding
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.CustomViews.setErrortoEditText
import com.example.iiifa_fan_android.utils.CustomViews.showSuccessToast
import com.example.iiifa_fan_android.utils.Resource
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {
    private var email: String? = null
    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel by activityViewModels<CommonViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    fun initViews() {
        addTextWatcher()
        setNavGraph()

        binding.btnLogin.setOnClickListener {
            if (validateDetails())
                getOTP()
        }
    }


    private fun validateDetails(): Boolean {
        var valid = true

        email = binding.etEmail.text?.toString()?.trim()

        if (email.isNullOrBlank()) {
            valid = false
            setErrortoEditText(context, binding.etEmail, binding.textInputLayoutEmail,getString(R.string.validation_no_email))
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false
            setErrortoEditText(context,binding.etEmail,binding.textInputLayoutEmail,getString(R.string.validation_invalid_email))
        }
        return valid
    }

    private fun addTextWatcher() {

        binding.btnLogin.setOnClickListener {
            if (validateDetails())
                getOTP()
        }

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                CustomViews.removeError(context, binding.etEmail, binding.textInputLayoutEmail)
            }
        })
    }


    private fun initObserver() {
        viewModel.sendResendOtpResponse.observe(this) {

            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(requireActivity());
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    if (it.value.code == 200)
                        onSuccess(it.value.content)
                    else
                        onFailure(it.value.error?.userMessage)
                }

                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    onFailure(it.errorBody.toString())
                }
            }
        }
    }

    private fun onFailure(error: String?) {
        CustomViews.showFailToast(layoutInflater, error)
    }


    private fun onSuccess(successResponse: JsonObject?) {
        if (BuildConfig.FLAVOR.contains("dev") ||
                BuildConfig.FLAVOR.contains("test") ||
                BuildConfig.FLAVOR.contains("demo")
        ) {
            showSuccessToast(layoutInflater, successResponse!!["otp"].toString())
        }

        //   CustomViews.showSuccessToast(getLayoutInflater(), successResponse.get(Constants.RESPONSE_MESSAGE).getAsString());
        goNextToSecond(successResponse!!["wait_time"].asInt)
    }

    private fun goNextToSecond(wait_time: Int) {
        val bundle = Bundle().apply {
            putInt("wait_time", wait_time)
            putString("email", email)
            putString("action_type", Constants.FORGOT_PASSWORD)
        }

        navController.navigate(R.id.action_forgotFragmentPassword_to_verificationCodeFragment,bundle)
    }

    private fun setNavGraph() {
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_forgot_password_main)
    }


    private fun getOTP() {
        val stringObjectHashMap: MutableMap<String?, Any?> = HashMap()
        stringObjectHashMap["email"] = email!!
        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        stringObjectHashMap["action_type"] = Constants.FORGOT_PASSWORD

        viewModel.sendResendOTP(stringObjectHashMap)
    }


}