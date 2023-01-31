package com.jigar.me.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.jigar.me.data.model.MainAPIResponseArray
import com.jigar.me.data.model.dbtable.exam.ExamHistory
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.data.repositories.ApiRepository
import com.jigar.me.data.repositories.DBRepository
import com.jigar.me.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
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

    private val _getExamAbacusResponse: MutableLiveData<Resource<MainAPIResponseArray>> = MutableLiveData()
    val getExamAbacusResponse: LiveData<Resource<MainAPIResponseArray>> get() = _getExamAbacusResponse
    fun getExamAbacus(level : String) = viewModelScope.launch {
        _getExamAbacusResponse.value = Resource.Loading
        _getExamAbacusResponse.value = apiRepository.getExamAbacus(level)
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

    fun getInAppSKUDetail(sku : String) : LiveData<List<InAppSkuDetails>>{
        val result = MutableLiveData<List<InAppSkuDetails>>()
        viewModelScope.launch(Dispatchers.IO) {
            val list = dbRepository.getInAppSKUDetail(sku)
            result.postValue(list)
        }
        return result
    }

    suspend fun saveExamResultDB(data: ExamHistory) = dbRepository.saveExamResultDB(data)
    fun getExamHistoryList(examType :String) = dbRepository.getExamHistoryList(examType)

}