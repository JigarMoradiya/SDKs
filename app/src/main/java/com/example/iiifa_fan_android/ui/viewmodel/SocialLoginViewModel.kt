package com.example.iiifa_fan_android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iiifa_fan_android.data.models.SocialMediaUserModel

class SocialLoginViewModel : ViewModel(){
    private val mutableLiveData = MutableLiveData<SocialMediaUserModel?>()
    val isSocialDataSet = MutableLiveData(false)
    val isLoader = MutableLiveData(false)
    private var socialMediaUserModel: SocialMediaUserModel? = SocialMediaUserModel()

    fun setSocialMediaLogin(socialMediaUserModel: SocialMediaUserModel?){
        isSocialDataSet.postValue(true)
        this.socialMediaUserModel = socialMediaUserModel
        mutableLiveData.postValue(socialMediaUserModel)
    }
    fun getSocialMediaLoginData(): SocialMediaUserModel? = socialMediaUserModel
}