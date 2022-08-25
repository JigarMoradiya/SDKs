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
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.FragmentRegistrationBinding
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import java.util.*

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var navController: NavController
    private var email: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        addTextWatcher()
        return binding.root
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
                goNextToSecond(90)
            }
        }
    }
    private fun goNextToSecond(wait_time: Int) {
        val bundle = Bundle()
        bundle.putInt("wait_time", wait_time)
        bundle.putString("email", binding.etEmail.text.toString())
        bundle.putString("action_type", Constants.REGISTRATION)
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

}