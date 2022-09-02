package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
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
import com.example.iiifa_fan_android.databinding.FragmentVerificationCodeBinding
import com.example.iiifa_fan_android.ui.view.base.BaseFragment
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.ui.viewmodel.RegistrationViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.Resource
import com.example.iiifa_fan_android.utils.extensions.enableDisable
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class VerificationCodeFragment : BaseFragment() {
    private lateinit var binding: FragmentVerificationCodeBinding
    private lateinit var navController: NavController
    private lateinit var email: String
    private lateinit var action_type: String
    private var otp: String? = null
    private var enteredOtp: String? = null
    private var time: Long = 0
    private var countDownTimer: CountDownTimer? = null
    private val registrationViewModel by activityViewModels<RegistrationViewModel>()
    private val viewModel by activityViewModels<CommonViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email")?:""
        time = (arguments?.getInt("wait_time")?:90).toLong()
        time = TimeUnit.SECONDS.toMillis(time)
        action_type = arguments?.getString("action_type")?:""
        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentVerificationCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
    }
    private fun initViews() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        if (action_type == Constants.REGISTRATION){
            navController = Navigation.findNavController(requireActivity(), R.id.fragment_main)
        }else if (action_type == Constants.FORGOT_PASSWORD){
            binding.ibBack.hide()
            binding.tvSkip.hide()
            binding.progressHorizontal.hide()
            binding.viewDivider.hide()
            navController = Navigation.findNavController(requireActivity(), R.id.fragment_forgot_password_main)
        }


        displayRemainigTime()
    }

    private fun onBack() {
        navController.navigateUp()
    }

    private fun initListener() {
        binding.progressHorizontal.setProgress(20, 45)
        binding.ibBack.onClick {
            onBack()
        }
        binding.otpView.setOtpCompletionListener { otp_data ->
            otp = otp_data
        }
        binding.btnVerify.onClick {
            if (validateDetails()) {
                setParamsForValidateOTP()
            }
        }
        binding.btnResend.onClick { setParamsForGenerateOTP() }
    }

    private fun initObserver() {
        viewModel.validateOtpResponse.observe(this) {

            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(requireContext())
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    if (it.value.code == 200) {
                        val token = (it.value.content?.get("access_token")?.asString ?: "")
                        registrationViewModel.sendToken(token)
                        goNextToThird()

                    } else
                        CustomViews.showFailToast(layoutInflater, it.value.error?.message)
                }

                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
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
                    if (it.value.code == 200) {
                        if (BuildConfig.FLAVOR.contains("dev") || BuildConfig.FLAVOR.contains("test") || BuildConfig.FLAVOR.contains("demo")) {
                            CustomViews.showSuccessToast(layoutInflater,(it.value.content?.get("otp")?:"").toString())
                        }
                        time = TimeUnit.SECONDS.toMillis((it.value.content?.get("wait_time")?.asInt?:90).toLong())
                        displayRemainigTime()
                    } else
                        CustomViews.showFailToast(layoutInflater, it.value.message)
                }

                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    CustomViews.showFailToast(layoutInflater, it.errorBody.toString())
                }
            }
        }

    }
    private fun validateDetails(): Boolean {
        otp = binding.otpView.text.toString()
        var valid = true
        if (otp == null || TextUtils.isEmpty(otp)) {
            CustomViews.showFailToast(layoutInflater, getString(R.string.validation_no_OTP))
            valid = false
        } else if (otp?.length != 6) {
            CustomViews.showFailToast(layoutInflater, getString(R.string.validation_no_OTP))
            valid = false
        }
        return valid
    }

    private fun setParamsForValidateOTP() {
        CustomViews.startButtonLoading(requireActivity(), true)
        val stringObjectHashMap: MutableMap<String?, Any?> = HashMap()
        stringObjectHashMap["email"] = email
        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        enteredOtp = otp
        stringObjectHashMap["otp"] = otp

        stringObjectHashMap["action_type"] = action_type
        viewModel.validateOtp(stringObjectHashMap)
    }

    private fun setParamsForGenerateOTP() {
        CustomViews.startButtonLoading(requireActivity(), true)
        val stringObjectHashMap: MutableMap<String?, Any?> = HashMap()
        stringObjectHashMap["email"] = email
        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        stringObjectHashMap["action_type"] = action_type
        viewModel.sendResendOTP(stringObjectHashMap)
    }

    private fun displayRemainigTime() {
        binding.btnResend.enableDisable(false)
        //  tvTimerValidity.setText("Valid for " + TimeUnit.MILLISECONDS.toMinutes(time) + " minutes");
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(time_data: Long) {
                time = time_data
                val minutes: Long = time / 1000 / 60
                val seconds: Long = time / 1000 % 60
                val output = String.format("%02d : %02d", minutes, seconds)
                if (isResumed){
                    binding.btnResend.text = getString(R.string.resend) + " in " + output
                }
            }

            override fun onFinish() {
                if (isResumed){
                    binding.btnResend.text = requireActivity().resources.getString(R.string.resend)
                    binding.btnResend.enableDisable(true)
                }
            }
        }.start()
    }

    override fun onStop() {
        super.onStop()
        otp = null
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel()
        countDownTimer = null
    }


    override fun onDestroy() {
        super.onDestroy()
        CustomViews.hideButtonLoading()
    }

    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(requireContext())
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {

        }.addOnFailureListener {
            //   Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
        }
    }


    private fun getOtpFromMessage(message: String) {
        // This will match any 6 digit number in the message
        val pattern = Pattern.compile("(|^)\\d{6}")
        val matcher = pattern.matcher(message)
        if (matcher.find()) {
            binding.otpView.setText(matcher.group(0))
        }
    }

    private fun goNextToThird() {
        countDownTimer?.cancel()
        countDownTimer = null
        if (action_type == Constants.REGISTRATION){
            navController.navigate(R.id.action_verificationCodeFragment_to_personalDetailsFragment2)
        }else if (action_type == Constants.FORGOT_PASSWORD){
            val bundle = Bundle().apply {
                putString("email", email)
                putString("otp", enteredOtp)
            }
            navController.navigate(R.id.action_verificationCodeFragment_to_resetPasswordFragment,bundle)
        }
    }

}