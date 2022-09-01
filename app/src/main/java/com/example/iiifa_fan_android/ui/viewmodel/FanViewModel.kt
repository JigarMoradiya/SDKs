package com.example.iiifa_fan_android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiifa_fan_android.data.models.MainAPIResponse
import com.example.iiifa_fan_android.data.repositories.FanRepository
import com.example.iiifa_fan_android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FanViewModel @Inject constructor(private val repository: FanRepository) : ViewModel() {

    private val _loginResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<MainAPIResponse>> get() = _loginResponse
    fun login(params: Map<String?, Any?>?) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(params)
    }

    private val _changePasswordResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val changePasswordResponse: LiveData<Resource<MainAPIResponse>> get() = _changePasswordResponse
    fun changePassword(params: Map<String?, Any?>? ) = viewModelScope.launch {
        _changePasswordResponse.value = Resource.Loading
        _changePasswordResponse.value = repository.changePassword(params)
    }

    private val _addFanResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val addFanResponse: LiveData<Resource<MainAPIResponse>> get() = _addFanResponse
    fun addFan(params: Map<String?, Any?>?) = viewModelScope.launch {
        _addFanResponse.value = Resource.Loading
        _addFanResponse.value = repository.addFan(params)
    }
}