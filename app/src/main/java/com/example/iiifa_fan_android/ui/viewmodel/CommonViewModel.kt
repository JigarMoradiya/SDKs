package com.example.iiifa_fan_android.ui.viewmodel

import androidx.lifecycle.*
import com.example.iiifa_fan_android.data.models.MainAPIResponse
import com.example.iiifa_fan_android.data.repositories.CommonRepository
import com.example.iiifa_fan_android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(private val repository: CommonRepository) : ViewModel() {
    val currentPosition = MutableLiveData<Int>()
    val filePath = MutableLiveData<String>()
    val fileExtension = MutableLiveData<String>()
    val fileType = MutableLiveData<String>()
    val imageFile = MutableLiveData<File>()
    val position = MutableLiveData<Int>()
    val isPublicAssets = MutableLiveData<Boolean>()


    private val _checkUserExistsResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val checkUserExistsResponse: LiveData<Resource<MainAPIResponse>> get() = _checkUserExistsResponse
    fun checkUserExists(params: Map<String?, Any?>?) = viewModelScope.launch {
        _checkUserExistsResponse.value = Resource.Loading
        _checkUserExistsResponse.value = repository.checkUserExists(params)
    }

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
    fun validateOtp(params: Map<String?, Any?>?) = viewModelScope.launch {
        _validateOtpResponse.value = Resource.Loading
        _validateOtpResponse.value = repository.validateOtp(params)
    }

    private val _resetPasswordResponse: MediatorLiveData<Resource<MainAPIResponse>> = MediatorLiveData()
    val resetPasswordResponse: LiveData<Resource<MainAPIResponse>> get() = _resetPasswordResponse
    fun resetPassword(params: Map<String?, Any?>? ) = viewModelScope.launch {
        _resetPasswordResponse.value = Resource.Loading
        _resetPasswordResponse.value = repository.resetPassword(params)
    }

    private val _changePasswordResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val changePasswordResponse: LiveData<Resource<MainAPIResponse>> get() = _changePasswordResponse
    fun changePassword(params: Map<String?, Any?>? ) = viewModelScope.launch {
        _changePasswordResponse.value = Resource.Loading
        _changePasswordResponse.value = repository.changePassword(params)
    }

    private val _getSignedObjectPutUrlResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val getSignedObjectPutUrlResponse: LiveData<Resource<MainAPIResponse>> get() = _getSignedObjectPutUrlResponse
    fun getSignedObjectPutUrl(params: Map<String?, Any?>?) = viewModelScope.launch {
        _getSignedObjectPutUrlResponse.value = Resource.Loading
        _getSignedObjectPutUrlResponse.value = repository.getSignedPutObjectUrl(params)
    }
    fun updateFileName(pos: Int, url: String) {
        currentPosition.value = pos
        filePath.value = url
    }
    fun setUrlAndPosition(
        pos: Int, url: String, file_extension: String,
        file_type: String, image_file: File, is_public_assets: Boolean = false
    ) {
        currentPosition.value = pos
        filePath.value = url
        fileExtension.value = file_extension
        fileType.value = file_type
        imageFile.value = image_file
        isPublicAssets.value = is_public_assets
    }


}