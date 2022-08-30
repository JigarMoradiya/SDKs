package com.example.iiifa_fan_android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val token = MutableLiveData<String>()
    val first_name = MutableLiveData<String>()
    val last_name = MutableLiveData<String>()
    val phone_no = MutableLiveData<String>()
    val referral_code = MutableLiveData<String>()
    val age = MutableLiveData<Int>()
    val gender = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val emotions_ids = MutableLiveData<List<String>>()


    fun sendEmotionsIds(value: List<String>) {
        emotions_ids.value = value
    }


    fun sendEmail(value: String) {
        email.value = value
    }

    fun sendToken(value: String) {
        token.value = value
    }

    fun sendPassword(value: String) {
        password.value = value
    }

    fun setPersonalDetails(first_name_val: String, last_name_val: String, phone_no_val: String?, age_val: Int, gender_val: String, referral_code_val: String?) {
        first_name.value = first_name_val
        last_name.value = last_name_val

        if (!phone_no_val.isNullOrEmpty())
            phone_no.value = phone_no_val!!

        age.value = age_val
        gender.value = gender_val

        if (!referral_code_val.isNullOrEmpty())
            referral_code.value = referral_code_val!!

    }


}