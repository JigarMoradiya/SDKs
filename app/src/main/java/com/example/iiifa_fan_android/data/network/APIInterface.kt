package com.example.iiifa_fan_android.data.network

import com.example.iiifa_fan_android.utils.MainAPIResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.HashMap

//this class contains all API  endpoints with request body type
interface APIInterface {
//    @POST("patientlogin")
//    @JvmSuppressWildcards
//    fun login(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
    @POST("checkuserexists")
    @JvmSuppressWildcards
    fun checkuserexists(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?

    @POST("sendresendotp")
    @JvmSuppressWildcards
    fun sendotp(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
//    @POST("validateotp")
//    @JvmSuppressWildcards
//    fun validateotp(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
//
//    @POST("getemotion")
//    @JvmSuppressWildcards
//    fun getemotion(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
//    @POST("updatepatientprofile")
//    @JvmSuppressWildcards
//    fun updatepatientprofile(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
//    @POST("patientloginwithsocialmedia")
//    @JvmSuppressWildcards
//    fun loginWithSocialMedia(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
//    @POST("getcmspages")
//    @JvmSuppressWildcards
//    fun getCmsPages(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
//
//    @POST("startmeeting")
//    fun startZoomMeeting(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
//    @POST("getsignedgetobjecturl")
//    fun getSignedDownloadUrl(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?
//
//    @POST("validatereferralcode")
//    fun validateReferralCode(@Body params: HashMap<String, Any?>): Observable<MainAPIResponse?>?


}