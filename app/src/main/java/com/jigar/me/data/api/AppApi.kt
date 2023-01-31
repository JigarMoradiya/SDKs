package com.jigar.me.data.api

import com.jigar.me.data.model.MainAPIResponseArray
import retrofit2.http.*

interface AppApi {
    @FormUrlEncoded
    @POST("getPagesNew")
    suspend fun getPages(@Field("level_id") param : String): MainAPIResponseArray

    @FormUrlEncoded
    @POST("getAbacus")
    suspend fun getAbacusOfPages(@Field("page_id") page_id : String,@Field("limit") limit : String): MainAPIResponseArray

    @FormUrlEncoded
    @POST("getDailyPaper")
    suspend fun getExamAbacus(@Field("level") level : String): MainAPIResponseArray

    @FormUrlEncoded
    @POST("getImages")
    suspend fun getPracticeMaterial(@Field("type") param : String): MainAPIResponseArray

}