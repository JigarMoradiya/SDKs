package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.Error
import com.example.iiifa_fan_android.data.network.MainApiResponseInterface
import com.example.iiifa_fan_android.databinding.FragmentPasswordBinding
import com.example.iiifa_fan_android.ui.view.base.BaseFragment
import com.example.iiifa_fan_android.ui.view.commonviews.classes.PasswordMeterClass
import com.example.iiifa_fan_android.ui.viewmodel.RegistrationViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.example.iiifa_fan_android.utils.extensions.show
import com.google.gson.JsonObject
import java.util.*

class PasswordFragment : BaseFragment(), MainApiResponseInterface {
    private lateinit var binding: FragmentPasswordBinding
    private lateinit var navController: NavController
    private lateinit var passwordMeterClass: PasswordMeterClass
    private var type: String? = null
    private var password: String = ""
    private var confirm_password: String = ""
    private var lastProgress = 0
    private val registrationViewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
    }

    private fun initViews() {
        passwordMeterClass = PasswordMeterClass(requireActivity())
        passwordMeterClass.setAdapter(binding.rvPassword)
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_main)
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
        binding.progressHorizontal.setProgress(70, 95)
        binding.ibBack.onClick {
            onBack()
        }
        binding.btnNext.onClick {
            if (validateFields()){
                addFan()
            }
        }
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.textInputLayoutPassword.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.colorAccent))
                CustomViews.removeError(requireContext(),binding.etPassword,binding.textInputLayoutPassword,0,R.color.text_color,true)
                val len = s.length

                //if user erase everything , then hide hint as well
                if (len == 0) {
                    binding.seekbarPassword.hide()
                    binding.tvHintPassword.hide()
                    binding.tvPasswordType.hide()
                    passwordMeterClass.setResetAll()
                } else {
                    binding.seekbarPassword.show()
                    passwordMeterClass.atLeast8charSelected(s.toString().length >= 8)
                    passwordMeterClass.numericCharSelected(CustomFunctions.containsNumericCharacter(s.toString()))
                    passwordMeterClass.smallCharSelected(CustomFunctions.containsLowerCase(s.toString()))
                    passwordMeterClass.capitalLetterSelected(CustomFunctions.containsUpperCase(s.toString()))

                    //check basic condition
                    if (passwordMeterClass.isAllSelected()) {
                        type = if (!CustomFunctions.containsSpecialCharacter(s.toString())) {
                            if (s.length < 12) {
                                Constants.WEAK
                            } else if (s.length < 16) {
                                Constants.MODERATED
                            } else {
                                Constants.STRONG
                            }
                        } else {
                            if (s.length < 12) {
                                Constants.MODERATED
                            } else {
                                Constants.STRONG
                            }
                        }
                    } else {
                        type = Constants.WEAK
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
                CustomViews.removeError(requireContext(),binding.etConfirmPassword,binding.textInputLayoutConfirmPassword,0,R.color.text_color,true)
            }
        })
    }

    private fun addFan() {
//        val bundle = Bundle()
//        navController.navigate(R.id.action_passwordFragment_to_selectDecadeFragment, bundle)

        CustomViews.startButtonLoading(context!!, false)
        val params = HashMap<String, Any?>()
        params["first_name"] = registrationViewModel.first_name.value
        params["last_name"] = registrationViewModel.last_name.value
        params["email"] = registrationViewModel.email.value
        params["gender"] = registrationViewModel.gender.value
        params["age"] = registrationViewModel.age.value
        params["access_token"] = registrationViewModel.token.value
        params["referral_code"] = registrationViewModel.referral_code.value
        params["device_id"] = CustomFunctions.getDeviceId()
        params["device_name"] = CustomFunctions.getDeviceName()

        params["phone_number"] = registrationViewModel.phone_no.value

        mainApiCall.getData(params, Constants.ADD_FAN, this)
    }

    private fun validateFields(): Boolean {
        var validate = true
        confirm_password = Objects.requireNonNull(binding.etConfirmPassword.text).toString()
        password = Objects.requireNonNull(binding.etPassword.text).toString()
        if (TextUtils.isEmpty(password)) {
            validate = false
            binding.tvHintPassword.hide()
            binding.seekbarPassword.hide()
            passwordMeterClass.setAllError()
            CustomViews.setErrortoEditText(requireContext(), binding.etPassword, binding.textInputLayoutPassword, getString(R.string.validation_no_password))
        } else if (!passwordMeterClass.setAllError()) {
            validate = false
        } else if (TextUtils.isEmpty(confirm_password)) {
            validate = false
            CustomViews.setErrortoEditText(requireContext(), binding.etConfirmPassword, binding.textInputLayoutConfirmPassword, getString(R.string.validation_no_confirmPassword))
        } else if (password != confirm_password) {
            validate = false
            CustomViews.setErrortoEditText(requireContext(), binding.etConfirmPassword, binding.textInputLayoutConfirmPassword, getString(R.string.validation_match_password))
        }
        return validate
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
            }
        }
    }

    private fun animateSeekbar(progress: Int, color: Int, type: String?) {
        binding.seekbarPassword.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))
        binding.seekbarPassword.show()
        binding.tvHintPassword.hide()
        binding.tvPasswordType.show()
        binding.tvPasswordType.text = type
        binding.tvPasswordType.setTextColor(ContextCompat.getColor(requireContext(), color))
        if (lastProgress != progress) {
            binding.seekbarPassword.setProgress(lastProgress, progress)
            lastProgress = progress
        }
    }

    /*
    * API response success
    * */
    override fun onSuccess(successResponse: JsonObject?, apiName: String?) {
        when (apiName) {
            Constants.ADD_FAN -> {

            }
        }

    }

    /*
    * API response Failure
    * */
    override fun onFailure(failureMessage: Error?, apiName: String?) {

    }

}