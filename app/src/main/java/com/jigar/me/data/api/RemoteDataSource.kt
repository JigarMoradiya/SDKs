package com.jigar.me.data.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.jigar.me.BuildConfig
import com.jigar.me.data.model.MainAPIResponseArray
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.SSLException

class RemoteDataSource @Inject constructor() {

    var client: OkHttpClient? = null
    private var retrofit: Retrofit? = null
//    @Inject
//    internal lateinit var prefManager: PreferencesHelper

    fun <Api> buildApi(api: Class<Api>,context: Context,base_url: String?): Api {
        return getClient(context, base_url)?.create(api)!!
    }

    private fun getClient(context: Context,base_url: String?,): Retrofit? {
        client = if (BuildConfig.DEBUG){
            OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    forwardNext(context, chain)!!
                })
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build()
        }else{
            OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build()
        }

        retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit
    }


    @Throws(IOException::class)
    fun forwardNext(context: Context, chain: Interceptor.Chain): Response? {
        val request: Request = chain.request()
        Log.e("post_request", request.url.toString() + "")
        //get Response Body sent by Repositary APT calls
//        val oldBody = request.body
//        val buffer = Buffer()
//        oldBody!!.writeTo(buffer)
//        val strNewBody = buffer.readUtf8()
//        Log.e("post_request_new_body", "Without encryption enabled body $strNewBody")
        return try {
            val response: Response = chain.proceed(request) // get the API response
            //forcefully logout user if API response is 403
//            if (response.code == 403 || response.code == 401) {
//                handleForbiddenResponse()
//                return createEmptyResponse(
//                    chain,
//                    "Something went wrong, Please try after sometime"
//                )
//            } else if (response.code == 500) {
//                return createEmptyResponse(
//                    chain,
//                    "Something went wrong, Please try after sometime"
//                )
//            }

            Log.e("post_response_main", "Response from direct API $response")
            val stringJson = response.body?.string()
            val jsonObject = JSONObject(stringJson)
            val decrypted_string = jsonObject.toString()

            //remove null keys from the json object
            val gson = GsonBuilder().setPrettyPrinting().create()
            val je = JsonParser.parseString(decrypted_string)
            val prettyJsonString = gson.toJson(je)
            Log.e(
                "post_response",
                "Remove all NULL keys from JSON Object$prettyJsonString"
            )
            val mainAPIResponse = gson.fromJson(prettyJsonString, MainAPIResponseArray::class.java)
                //create response body with decrypted value and pass it to the respositories
            Log.e(
                "post_response",
                "creating the main response to send it to APIs for" + request.url
            )
            response.newBuilder().body(
                Gson().toJson(mainAPIResponse).toResponseBody(response.body?.contentType())
            ).build()

        } catch (e: Exception) {
            Log.e("post_response_Issue with response of API ", request.url.toString())
            return handleException(chain, e)
        }
    }


    private fun handleException(
        chain: Interceptor.Chain,
        e: Exception
    ): Response? {
        Log.e("post_response_message", e.message + " " + e.javaClass.canonicalName)
        //logout user while facing error related to JWT
        when (e) {
            is SocketTimeoutException, is UnknownHostException -> {
                Log.e("response_create_post", e.message + "")
                return createEmptyResponse(
                    chain,
                    "Uh-Oh! Slow or no internet connection. Please check your internet settings and try again"
                )
            }
            is SSLException -> {
//                handleForbiddenResponse()
                return createEmptyResponse(
                    chain,
                    "Something went wrong, Please try after sometime"
                )
            }
            else -> {
                //in case content/error dose not fount in json
                return createEmptyResponse(
                    chain,
                    "Something went wrong, Please try after sometime"
                )
            }
        }
    }

    private fun createEmptyResponse(chain: Interceptor.Chain, errorMessage: String): Response? {
        val mediaType = "application/json".toMediaType()

//        val error = com.jigar.me.data.model.Error()
//        error.message = errorMessage

        val mainAPIResponse = MainAPIResponseArray("empty response",false,null)
        val responseBody = Gson().toJson(mainAPIResponse).toResponseBody(mediaType)

        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(500)
            .message("Something went wrong, Please try after sometime")
            .body(responseBody)
            .build()

    }


}