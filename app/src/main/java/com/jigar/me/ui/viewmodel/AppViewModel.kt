package com.jigar.me.ui.viewmodel

import androidx.lifecycle.*
import com.jigar.me.data.model.MainAPIResponseArray
import com.jigar.me.data.repositories.ApiRepository
import com.jigar.me.data.repositories.DBRepository
import com.jigar.me.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val apiRepository: ApiRepository,private val dbRepository: DBRepository) : ViewModel() {


    private val _getPagesResponse: MutableLiveData<Resource<MainAPIResponseArray>> = MutableLiveData()
    val getPagesResponse: LiveData<Resource<MainAPIResponseArray>> get() = _getPagesResponse
    fun getPages(level_id : Int) = viewModelScope.launch {
        _getPagesResponse.value = Resource.Loading
        _getPagesResponse.value = apiRepository.getPages(level_id.toString())
    }

    private val _getAbacusOfPagesResponse: MutableLiveData<Resource<MainAPIResponseArray>> = MutableLiveData()
    val getAbacusOfPagesResponse: LiveData<Resource<MainAPIResponseArray>> get() = _getAbacusOfPagesResponse
    fun getAbacusOfPages(pageId : String,limit : Int) = viewModelScope.launch {
        _getAbacusOfPagesResponse.value = Resource.Loading
        _getAbacusOfPagesResponse.value = apiRepository.getAbacusOfPages(pageId,limit.toString())
    }

    private val _getPracticeMaterialResponse: MutableLiveData<Resource<MainAPIResponseArray>> = MutableLiveData()
    val getPracticeMaterialResponse: LiveData<Resource<MainAPIResponseArray>> get() = _getPracticeMaterialResponse
    fun getPracticeMaterial(type : String) = viewModelScope.launch {
        _getPracticeMaterialResponse.value = Resource.Loading
        _getPracticeMaterialResponse.value = apiRepository.getPracticeMaterial(type)
    }

    fun getInAppPurchase() = dbRepository.getInAppPurchase()

    fun getInAppSKUDetailLive(sku : String) = dbRepository.getInAppSKUDetailLive(sku)
    fun getInAppSKU() = dbRepository.getInAppSKU()

}