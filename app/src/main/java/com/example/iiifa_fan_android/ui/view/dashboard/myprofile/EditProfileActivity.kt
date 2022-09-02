package com.example.iiifa_fan_android.ui.view.dashboard.myprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.activityViewModels
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.FanUser
import com.example.iiifa_fan_android.data.models.MultiSelect
import com.example.iiifa_fan_android.databinding.ActivityEditProfileBinding
import com.example.iiifa_fan_android.databinding.BottomPopUpSingleselectionBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.commonviews.adapters.SingleSelectAdapter
import com.example.iiifa_fan_android.ui.view.commonviews.interfaces.SingleSelectClickListner
import com.example.iiifa_fan_android.ui.viewmodel.FanViewModel
import com.example.iiifa_fan_android.utils.*
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.show
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var user: FanUser? = null
    private var first_name: String = ""
    private var last_name:String = ""
    private var email:String = ""
    private var age:String = ""
    private var gender:String = ""
    private var phone:String = ""
    private var country:String = ""
    private val viewModel by viewModels<FanViewModel>()
    companion object {
        fun getInstance(context: Context?) {
            Intent(context, EditProfileActivity::class.java).apply {
                context?.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListener()
        initObserver()
    }

    private fun initViews() {
        user = Gson().fromJson(prefManager.getUserData(), FanUser::class.java)
        binding.dataModel = user
    }

    private fun initListener() {
        addTextWatcher(binding.etFirstName, binding.textInputLayoutFirstName)
        addTextWatcher(binding.etLastName, binding.textInputLayoutLastName)
        addTextWatcher(binding.etEmail, binding.textInputLayoutEmail)
        addTextWatcher(binding.etAge, binding.textInputLayoutAge)
        addTextWatcher(binding.etPhone, binding.textInputLayoutPhone)
        addTextWatcher(binding.spinnerGender, binding.textInputLayoutGender)
        binding.ibBack.onClick { onBackPressed() }
        binding.ivEditProfile.onClick { ChangeProfileActivity.getInstance(this@EditProfileActivity) }
        binding.tvSave.onClick {
            if (validateFields()){
                updateFanProfile()
            }
        }
        binding.spinnerGender.onClick { showCreateContentPopup() }
    }

    private fun initObserver() {
        viewModel.updateFanProfileResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(this@EditProfileActivity, false)
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    if (it.value.code == 200) {
                        CustomViews.hideButtonLoading()
                        val data = Gson().fromJson(it.value.content!![Constants.DATA], FanUser::class.java)
                        prefManager.setUserData(Gson().toJson(data))
                        data?.email?.let { prefManager.setUserEmail(it) }
                        CustomViews.showSuccessToast(layoutInflater, it.value.message)
                    } else{
                        CustomViews.hideButtonLoading()
                        CustomViews.showFailToast(layoutInflater, it.value.message)
                    }
                }
                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    CustomViews.showFailToast(layoutInflater, getString(R.string.something_went_wrong))
                }
            }
        }

    }

    private fun updateFanProfile() {
        CustomViews.startButtonLoading(this, false)
        val params: MutableMap<String?, Any?> = HashMap()
        params["fan_id"] = prefManager.getUserId()
        params["first_name"] = first_name
        params["last_name"] = last_name
        params["gender"] = gender
        params["age"] = age.toIntOrNull()
        params["phone_number"] = phone
        viewModel.updateFanProfile(params)
    }
    private fun showCreateContentPopup() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
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
                CustomViews.removeError(this@EditProfileActivity,binding.spinnerGender,binding.textInputLayoutGender,0,
                    R.color.text_color,true)
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
        email = Objects.requireNonNull(binding.etEmail.text).toString().trim { it <= ' ' }
        age = Objects.requireNonNull(binding.etAge.text).toString().trim { it <= ' ' }
        if (binding.etPhone.text != null && !TextUtils.isEmpty(binding.etPhone.text.toString())) {
            if (binding.ccp.selectedCountryCodeWithPlus != null) {
                phone = binding.ccp.selectedCountryCodeWithPlus.toString() + " " + binding.etPhone.text.toString()
                country = binding.ccp.selectedCountryName
            } else phone = ""
        } else phone = ""
        if (TextUtils.isEmpty(first_name)) {
            validate = false
            CustomViews.setErrortoEditText(this, binding.etFirstName, binding.textInputLayoutFirstName,getString(
                R.string.validation_no_firstName))
        } else if (TextUtils.isEmpty(last_name)) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etLastName,binding.textInputLayoutLastName,getString(
                R.string.validation_no_lastName))
        } else if (!Regex(Constants.ONLY_ALPHABETS).matches(first_name)) {
            validate = false
            CustomViews.setErrortoEditText(this, binding.etFirstName, binding.textInputLayoutFirstName, getString(
                R.string.validation_invalid_first_name))
        } else if (!Regex(Constants.ONLY_ALPHABETS).matches(last_name)) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etLastName,binding.textInputLayoutLastName,getString(
                R.string.validation_invalid_last_name))
        } else if (first_name.length > 40) {
            validate = false
            CustomViews.setErrortoEditText(this, binding.etFirstName, binding.textInputLayoutFirstName,getString(
                R.string.validation_first_name_length))
        } else if (last_name.length > 40) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etLastName,binding.textInputLayoutLastName,getString(
                R.string.validation_last_name_length))
        } else if (!Regex(Constants.WHITE_LIST_CHARCTER).matches(first_name)) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etFirstName,binding.textInputLayoutFirstName,getString(
                R.string.validation_invalid_first_name))
        } else if (!Regex(Constants.WHITE_LIST_CHARCTER).matches(last_name)) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etLastName,binding.textInputLayoutLastName,getString(
                R.string.validation_invalid_last_name))
        }else if (TextUtils.isEmpty(email)) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etEmail,binding.textInputLayoutEmail,getString(R.string.validation_no_username))
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etEmail,binding.textInputLayoutEmail,getString(R.string.validation_invalid_username))
        } else if (TextUtils.isEmpty(age)) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etAge,binding.textInputLayoutAge,getString(
                R.string.validation_no_age))
        } else if (age.toInt() == 0) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etAge,binding.textInputLayoutAge,getString(
                R.string.validation_age_incorrect))
        } else if (age.toInt() < 18 || age.toInt() > 80) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etAge,binding.textInputLayoutAge,getString(
                R.string.age_number_validation))
        } else if (binding.spinnerGender.text == null || TextUtils.isEmpty(binding.spinnerGender.text.toString())|| binding.spinnerGender.text.toString()== Constants.GENDER[0]) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.spinnerGender,binding.textInputLayoutGender,getString(
                R.string.validation_no_gender))
        } else if (TextUtils.isEmpty(phone)) {
            validate = false
            CustomViews.setErrortoEditText(this,binding.etPhone,binding.textInputLayoutPhone,getString(
                R.string.validation_invalid_mobile_no))
        }
        if (validate) {
            gender = binding.spinnerGender.text.toString().lowercase(Locale.getDefault())
        }
        return validate
    }

    private fun addTextWatcher(edtText : AppCompatEditText, til : NoChangingBackgroundTextInputLayout) {
        edtText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                CustomViews.removeError(this@EditProfileActivity, edtText, til)
            }
        })
    }

}