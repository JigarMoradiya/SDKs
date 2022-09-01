package com.example.iiifa_fan_android.ui.view.login.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.FanUser
import com.example.iiifa_fan_android.databinding.FragmentVerificationCodeBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.dashboard.MainDashboardActivity
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews.hideButtonLoading
import com.example.iiifa_fan_android.utils.CustomViews.showFailToast
import com.example.iiifa_fan_android.utils.CustomViews.showSuccessToast
import com.example.iiifa_fan_android.utils.CustomViews.startButtonLoading
import com.example.iiifa_fan_android.utils.Resource
import com.example.iiifa_fan_android.utils.extensions.enableDisable
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mukesh.OnOtpCompletionListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.collections.HashMap


@AndroidEntryPoint
class VerificationCodeActivity : BaseActivity() {

    lateinit var phone: String
    lateinit var action_type: String
    private var otp: String? = null
    private var user_id: String? = null
    private var enteredOtp: kotlin.String? = null
    private var time: Long = 0
    private var countDownTimer: CountDownTimer? = null
    lateinit var binding: FragmentVerificationCodeBinding
    private val viewModel by viewModels<CommonViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentVerificationCodeBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        initViews()
        starObserver()
    }


    companion object {
        @JvmStatic
        fun getInstance(context: Context?,wait_time: Long,otp: String,action_type: String,email: String,user_id: String?){
            Intent(context, VerificationCodeActivity::class.java).apply {
                putExtra("wait_time", wait_time)
                putExtra("otp", otp)
                putExtra("action_type", action_type)
                putExtra("email", email)
                putExtra("user_id", user_id)
                context?.startActivity(this)
            }
        }
    }


    fun initViews() {
        phone = intent.getStringExtra("email").toString()
        user_id = intent.getStringExtra("user_id").toString()
        time = intent.getLongExtra("wait_time", 0L)
        action_type = intent.getStringExtra("action_type").toString()
        val otp_data = intent.getStringExtra("otp").toString()


        if (BuildConfig.FLAVOR.contains("dev") || BuildConfig.FLAVOR.contains("test") || BuildConfig.FLAVOR.contains("demo")
        ) {
            showSuccessToast(layoutInflater, otp_data)
        }

        if (phone.isBlank() || time == 0L || action_type.isBlank()) {
            finish()
        }

        binding.progressHorizontal.hide()
        binding.otpView.setOtpCompletionListener { otp_data ->
            otp = otp_data
        }
        binding.btnVerify.onClick {
            if (validateDetails()) {
                setParamsForValidateOTP()
            }
        }
        binding.btnResend.onClick { setParamsForGenerateOTP() }
        time = TimeUnit.SECONDS.toMillis(time)

        binding.ibBack.onClick {
            finish()
        }
    }


    private fun starObserver() {
        viewModel.validateOtpResponse.observe(this, androidx.lifecycle.Observer {

            when (it) {
                is Resource.Loading -> {
                    startButtonLoading(this)
                }
                is Resource.Success -> {
                    hideButtonLoading()
                    if (it.value.code == 200) {


                        //saving the validate OTP access token in storage to later confirm with registration process
                        // we will get access token only in registation process, else we will get null
                        val gson = GsonBuilder().create()
                        val data = gson.fromJson(it.value.content!![Constants.DATA],FanUser::class.java)
                        setLearner(data)

                    } else
                        showFailToast(layoutInflater, it.value.message)
                }

                is Resource.Failure -> {
                    hideButtonLoading()
                    showFailToast(layoutInflater, it.errorBody.toString())
                }
            }
        })


        viewModel.sendResendOtpResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    startButtonLoading(this);
                }
                is Resource.Success -> {
                    hideButtonLoading()
                    if (it.value.code == 200) {
                        if (BuildConfig.FLAVOR.contains("dev") || BuildConfig.FLAVOR.contains("test") || BuildConfig.FLAVOR.contains(
                                "demo"
                            )
                        ) {
                            showSuccessToast(layoutInflater, it.value.content!!["otp"].toString())
                        }
                        time =
                            TimeUnit.SECONDS.toMillis(it.value.content!!["wait_time"].asInt.toLong())
                        displayRemainigTime()
                    } else
                        showFailToast(layoutInflater, it.value.message)
                }

                is Resource.Failure -> {
                    hideButtonLoading()
                    showFailToast(layoutInflater, it.errorBody.toString())
                }
            }
        }

    }


    private fun validateDetails(): Boolean {
        otp = binding.otpView.getText().toString()
        var valid = true
        if (otp == null || TextUtils.isEmpty(otp)) {
            showFailToast(layoutInflater, getString(R.string.validation_no_OTP))
            valid = false
        } else if (otp?.length != 6) {
            showFailToast(layoutInflater, getString(R.string.validation_no_OTP))
            valid = false
        }
        return valid
    }

    private fun setParamsForValidateOTP() {
        startButtonLoading(this, true)
        val stringObjectHashMap: MutableMap<String?, Any?> = HashMap()
        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        enteredOtp = otp
        stringObjectHashMap["otp"] = otp
        stringObjectHashMap["action_type"] = action_type
        stringObjectHashMap["device_id"] = CustomFunctions.getDeviceId()
        stringObjectHashMap["device_name"] = CustomFunctions.getDeviceName()
        stringObjectHashMap["user_id"] = user_id
        viewModel.validateOtp(stringObjectHashMap)
    }

    private fun setParamsForGenerateOTP() {
        startButtonLoading(this, true)
        val stringObjectHashMap: MutableMap<String?, Any?> = HashMap()
        stringObjectHashMap["entity_type"] = Constants.ENTITY_TYPE
        stringObjectHashMap["action_type"] = action_type
        stringObjectHashMap["device_id"] = CustomFunctions.getDeviceId()
        stringObjectHashMap["user_id"] = user_id

        viewModel.sendResendOTP(stringObjectHashMap)
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
       // registerBroadcastReceiver()
    }


    override fun onStop() {
        otp = null
        super.onStop()
      //  this.unregisterReceiver(smsBroadcastReceiver)
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
    }

    override fun onPause() {
        super.onPause()
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        hideButtonLoading()
    }

    override fun onResume() {
        super.onResume()
        if (countDownTimer == null) {
            binding.btnResend.enableDisable(false)
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
                    binding.btnResend.enableDisable(true)
                }
            }.start()
        }
    }


    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {
            //   Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
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




    private fun setLearner(expert: FanUser?) {
//        prefManager!!.user = Gson().toJson(expert)
//        prefManager!!.userEmail = expert?.email
//        prefManager!!.token = expert?.secret
//        prefManager!!.userId = expert?.id
//        MainDashboardActivity.getInstance(this)
//        finish()
    }

}