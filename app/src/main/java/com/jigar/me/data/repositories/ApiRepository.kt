package com.jigar.me.data.repositories

import com.jigar.me.data.api.AppApi
import com.jigar.me.data.api.SafeApiCall
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val api: AppApi
) : SafeApiCall {

    suspend fun getPages(level_id : String) = safeApiCall {
        api.getPages(level_id)
    }

    suspend fun getAbacusOfPages(pageId : String,limit : String) = safeApiCall {
        api.getAbacusOfPages(pageId,limit)
    }

    suspend fun getPracticeMaterial(type : String) = safeApiCall {
        api.getPracticeMaterial(type)
    }
}