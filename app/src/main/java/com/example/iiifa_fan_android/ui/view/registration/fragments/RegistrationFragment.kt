package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.FanUser
import com.example.iiifa_fan_android.data.models.SocialMediaUserModel
import com.example.iiifa_fan_android.databinding.FragmentRegistrationBinding
import com.example.iiifa_fan_android.ui.view.base.BaseFragment
import com.example.iiifa_fan_android.ui.view.dashboard.MainDashboardActivity
import com.example.iiifa_fan_android.ui.view.login.component.GoogleLoginComponent
import com.example.iiifa_fan_android.ui.view.registration.activities.RegistrationHolderActivity
import com.example.iiifa_fan_android.ui.view.registration.dilogs.EmailAlreadyExist
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.ui.viewmodel.FanViewModel
import com.example.iiifa_fan_android.ui.viewmodel.RegistrationViewModel
import com.example.iiifa_fan_android.ui.viewmodel.SocialLoginViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.Resource
import com.example.iiifa_fan_android.utils.extensions.enableDisable
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RegistrationFragment : BaseFragment(), GoogleLoginComponent.StartActivityResult {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var navController: NavController
    private var email: String = ""
    private val socialLoginViewModel by viewModels<SocialLoginViewModel>()
    private val registrationViewModel by activityViewModels<RegistrationViewModel>()
    private val commonViewModel by activityViewModels<CommonViewModel>()
    private val viewModel by viewModels<FanViewModel>()
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

        /**
         * set ViewModel For social login component
         */
        binding.ivFacebook.setViewModel(socialLoginViewModel)
        binding.ivGoogle.setViewModel(socialLoginViewModel)
        binding.ivGoogle.setListener(this)
        binding.ivApple.setViewModel(socialLoginViewModel)
        binding.ivApple.setListener(requireActivity())
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
        socialLoginViewModel.isSocialDataSet.observe(this) {
            if (it) {
                val socialMediaModel = socialLoginViewModel.getSocialMediaLoginData()
                Log.e("loginActivity","socialMediaModel : "+ Gson().toJson(socialMediaModel))
                if (!socialMediaModel?.email.isNullOrEmpty()) {
                    loginUsingSocialMedia(socialMediaModel)
                }
            }
        }
        viewModel.socail_media_loginResponse.observe(this, androidx.lifecycle.Observer {

            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(requireContext(), false)
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    if (it.value.code == 200) {
                        val data = Gson().fromJson(it.value.content!![Constants.DATA], FanUser::class.java)

                        prefManager.setUserData(Gson().toJson(data))
                        data?.id?.let { prefManager.setUserId(it) }
                        data?.email?.let { prefManager.setUserEmail(it) }
                        data?.secret?.let { prefManager.setToken(it) }

                        if (data.is_complete_profile) {
                            MainDashboardActivity.getInstance(requireContext())
                        } else {
                            //Move to Registration
                            RegistrationHolderActivity.getInstance(requireContext())
                        }
                    }
                    //    onSuccess(it.value.content)
                    else{
                        CustomViews.showFailToast(layoutInflater,it.value.error?.message)
                    }
                }

                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    CustomViews.showFailToast(
                        layoutInflater,
                        "Something went wrong, please try after sometime"
                    )
                }
            }
        })
        commonViewModel.checkUserExistsResponse.observe(this) {
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
        commonViewModel.sendResendOtpResponse.observe(this) {
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
    private fun loginUsingSocialMedia(socialMediaModel: SocialMediaUserModel?) {
        val stringObjectMap: MutableMap<String?, Any?> = HashMap()
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
        stringObjectMap["device_id"] = CustomFunctions.getDeviceId()
        stringObjectMap["device_name"] = CustomFunctions.getDeviceName()

        CustomViews.startButtonLoading(requireContext(), false)
        viewModel.socialMediaLogin(stringObjectMap)
    }
    private fun setParams() {
        CustomViews.startButtonLoading(requireContext(), false)
        val stringObjectMap: MutableMap<String?, Any?> = HashMap()
        stringObjectMap["email"] = email
        stringObjectMap["entity_type"] = Constants.ENTITY_TYPE
        commonViewModel.checkUserExists(stringObjectMap)
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
        commonViewModel.sendResendOTP(stringObjectMap)
    }

    /*
  * google login intent launch
  * */
    override fun onStart(signInIntent: Intent?) {
        googleActivityResultLauncher.launch(signInIntent)
    }

    private var googleActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
                binding.ivGoogle.activityResult(activityResult.data)
            }
        }
    override fun showHideProgress(isShow: Boolean) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.ivFacebook.activityResult(requestCode, resultCode, data)
    }

}