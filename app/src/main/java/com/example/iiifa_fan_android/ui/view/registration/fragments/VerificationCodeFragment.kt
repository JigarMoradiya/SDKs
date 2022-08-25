package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.FragmentVerificationCodeBinding
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class VerificationCodeFragment : Fragment() {
    private lateinit var binding: FragmentVerificationCodeBinding
    private lateinit var navController: NavController
    private lateinit var phone: String
    private lateinit var action_type: String
    private var otp: String? = null
    private var user_id: String? = null
    private var enteredOtp: String? = null
    private var time: Long = 0
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phone = arguments?.getString("email")?:""
        time = (arguments?.getInt("wait_time")?:90).toLong()
        action_type = arguments?.getString("action_type")?:""
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentVerificationCodeBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
    }

    private fun initViews() {
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_main)
        time = TimeUnit.SECONDS.toMillis(time)
        displayRemainigTime()
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
//            if (validateDetails()) {
//                setParamsForValidateOTP()
//            }
            val bundle = Bundle()
            navController.navigate(R.id.action_verificationCodeFragment_to_personalDetailsFragment2, bundle)
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
        CustomViews.startButtonLoading(requireContext(), true)
        val stringObjectHashMap: MutableMap<String?, Any?> = HashMap()
//        if (phone.contains("+")) {
//            stringObjectHashMap["phone_number"] = phone
//        } else {
//            stringObjectHashMap["email"] = phone
//        }
        stringObjectHashMap["entity_type"] = ""
        enteredOtp = otp
        stringObjectHashMap["otp"] = otp
        stringObjectHashMap["action_type"] = action_type
        stringObjectHashMap["device_id"] = CustomFunctions.getDeviceId()
        stringObjectHashMap["device_name"] = CustomFunctions.getDeviceName()
        stringObjectHashMap["user_id"] = user_id
//        viewModel.validateOtp(stringObjectHashMap)
    }

    private fun setParamsForGenerateOTP() {
        CustomViews.startButtonLoading(requireContext(), true)
        val stringObjectHashMap: MutableMap<String?, Any?> = HashMap()
//        if (phone.contains("+")) {
//            stringObjectHashMap["phone_number"] = phone
//        } else {
//            stringObjectHashMap["email"] = phone
//        }
//        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        stringObjectHashMap["action_type"] = action_type
        stringObjectHashMap["device_id"] = CustomFunctions.getDeviceId()
        stringObjectHashMap["user_id"] = user_id

//        viewModel.sendResendOTP(stringObjectHashMap)
//        mainApiCall.getData(
//            stringObjectHashMap,
//            Constants.SEND_RESEND_OTP,
//            mainApiResponseInterface
//        )
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
    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }


    override fun onStop() {
        otp = null
        super.onStop()
//        context!!.unregisterReceiver(smsBroadcastReceiver)
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


    private fun registerBroadcastReceiver() {
//        smsBroadcastReceiver = SmsBroadcastReceiver()
//        smsBroadcastReceiver.smsBroadcastReceiverListener =
//            object : SmsBroadcastReceiverListener() {
//                fun onSuccess(intent: Intent?) {
//                    startActivityForResult(intent, Constants.REQUEST_SMS_PERMISSION)
//                }
//
//                fun onFailure() {}
//            }
//        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
//        context!!.registerReceiver(smsBroadcastReceiver, intentFilter)
    }



}