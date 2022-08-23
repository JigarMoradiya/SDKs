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
import com.example.iiifa_fan_android.data.models.MultiSelect
import com.example.iiifa_fan_android.databinding.BottomPopUpSingleselectionBinding
import com.example.iiifa_fan_android.databinding.FragmentPersonalDetailsBinding
import com.example.iiifa_fan_android.ui.view.commonviews.adapters.SingleSelectAdapter
import com.example.iiifa_fan_android.ui.view.commonviews.interfaces.SingleSelectClickListner
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.show
import com.google.android.material.bottomsheet.BottomSheetDialog

class PersonalDetailFragment : Fragment() {
    private lateinit var binding: FragmentPersonalDetailsBinding
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPersonalDetailsBinding.inflate(inflater, container, false)
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
        navController.navigateUp()
    }

    private fun initListener() {
        binding.ibBack.onClick {
            onBack()
        }
        binding.btnLogin.onClick {
            val bundle = Bundle()
            navController.navigate(R.id.action_personalDetailsFragment_to_passwordFragment, bundle)
        }
        binding.spinnerGender.onClick {
            showCreateContentPopup()
        }
    }

    private fun showCreateContentPopup() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val sheetBinding: BottomPopUpSingleselectionBinding = BottomPopUpSingleselectionBinding.inflate(layoutInflater)
        val list: ArrayList<MultiSelect> = ArrayList()
        list.add(MultiSelect("0", Constants.MALE, null, null, null, null))
        list.add(MultiSelect("1", Constants.FEMALE, null, null, null, null))
        list.add(MultiSelect("2", Constants.OTHER, null, null, null, null))
        sheetBinding.tvTitle.setText(R.string.choose_your_gender)
        sheetBinding.tvTitle.show()
        val adapter = SingleSelectAdapter(list, null, false, false, object : SingleSelectClickListner {
                override fun onClicked(selectedItem: MultiSelect) {
                    bottomSheetDialog.dismiss()
                    CustomViews.removeError(requireContext(),binding.spinnerGender,binding.textInputLayoutGender,0,R.color.text_color,true)
                    when (selectedItem.name) {
                        Constants.MALE -> {
                            binding.spinnerGender.setText(Constants.MALE)
                        }
                        Constants.FEMALE -> {
                            binding.spinnerGender.setText(Constants.FEMALE)
                        }
                        Constants.OTHER -> {
                            binding.spinnerGender.setText(Constants.OTHER)
                        }
                    }
                }
            })
        sheetBinding.rvBottomPopUpLanguage.adapter = adapter
        bottomSheetDialog.setContentView(sheetBinding.root)
        bottomSheetDialog.show()
    }
}