package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.FragmentPasswordBinding
import com.example.iiifa_fan_android.ui.view.commonviews.classes.PasswordMeterClass
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.show

class PasswordFragment : Fragment() {
    private lateinit var binding: FragmentPasswordBinding
    private lateinit var navController: NavController
    private lateinit var passwordMeterClass: PasswordMeterClass
    private var type: String? = null
    private var lastProgress = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
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
        binding.ibBack.onClick {
            onBack()
        }
        binding.btnNext.onClick {
            val bundle = Bundle()
            navController.navigate(R.id.action_passwordFragment_to_selectDecadeFragment, bundle)
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
                    binding.seekbar.hide()
                    binding.tvHintPassword.hide()
                    binding.tvPasswordType.hide()
                    passwordMeterClass.setResetAll()
                } else {
                    binding.seekbar.show()
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
                binding.seekbar.hide()
                binding.tvPasswordType.hide()
                binding.tvHintPassword.show()
            }
        }
    }

    private fun animateSeekbar(progress: Int, color: Int, type: String?) {
        binding.seekbar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))
        binding.seekbar.show()
        binding.tvHintPassword.hide()
        binding.tvPasswordType.show()
        binding.tvPasswordType.text = type
        binding.tvPasswordType.setTextColor(ContextCompat.getColor(requireContext(), color))
        if (lastProgress != progress) {
            val anim = ValueAnimator.ofInt(lastProgress, progress)
            anim.duration = 1000
            anim.addUpdateListener { animation ->
                val animProgress = animation.animatedValue as Int
                binding.seekbar.progress = animProgress
            }
            anim.start()
            lastProgress = progress
        }
    }

}