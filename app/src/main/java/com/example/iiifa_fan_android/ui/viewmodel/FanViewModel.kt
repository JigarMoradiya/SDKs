package com.example.iiifa_fan_android.ui.viewmodel

import androidx.lifecycle.*
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

    private val _updateFanProfileResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val updateFanProfileResponse: LiveData<Resource<MainAPIResponse>> get() = _updateFanProfileResponse
    fun updateFanProfile(params: Map<String?, Any?>?) = viewModelScope.launch {
        _updateFanProfileResponse.value = Resource.Loading
        _updateFanProfileResponse.value = repository.updateFanProfile(params)
    }


    private val _addFanResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val addFanResponse: LiveData<Resource<MainAPIResponse>> get() = _addFanResponse
    fun addFan(params: Map<String?, Any?>?) = viewModelScope.launch {
        _addFanResponse.value = Resource.Loading
        _addFanResponse.value = repository.addFan(params)
    }
}