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
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.Error
import com.example.iiifa_fan_android.data.models.ErrorSession
import com.example.iiifa_fan_android.data.models.FanUser
import com.example.iiifa_fan_android.data.models.SocialMediaUserModel
import com.example.iiifa_fan_android.databinding.ActivityLoginBinding
import com.example.iiifa_fan_android.databinding.BottomPopUpMfaBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.dashboard.MainDashboardActivity
import com.example.iiifa_fan_android.ui.view.forgotpassword.ForgotPasswordActivity
import com.example.iiifa_fan_android.ui.view.login.component.GoogleLoginComponent
import com.example.iiifa_fan_android.ui.view.registration.activities.RegistrationHolderActivity
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.ui.viewmodel.FanViewModel
import com.example.iiifa_fan_android.ui.viewmodel.SocialLoginViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.CustomViews.hideButtonLoading
import com.example.iiifa_fan_android.utils.CustomViews.removeError
import com.example.iiifa_fan_android.utils.CustomViews.setErrortoEditText
import com.example.iiifa_fan_android.utils.CustomViews.showFailToast
import com.example.iiifa_fan_android.utils.CustomViews.startButtonLoading
import com.example.iiifa_fan_android.utils.Resource
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginActivity : BaseActivity(), GoogleLoginComponent.StartActivityResult {
    private lateinit var binding: ActivityLoginBinding
    private var email: String = ""
    private var password: String = ""
    private var user_id: String? = null
    private val socialLoginViewModel by viewModels<SocialLoginViewModel>()
    private val viewModel by viewModels<FanViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()
    private val stringObjectMap: MutableMap<String?, Any?> = HashMap()
    companion object {
        @JvmStatic
        fun getInstance(
            context: Context?, is_from_non_logged_in_flow: Boolean? = false, showingAfterLogout: Boolean? = false, email : String? = null
        ) {
            Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("is_from_non_logged_in_flow", is_from_non_logged_in_flow)
                putExtra("showing_after_logout", showingAfterLogout)
                if (email != null){
                    putExtra("email", email)
                }
                context?.startActivity(this)
            }
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
            RegistrationHolderActivity.getInstance(this@LoginActivity)
        }
        binding.tvForgotPassword.onClick {
            ForgotPasswordActivity.getInstance(this@LoginActivity)
        }
        binding.btnLogin.onClick {
            if (validateFields()){
                setParams()
            }
        }
    }

    private fun setParams() {
        startButtonLoading(this, false)
        if (stringObjectMap.containsKey("type")) {
            loginWithSocialMdeiaAPI()
        } else {
            stringObjectMap["email"] = email
            stringObjectMap["password"] = password
            if (stringObjectMap.containsKey("first_name")) {
                stringObjectMap.remove("first_name")
            }
            if (stringObjectMap.containsKey("last_name")) {
                stringObjectMap.remove("last_name")
            }
            if (stringObjectMap.containsKey("profile_url")) {
                stringObjectMap.remove("profile_url")
            }
            if (stringObjectMap.containsKey("type")) {
                stringObjectMap.remove("type")
            }
            stringObjectMap["device_id"] = CustomFunctions.getDeviceId()
            stringObjectMap["device_name"] = CustomFunctions.getDeviceName()

            viewModel.login(stringObjectMap)
        }

    }

    private fun loginWithSocialMdeiaAPI() {
        startButtonLoading(this, false)
        if (stringObjectMap.containsKey("password")) stringObjectMap.remove("password")
        if (stringObjectMap.containsKey("phone_number"))
            stringObjectMap.remove("phone_number")
        stringObjectMap["device_id"] = CustomFunctions.getDeviceId()
        stringObjectMap["device_name"] = CustomFunctions.getDeviceName()

        viewModel.socialMediaLogin(stringObjectMap)
    }


    private fun initObserver() {
        socialLoginViewModel.isSocialDataSet.observe(this) {
            if (it) {
                val socialMediaModel = socialLoginViewModel.getSocialMediaLoginData()
                Log.e("loginActivity","socialMediaModel : "+Gson().toJson(socialMediaModel))
                if (!socialMediaModel?.email.isNullOrEmpty()) {
                    loginUsingSocialMedia(socialMediaModel)
                }
            }
        }
        viewModel.socail_media_loginResponse.observe(this, androidx.lifecycle.Observer {

            when (it) {
                is Resource.Loading -> {
                    startButtonLoading(this, false);
                }
                is Resource.Success -> {
                    hideButtonLoading()
                    if (it.value.code == 200) {
                        val data = Gson().fromJson(it.value.content!![Constants.DATA], FanUser::class.java)

                        prefManager.setUserData(Gson().toJson(data))
                        data?.id?.let { prefManager.setUserId(it) }
                        data?.email?.let { prefManager.setUserEmail(it) }
                        data?.secret?.let { prefManager.setToken(it) }

                        if (data.is_complete_profile) {
                            MainDashboardActivity.getInstance(this)
                        } else {
                            //Move to Registration
                            RegistrationHolderActivity.getInstance(this)
                        }
                    }
                    //    onSuccess(it.value.content)
                    else
                        onFailure(it.value.error)
                }

                is Resource.Failure -> {
                    hideButtonLoading()
                    showFailToast(layoutInflater, "Something went wrong, please try after sometime")
                }
            }
        })
        viewModel.loginResponse.observe(this) {

            when (it) {
                is Resource.Loading -> {
                    startButtonLoading(this, false);
                }
                is Resource.Success -> {
                    hideButtonLoading()
                    if (it.value.code == 200)
                        onSuccess(it.value.content)
                    else
                        onFailure(it.value.error)
                }

                is Resource.Failure -> {
                    hideButtonLoading()
                    showFailToast(layoutInflater, getString(R.string.something_went_wrong))
                }
            }
        }

        commonViewModel.sendResendOtpResponse.observe(this) {

            when (it) {
                is Resource.Loading -> {
                    startButtonLoading(this);
                }
                is Resource.Success -> {
                    hideButtonLoading()
                    if (it.value.code == 200)
                        onOtpSuccess(it.value.content)
                    else
                        onFailure(it.value.error)
                }

                is Resource.Failure -> {
                    hideButtonLoading()
                    showFailToast(layoutInflater, it.errorBody.toString())
                }
            }
        }

        commonViewModel.logoutResponse.observe(this) {

            when (it) {
                is Resource.Loading -> {
                    startButtonLoading(this);
                }
                is Resource.Success -> {
                    hideButtonLoading()
                    if (it.value.code == 200)
                        setParams()
                    else
                        onFailure(it.value.error)
                }

                is Resource.Failure -> {
                    hideButtonLoading()
                    showFailToast(layoutInflater, it.errorBody.toString())
                }
            }
        }

    }

    private fun loginUsingSocialMedia(socialMediaModel: SocialMediaUserModel?) {
        if (socialMediaModel?.email.isNullOrEmpty()){
            stringObjectMap["email"] = ""
        }else{
            stringObjectMap["email"] = Objects.requireNonNull(socialMediaModel?.email)
        }
        if (!socialMediaModel?.first_name.isNullOrEmpty()) {
            stringObjectMap["first_name"] = Objects.requireNonNull(socialMediaModel?.first_name)
        }
        if (!socialMediaModel?.last_name.isNullOrEmpty()) {
            stringObjectMap["last_name"] = Objects.requireNonNull(socialMediaModel?.last_name)
        }
        if (!socialMediaModel?.profile_pic.isNullOrEmpty()) {
            stringObjectMap["profile_url"] = Objects.requireNonNull(socialMediaModel?.profile_pic)
        }
        if (socialMediaModel?.social_type == "apple" && !socialMediaModel.social_id.isNullOrEmpty()) {
            stringObjectMap["apple_id"] = Objects.requireNonNull(socialMediaModel.profile_pic)
        }
        stringObjectMap["type"] = Objects.requireNonNull(socialMediaModel?.social_type)
        setParams()
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

    private fun onOtpSuccess(successResponse: JsonObject?) {
        successResponse?.get("otp")?.asString?.let { otp ->
            successResponse.get("wait_time")?.asLong?.let { wait_time ->
                VerificationCodeActivity.getInstance(this, wait_time, otp, Constants.LOGIN, email, user_id)
            }
        }
    }

    private fun onSuccess(successResponse: JsonObject?) {
        val gson = GsonBuilder().create()
        val data = gson.fromJson(successResponse!![Constants.DATA], FanUser::class.java)
        if (data.mfa_required == 1) {
            //show otp screen in case of mfa required
            user_id = data.id
            setParamsToSendOtp(data.id)
        } else {
            setLearner(data)
        }
    }

    private fun setLearner(data: FanUser?) {
        prefManager.setUserData(Gson().toJson(data))
        data?.id?.let { prefManager.setUserId(it) }
        data?.email?.let { prefManager.setUserEmail(it) }
        data?.secret?.let { prefManager.setToken(it) }

        MainDashboardActivity.getInstance(this)
        finish()
    }

    private fun onFailure(failureMessage: Error?) {

        if (failureMessage?.errorType != null) {
            if (failureMessage.errorType.equals(Constants.LOGIN_INTO_ANOTHER_DEVICE)) {
                showFailToast(layoutInflater, failureMessage.userMessage)
            } else if (failureMessage.errorType.equals(Constants.EMAIL_NOT_VERIFIED)
                || failureMessage.errorType.equals(Constants.PHONE_NOT_VERIFIED)
            ) {
                showFailToast(layoutInflater, failureMessage.userMessage)
            } else if (failureMessage.errorType.equals(Constants.PROFILE_IS_SUSPENDED)) {
                showFailToast(layoutInflater, failureMessage.userMessage)
            } else if (failureMessage.errorType.equals(Constants.ACTIVE_SESSION_EXCEEDS)) {
                failureMessage.sessions?.let { showLoggedInDevices(it) }
            } else {
                showFailToast(layoutInflater, failureMessage.userMessage)
            }
        } else {
            showFailToast(layoutInflater, failureMessage?.userMessage)
        }
    }


    private fun setParamsToSendOtp(id: String) {
        startButtonLoading(this)
        val params: MutableMap<String?, Any?> = HashMap()
        params["entity_type"] = Constants.ENTITY_TYPE
        params["action_type"] = Constants.LOGIN
        params["device_id"] = CustomFunctions.getDeviceId()
        params["user_id"] = id
        commonViewModel.sendResendOTP(params)
    }

    private fun showLoggedInDevices(list: ArrayList<ErrorSession>) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val binding = BottomPopUpMfaBinding.inflate(layoutInflater)

        binding.tvClose.onClick { bottomSheetDialog.dismiss() }

        if (list.size == 2) {
            list[0].let {
                if (it.channel == "ios" || it.channel == "android") {
                    binding.layoutOne.ivImage.setImageResource(R.drawable.ic_type_mobile)
                } else {
                    binding.layoutOne.ivImage.setImageResource(R.drawable.ic_type_laptop_device)
                }

                //in this case expert id will be user id
                user_id = it.patient_id
                binding.layoutOne.tvName.text = it.device_name
            }

            list[1].let {
                if (it.channel == "ios" || it.channel == "android") {
                    binding.layoutTwo.ivImage.setImageResource(R.drawable.ic_type_mobile)
                } else {
                    binding.layoutTwo.ivImage.setImageResource(R.drawable.ic_type_laptop_device)
                }

                //in this case expert id will be user id
                user_id = it.patient_id
                binding.layoutTwo.tvName.text = it.device_name
            }

            binding.layoutOne.btnLogin.onClick {
                setParamsToLogout(list[0].id)
            }


            binding.layoutTwo.btnLogin.onClick {
                setParamsToLogout(list[1].id)
            }

        }

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
    }

    /*
    * login api call from all devices
    * */
    private fun setParamsToLogout(id: String) {
        startButtonLoading(this)

        val params: MutableMap<String?, Any?> = HashMap()
        val session_list = ArrayList<String>()
        session_list.add(id)
        params["user_type"] = Constants.ENTITY_TYPE
        params["user_id"] = user_id
        params["login_id"] = session_list
        commonViewModel.logoutUser(params)
    }
}