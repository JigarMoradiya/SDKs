package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.Error
import com.example.iiifa_fan_android.data.network.MainApiResponseInterface
import com.example.iiifa_fan_android.databinding.FragmentVerificationCodeBinding
import com.example.iiifa_fan_android.ui.view.base.BaseFragment
import com.example.iiifa_fan_android.ui.viewmodel.RegistrationViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.JsonObject
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class VerificationCodeFragment : BaseFragment(), MainApiResponseInterface {
    private lateinit var binding: FragmentVerificationCodeBinding
    private lateinit var navController: NavController
    private lateinit var email: String
    private lateinit var action_type: String
    private var otp: String? = null
    private var enteredOtp: String? = null
    private var time: Long = 0
    private var countDownTimer: CountDownTimer? = null
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email")?:""
        time = (arguments?.getInt("wait_time")?:90).toLong()
        action_type = arguments?.getString("action_type")?:""
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

        navController = Navigation.findNavController(requireActivity(), R.id.fragment_main)
        time = TimeUnit.SECONDS.toMillis(time)
        displayRemainigTime()
        startSmsUserConsent()
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
        val stringObjectHashMap = java.util.HashMap<String, Any?>()
        stringObjectHashMap["email"] = email
        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        enteredOtp = otp
        stringObjectHashMap["otp"] = otp

        stringObjectHashMap["action_type"] = action_type
        mainApiCall.getData(stringObjectHashMap, Constants.VALIDATE_OTP, this)
    }

    private fun setParamsForGenerateOTP() {
        CustomViews.startButtonLoading(requireActivity(), true)
        val stringObjectHashMap = HashMap<String, Any?>()
        stringObjectHashMap["email"] = email
        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        stringObjectHashMap["action_type"] = action_type
        mainApiCall.getData(stringObjectHashMap,Constants.SEND_RESEND_OTP,this)
    }

    private fun displayRemainigTime() {
        binding.btnResend.isEnabled = false
        //  tvTimerValidity.setText("Valid for " + TimeUnit.MILLISECONDS.toMinutes(time) + " minutes");
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(time_data: Long) {
                time = time_data
                val minutes: Long = time / 1000 / 60
                val seconds: Long = time / 1000 % 60
                val output = String.format("%02d : %02d", minutes, seconds)
                binding.btnResend.text = getString(R.string.resend) + " in " + output
            }

            override fun onFinish() {
                binding.btnResend.text = getString(R.string.resend)
                binding.btnResend.isEnabled = true
                binding.btnResend.isEnabled = true
            }
        }.start()
    }

    override fun onStop() {
        super.onStop()
        otp = null
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }

    override fun onPause() {
        super.onPause()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        CustomViews.hideButtonLoading()
    }

    override fun onResume() {
        super.onResume()
        if (countDownTimer == null) {
            binding.btnResend.isEnabled = false
            //  tvTimerValidity.setText("Valid for " + TimeUnit.MILLISECONDS.toMinutes(time) + " minutes");
            countDownTimer = object : CountDownTimer(time, 1000) {
                override fun onTick(time_data: Long) {
                    time = time_data
                    val minutes = time / 1000 / 60
                    val seconds = time / 1000 % 60
                    val output = String.format("%02d : %02d", minutes, seconds)
                    binding.btnResend.text = getString(R.string.resend) + " in " + output
                }

                override fun onFinish() {
                    binding.btnResend.text = getString(R.string.resend)
                    binding.btnResend.isEnabled = true
                    binding.btnResend.isEnabled = true
                }
            }.start()
        }
    }


    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(context)
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


    /*
    * API response success
    * */
    override fun onSuccess(successResponse: JsonObject?, apiName: String?) {
        when (apiName) {
            Constants.VALIDATE_OTP -> {
                CustomViews.hideButtonLoading()
                if (successResponse?.get(Constants.DATA)?.asBoolean == true) {
                    Log.d("validate_otp", successResponse.toString())
                    //saving the validate OTP access token in storage to later confirm with registration process
                    // we will get access token only in registation process, else we will get null
                    if (successResponse.has("access_token")) {
                        val access_token = successResponse["access_token"].asString
                        registrationViewModel.sendToken(access_token)
                    }
                    goNextToThird()
                }
            }
            Constants.SEND_RESEND_OTP -> {
                CustomViews.hideButtonLoading()
                if (BuildConfig.FLAVOR.contains("dev")) {
                    CustomViews.showSuccessToast(layoutInflater,successResponse?.get("otp")?.toString())
                }
                time = successResponse?.get("wait_time")?.asInt?.let { TimeUnit.SECONDS.toMillis(it.toLong()) }?:90
                displayRemainigTime()
            }
        }
    }

    /*
    * API response Failure
    * */
    override fun onFailure(failureMessage: Error?, apiName: String?) {
        CustomViews.hideButtonLoading()
        CustomViews.showFailToast(layoutInflater, failureMessage?.message)
    }

    private fun goNextToThird() {
        if (action_type == Constants.REGISTRATION){
            navController.navigate(R.id.action_verificationCodeFragment_to_personalDetailsFragment2)
        }
    }

}