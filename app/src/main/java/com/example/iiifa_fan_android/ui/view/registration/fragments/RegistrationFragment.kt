package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iiifa_fan_android.BuildConfig
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.Error
import com.example.iiifa_fan_android.data.network.MainApiResponseInterface
import com.example.iiifa_fan_android.databinding.FragmentRegistrationBinding
import com.example.iiifa_fan_android.ui.view.base.BaseFragment
import com.example.iiifa_fan_android.ui.view.registration.dilogs.EmailAlreadyExist
import com.example.iiifa_fan_android.ui.viewmodel.RegistrationViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.enableDisable
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.google.gson.JsonObject
import java.util.*

class RegistrationFragment : BaseFragment(), MainApiResponseInterface {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var navController: NavController
    private var email: String = ""
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
        addTextWatcher()
    }

    private fun initViews() {
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_main)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun onBack() {
        requireActivity().finish()
    }

    private fun initListener() {
        binding.progressHorizontal.setProgress(0, 20)
        binding.btnNext.onClick {
            if (validateFields()){
                binding.tvTermsAndCondition.enableDisable(false)
                setParams()
            }
        }
    }

    private fun setParams() {
        CustomViews.startButtonLoading(requireContext(), false)
        val stringObjectHashMap = HashMap<String, Any?>()
        stringObjectHashMap["email"] = email
        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        mainApiCall.getData(stringObjectHashMap, Constants.CHECK_USER_EXIST, this)
    }

    private fun goNextToSecond(wait_time: Int) {
        val bundle = Bundle()
        bundle.putInt("wait_time", wait_time)
        bundle.putString("email", email)
        bundle.putString("action_type", Constants.REGISTRATION)
        registrationViewModel.sendEmail(email)
        navController.navigate(R.id.action_registrationFragment_to_verificationCodeFragment, bundle)
    }

    private fun validateFields(): Boolean {
        var validate = true
        email = Objects.requireNonNull(binding.etEmail.text).toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            validate = false
            CustomViews.setErrortoEditText(requireContext(), binding.etEmail, binding.textInputLayoutEmail, getString(R.string.validation_no_username))
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validate = false
            CustomViews.setErrortoEditText(requireContext(), binding.etEmail, binding.textInputLayoutEmail, getString(R.string.validation_invalid_username) )
        }
        return validate
    }

    private fun addTextWatcher() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                CustomViews.removeError(requireContext(), binding.etEmail, binding.textInputLayoutEmail)
            }
        })
    }

    /*
    * API response success
    * */
    override fun onSuccess(successResponse: JsonObject?, apiName: String?) {
        when (apiName) {
            Constants.CHECK_USER_EXIST -> {
                if (successResponse?.get(Constants.DATA)?.asBoolean == true) {
                    setParamsForGenerateOTP()
                }
            }
            Constants.SEND_RESEND_OTP -> {
                CustomViews.hideButtonLoading()
                binding.tvTermsAndCondition.isClickable = true
                binding.tvTermsAndCondition.isEnabled = true
                if (BuildConfig.FLAVOR.contains("dev")) {
                    CustomViews.showSuccessToast(layoutInflater,successResponse?.get("otp")?.toString())
                }
                successResponse?.get("wait_time")?.let { goNextToSecond(it.asInt) }
            }
        }
    }

    /*
    * API response Failure
    * */
    override fun onFailure(failureMessage: Error?, apiName: String?) {
        binding.tvTermsAndCondition.enableDisable(true)
        CustomViews.hideButtonLoading()
        when (apiName) {
            Constants.CHECK_USER_EXIST -> {
                if (failureMessage?.errorType == Constants.EMAIL_ALREADY_REGISTERED) {
                    showEmailAlreadyExistError()
                }
            }
            else -> {
                CustomViews.showFailToast(layoutInflater, failureMessage?.message)
            }
        }
    }

    private fun showEmailAlreadyExistError() {
        val emailAlreadyExist = EmailAlreadyExist(requireContext(), email)
        emailAlreadyExist.show()
    }

    private fun setParamsForGenerateOTP() {
        val stringObjectMap = HashMap<String, Any?>()
        stringObjectMap["email"] = email
        stringObjectMap["entity_type"] = Constants.ENTITY_TYPE
        stringObjectMap["action_type"] = Constants.REGISTRATION
        mainApiCall.getData(stringObjectMap, Constants.SEND_RESEND_OTP, this)
    }


}