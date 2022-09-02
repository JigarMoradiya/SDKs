package com.example.iiifa_fan_android.ui.view.registration.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.MultiSelect
import com.example.iiifa_fan_android.databinding.BottomPopUpSingleselectionBinding
import com.example.iiifa_fan_android.databinding.FragmentPersonalDetailsBinding
import com.example.iiifa_fan_android.ui.view.base.BaseFragment
import com.example.iiifa_fan_android.ui.view.commonviews.adapters.SingleSelectAdapter
import com.example.iiifa_fan_android.ui.view.commonviews.interfaces.SingleSelectClickListner
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.ui.viewmodel.RegistrationViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.NoChangingBackgroundTextInputLayout
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.setProgress
import com.example.iiifa_fan_android.utils.extensions.show
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class PersonalDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentPersonalDetailsBinding
    private lateinit var navController: NavController
    private var first_name: String = ""
    private var last_name:String = ""
    private var age:String = ""
    private var gender:String = ""
    private var phone:String = ""
    private var country:String = ""
    private val registrationViewModel by activityViewModels<RegistrationViewModel>()
    private val viewModel by activityViewModels<CommonViewModel>()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPersonalDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
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
        Log.e("personalDetailFG","registrationViewModel token : "+registrationViewModel.token.value)

        binding.progressHorizontal.setProgress(45, 75)
        addTextWatcher(binding.etFirstName, binding.textInputLayoutFirstName)
        addTextWatcher(binding.etLastName, binding.textInputLayoutLastName)
        addTextWatcher(binding.etAge, binding.textInputLayoutAge)
        addTextWatcher(binding.etPhone, binding.textInputLayoutPhone)
        addTextWatcher(binding.spinnerGender, binding.textInputLayoutGender)
        binding.ibBack.onClick {
            onBack()
        }
        binding.btnLogin.onClick {
            setData()
        }
        binding.spinnerGender.onClick {
            showCreateContentPopup()
        }
    }

    private fun setData() {
        if (validateFields()) {
            val code = binding.etLastCode.text.toString()
            registrationViewModel.setPersonalDetails(first_name, last_name,phone, age.toInt(), gender, code)
//            if (TextUtils.isEmpty(code)) {
                goToCreatePassword()
//            } else {
//                validateReferralCode()
//            }
        }
    }

    private fun goToCreatePassword() {
        navController.navigate(R.id.action_personalDetailsFragment_to_passwordFragment)
    }

    private fun validateReferralCode() {
        CustomViews.startButtonLoading(requireContext(), false)
        val params: MutableMap<String?, Any?> = HashMap()
        params["email"] = registrationViewModel.email.value
        params["referral_code"] = registrationViewModel.referral_code.value
//        viewModel.validateReferralCode(params)
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

    private fun validateFields(): Boolean {
        var validate = true
        first_name = Objects.requireNonNull(binding.etFirstName.text).toString().trim { it <= ' ' }
        last_name = Objects.requireNonNull(binding.etLastName.text).toString().trim { it <= ' ' }
        age = Objects.requireNonNull(binding.etAge.text).toString().trim { it <= ' ' }
        if (binding.etPhone.text != null && !TextUtils.isEmpty(binding.etPhone.text.toString())) {
            if (binding.ccp.selectedCountryCodeWithPlus != null) {
                phone = binding.ccp.selectedCountryCodeWithPlus.toString() + " " + binding.etPhone.text.toString()
                country = binding.ccp.selectedCountryName
            } else phone = ""
        } else phone = ""
        if (TextUtils.isEmpty(first_name)) {
            validate = false
            CustomViews.setErrortoEditText(context, binding.etFirstName, binding.textInputLayoutFirstName,getString(R.string.validation_no_firstName))
        } else if (TextUtils.isEmpty(last_name)) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etLastName,binding.textInputLayoutLastName,getString(R.string.validation_no_lastName))
        } else if (!Regex(Constants.ONLY_ALPHABETS).matches(first_name)) {
            validate = false
            CustomViews.setErrortoEditText(context, binding.etFirstName, binding.textInputLayoutFirstName, getString(R.string.validation_invalid_first_name))
        } else if (!Regex(Constants.ONLY_ALPHABETS).matches(last_name)) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etLastName,binding.textInputLayoutLastName,getString(R.string.validation_invalid_last_name))
        } else if (first_name.length > 40) {
            validate = false
            CustomViews.setErrortoEditText(context, binding.etFirstName, binding.textInputLayoutFirstName,getString(R.string.validation_first_name_length))
        } else if (last_name.length > 40) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etLastName,binding.textInputLayoutLastName,getString(R.string.validation_last_name_length))
        } else if (!Regex(Constants.WHITE_LIST_CHARCTER).matches(first_name)) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etFirstName,binding.textInputLayoutFirstName,getString(R.string.validation_invalid_first_name))
        } else if (!Regex(Constants.WHITE_LIST_CHARCTER).matches(last_name)) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etLastName,binding.textInputLayoutLastName,getString(R.string.validation_invalid_last_name))
        } else if (TextUtils.isEmpty(age)) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etAge,binding.textInputLayoutAge,getString(R.string.validation_no_age))
        } else if (age.toInt() == 0) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etAge,binding.textInputLayoutAge,getString(R.string.validation_age_incorrect))
        } else if (age.toInt() < 18 || age.toInt() > 80) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etAge,binding.textInputLayoutAge,getString(R.string.age_number_validation))
        } else if (binding.spinnerGender.text == null || TextUtils.isEmpty(binding.spinnerGender.text.toString())|| binding.spinnerGender.text.toString()==Constants.GENDER[0]) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.spinnerGender,binding.textInputLayoutGender,getString(R.string.validation_no_gender))
        } else if (TextUtils.isEmpty(phone)) {
            validate = false
            CustomViews.setErrortoEditText(context,binding.etPhone,binding.textInputLayoutPhone,getString(R.string.validation_invalid_mobile_no))
        }
        if (validate) {
            gender = binding.spinnerGender.text.toString().lowercase(Locale.getDefault())
        }
        return validate
    }

    private fun addTextWatcher(edtText : AppCompatEditText,til : NoChangingBackgroundTextInputLayout) {
        edtText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                CustomViews.removeError(requireContext(), edtText, til)
            }
        })
    }
}