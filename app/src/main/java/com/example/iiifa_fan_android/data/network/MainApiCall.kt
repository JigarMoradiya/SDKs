package com.example.iiifa_fan_android.data.network

import android.util.Log
import com.example.iiifa_fan_android.utils.CustomClasses
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.HashMap
import com.example.iiifa_fan_android.data.models.Error
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.data.models.MainAPIResponse

class MainApiCall {
    private var apiInterface: APIInterface? = null
    fun getData(
            stringObjectMap: HashMap<String, Any?>,
            apiName: String?,
            mainApiResponseInterface: MainApiResponseInterface
    ) {
        var postResponseObservable: Observable<MainAPIResponse?>? = null
        when (apiName) {
            Constants.CHECK_USER_EXIST -> {
                apiInterface = APIClient.getClient(BuildConfig.COMMON_MODULE)?.create(
                    APIInterface::class.java
                )
                postResponseObservable = apiInterface?.checkuserexists(stringObjectMap)
            }
            Constants.SEND_RESEND_OTP -> {
                apiInterface = APIClient.getClient(BuildConfig.COMMON_MODULE)?.create(
                    APIInterface::class.java
                )
                postResponseObservable = apiInterface?.sendotp(stringObjectMap)
            }
            Constants.VALIDATE_OTP -> {
                apiInterface = APIClient.getClient(BuildConfig.COMMON_MODULE)?.create(
                    APIInterface::class.java
                )
                postResponseObservable = apiInterface?.validateotp(stringObjectMap)
            }
            Constants.ADD_FAN -> {
                apiInterface = APIClient.getClient(BuildConfig.FAN_MODULE)?.create(
                    APIInterface::class.java
                )
                postResponseObservable = apiInterface?.addFan(stringObjectMap)
            }
            Constants.VALIDATE_REFFERAL_CODE -> {
            }



        }
        postResponseObservable?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<MainAPIResponse?> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(mainAPIResponse: MainAPIResponse) {
                        Log.d("response_create_post", mainAPIResponse.toString() + "")
                        if (mainAPIResponse.code == 200) {
                            //convert content to json object and move forward
                            val jsonObject = CustomClasses.convertObjectToJsonObject(mainAPIResponse.content)
                            mainApiResponseInterface.onSuccess(jsonObject, apiName)
                        } else {
                            mainAPIResponse.error?.message = mainAPIResponse.message
                            mainApiResponseInterface.onFailure(mainAPIResponse.error, apiName)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is SocketTimeoutException) {
                            Log.d("response_create_post", e.message + "")
                            val error = Error()
                            error.message = "Uh-Oh! Slow or no internet connection. Please check your internet settings and try again"
                            mainApiResponseInterface.onFailure(error, apiName)
                        } else if (e is UnknownHostException) {
                            Log.d("response_create_post", e.message + "")
                            val error = Error()
                            error.message = "Uh-Oh! Slow or no internet connection. Please check your internet settings and try again"
                            mainApiResponseInterface.onFailure(error, apiName)
                        } else {
                            Log.d("response_create_post", e.message + "")
                            val error = Error()
                            error.message = "Something went wrong, please try after sometime"
                            mainApiResponseInterface.onFailure(error, apiName)
                        }
                    }

                    override fun onComplete() {}
                })
    }

    companion object {
        var instance: MainApiCall? = null

        @JvmName("getInstance1")
        @JvmStatic
        fun getInstance(): MainApiCall {
            if (instance == null) {
                instance = MainApiCall()
            }
            return instance as MainApiCall
        }
    }
}