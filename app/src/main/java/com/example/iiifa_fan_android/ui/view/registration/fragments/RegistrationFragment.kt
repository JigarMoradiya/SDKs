package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.os.Bundle
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
import com.example.iiifa_fan_android.utils.extensions.onClick

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        initViews()
        initListener()
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
        binding.btnNext.onClick {
            goNextToSecond(90)
        }
    }
    private fun goNextToSecond(wait_time: Int) {
        val bundle = Bundle()
        bundle.putInt("wait_time", wait_time)
        bundle.putString("email", binding.etEmail.text.toString())
        bundle.putString("action_type", Constants.REGISTRATION)
        navController.navigate(R.id.action_registrationFragment_to_verificationCodeFragment, bundle)
    }


}