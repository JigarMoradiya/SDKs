package com.example.iiifa_fan_android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiifa_fan_android.data.repositories.CommonRepository
import com.example.iiifa_fan_android.utils.MainAPIResponse
import com.example.iiifa_fan_android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(private val repository: CommonRepository) :
    ViewModel() {

    private val _sendResendOtpResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val sendResendOtpResponse: LiveData<Resource<MainAPIResponse>> get() = _sendResendOtpResponse
    fun sendResendOTP(params: Map<String?, Any?>?) = viewModelScope.launch {
        _sendResendOtpResponse.value = Resource.Loading
        _sendResendOtpResponse.value = repository.sendResendOTP(params)
    }


    private val _logoutResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val logoutResponse: LiveData<Resource<MainAPIResponse>> get() = _logoutResponse
    fun logoutUser(params: Map<String?, Any?>?) = viewModelScope.launch {
        _logoutResponse.value = Resource.Loading
        _logoutResponse.value = repository.logoutUser(params)
    }

    private val _validateOtpResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val validateOtpResponse: LiveData<Resource<MainAPIResponse>> get() = _validateOtpResponse
    fun validateOtp(
        params: Map<String?, Any?>?
    ) = viewModelScope.launch {
        _validateOtpResponse.value = Resource.Loading
        _validateOtpResponse.value = repository.validateOtp(params)
    }
}