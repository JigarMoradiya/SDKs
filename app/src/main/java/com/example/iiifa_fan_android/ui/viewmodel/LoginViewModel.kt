package com.example.iiifa_fan_android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiifa_fan_android.data.models.MainAPIResponse
import com.example.iiifa_fan_android.data.repositories.LoginRepository
import com.example.iiifa_fan_android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private val _loginResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<MainAPIResponse>> get() = _loginResponse

    fun login(params: Map<String?, Any?>?) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(params)
    }
}