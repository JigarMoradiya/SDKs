package com.example.iiifa_fan_android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiifa_fan_android.data.models.MainAPIResponse
import com.example.iiifa_fan_android.data.repositories.FanRepository
import com.example.iiifa_fan_android.data.repositories.SettingsRepository
import com.example.iiifa_fan_android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository) : ViewModel() {

    private val _manageEntitiesResponse: MutableLiveData<Resource<MainAPIResponse>> = MutableLiveData()
    val manageEntitiesResponse: LiveData<Resource<MainAPIResponse>> get() = _manageEntitiesResponse
    fun manageEntities(params: Map<String?, Any?>?) = viewModelScope.launch {
        _manageEntitiesResponse.value = Resource.Loading
        _manageEntitiesResponse.value = repository.manageEntities(params)
    }
}