package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.FragmentRegistrationBinding
import com.example.iiifa_fan_android.ui.view.base.BaseFragment
import com.example.iiifa_fan_android.ui.view.registration.dilogs.EmailAlreadyExist
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.ui.viewmodel.RegistrationViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.Resource
import com.example.iiifa_fan_android.utils.extensions.enableDisable
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RegistrationFragment : BaseFragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var navController: NavController
    private var email: String = ""
    private val registrationViewModel by activityViewModels<RegistrationViewModel>()
    private val viewModel by activityViewModels<CommonViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
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
        prefManager.setUserData("")
        binding.progressHorizontal.setProgress(0, 20)
        binding.tvLogin.onClick {
            requireActivity().finish()
        }
        binding.btnNext.onClick {
            if (validateFields()){
                binding.tvTermsAndCondition.enableDisable(false)
                setParams()
            }
        }
    }

    private fun initObserver() {
        viewModel.checkUserExistsResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(requireContext())
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    binding.tvTermsAndCondition.enableDisable(true)
                    when (it.value.code) {
                        200 -> {
                            setParamsForGenerateOTP()
                        }
                        500 -> {
                            showEmailAlreadyExistError()
                        }
                        else -> CustomViews.showFailToast(layoutInflater, it.value.error?.message)
                    }
                }

                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    binding.tvTermsAndCondition.enableDisable(true)
                    CustomViews.showFailToast(layoutInflater, it.errorBody.toString())
                }
            }
        }
        viewModel.sendResendOtpResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(requireContext())
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    binding.tvTermsAndCondition.enableDisable(true)
                    if (it.value.code == 200) {
                        if (BuildConfig.FLAVOR.contains("dev") || BuildConfig.FLAVOR.contains("test") || BuildConfig.FLAVOR.contains("demo")) {
                            CustomViews.showSuccessToast(layoutInflater,it.value.content?.get("otp")?.toString())
                        }
                        val time = it.value.content?.get("wait_time")?.asInt
                        time?.let { it1 -> goNextToSecond(it1) }
                    } else
                        CustomViews.showFailToast(layoutInflater, it.value.message)
                }

                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    binding.tvTermsAndCondition.enableDisable(true)
                    CustomViews.showFailToast(layoutInflater, it.errorBody.toString())
                }
            }
        }

    }

    private fun setParams() {
        CustomViews.startButtonLoading(requireContext(), false)
        val stringObjectMap: MutableMap<String?, Any?> = HashMap()
        stringObjectMap["email"] = email
        stringObjectMap["entity_type"] = Constants.ENTITY_TYPE
        viewModel.checkUserExists(stringObjectMap)
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

    private fun showEmailAlreadyExistError() {
        val emailAlreadyExist = EmailAlreadyExist(requireContext(), email)
        emailAlreadyExist.show()
    }

    private fun setParamsForGenerateOTP() {
        val stringObjectMap: MutableMap<String?, Any?> = HashMap()
        stringObjectMap["email"] = email
        stringObjectMap["entity_type"] = Constants.ENTITY_TYPE
        stringObjectMap["action_type"] = Constants.REGISTRATION
        viewModel.sendResendOTP(stringObjectMap)
    }


}