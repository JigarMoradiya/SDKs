package com.example.iiifa_fan_android.data.api

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.utils.EncryptRequestData
import com.example.iiifa_fan_android.utils.Jwt
import com.example.iiifa_fan_android.utils.PeerCertificateExtractor
import com.example.iiifa_fan_android.utils.PrefManager
import com.example.iiifa_fan_android.ui.view.commonviews.classes.CustomFunctions
import com.example.iiifa_fan_android.ui.view.commonviews.classes.CustomFunctions.handleForbiddenResponse
import com.example.iiifa_fan_android.ui.view.commonviews.classes.CustomFunctions.keyThree
import com.example.iiifa_fan_android.utils.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

import io.jsonwebtoken.JwtException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.SSLException

class RemoteDataSource @Inject constructor() {


    var client: OkHttpClient? = null
    private var retrofit: Retrofit? = null
    private var cert_count = 0
    private lateinit var prefManager: PrefManager;


    fun <Api> buildApi(
        api: Class<Api>,
        context: Context,
        base_url: String?
    ): Api {
        return getClient(context, base_url)?.create(api)!!
    }


    fun getClient(context: Context, base_url: String?): Retrofit? {
        prefManager = PrefManager(context)
        return try {
            if (TextUtils.isEmpty(prefManager.certSha)) {
                downloadCertAndCreateFile(context, base_url)
            } else {
                createClient(context, File(prefManager.certSha), base_url)
            }
        } catch (e: java.lang.Exception) {
            cert_count++
            if (cert_count < 3) {
                prefManager.certSha = null
                getClient(context, base_url)
            } else  //create fake client
                getClientWithCertPin(context, base_url, "sha256/")
        }
    }

    @Throws(
        ExecutionException::class,
        InterruptedException::class,
        FileNotFoundException::class,
        NoSuchAlgorithmException::class,
        CertificateException::class
    )
    fun downloadCertAndCreateFile(context: Context, base_url: String?): Retrofit? {
        val downloadCertificateTask =
            DownloadCertificateTask(File(context.filesDir.path + "test.crt"))
        val outputFile = downloadCertificateTask.execute().get()
        prefManager.certSha = outputFile.toString()
        return createClient(context, outputFile, base_url)
    }

    @Throws(
        FileNotFoundException::class,
        NoSuchAlgorithmException::class,
        CertificateException::class
    )
    fun createClient(context: Context, cert: File?, base_url: String?): Retrofit? {
        val cert_string = PeerCertificateExtractor.extract(cert)
        return getClientWithCertPin(context, base_url, cert_string)
    }

    private fun getClientWithCertPin(
        context: Context,
        base_url: String?,
        cert: String?
    ): Retrofit? {
        val certificatePinner: CertificatePinner = CertificatePinner.Builder()
            .add("apis.midigiworld.com", cert.toString())
            .build()

        client =
            OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    forwardNext(context, chain)!!
                })
                .readTimeout(5, TimeUnit.MINUTES).connectTimeout(5, TimeUnit.MINUTES)
                .certificatePinner(certificatePinner).build()



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
        var request: Request = chain.request()
        Log.d("post_request", request.url.toString() + "")
        val prefManager = PrefManager(context)

        //get user token
        val user_id = prefManager.userId


        //check the user has logged in or not
        var is_authorized = prefManager.userId != null && !TextUtils.isEmpty(prefManager.userId)

        //check weather to turn on encryption on request and rersponse
        val enable_encryption = !BuildConfig.BUILD_TYPE.contains("WithoutEncryption")
        //        Algorithm algorithm;


        //turn of authrization for some specific APIs even if user is logged in
        if (request.url.toString().contains("gettitle")
            || request.url.toString().contains("contactusviaemail")
            || request.url.toString().contains("getfaqforapplication")
            || request.url.toString().contains("getcmspages")
            || request.url.toString().contains("getcoursecategory")
            || request.url.toString().contains("Fgetlanguage")
            || request.url.toString().contains("getcadencebycourseid")
            || request.url.toString().contains("completeprofile")
            || request.url.toString().contains("gettrendingskill")
            || request.url.toString().contains("sendresendotp")
            || request.url.toString().contains("getreviewsbycourseid")
            || request.url.toString().contains("getinstructorbyid")
            || request.url.toString().contains("getcoursesbyinstructor")
            || request.url.toString().contains("getreviewsbyinstructorid")
            || request.url.toString().contains("getcourseconfiguration")
            || request.url.toString().contains("home/search")
            || request.url.toString().contains("validatelearnermiid")
            || request.url.toString().contains("createcustomer")
            || request.url.toString().contains("ephemeralkeys")
            || request.url.toString().contains("patientloginwithsocialmedia")
            || request.url.toString().contains("unauth")
            || request.url.toString().contains("userversioncheck")
        ) {
            is_authorized = false
        }


        //get Response Body sent by Repositary APT calls
        val oldBody = request.body
        val buffer = Buffer()
        oldBody!!.writeTo(buffer)
        val strOldBody = buffer.readUtf8()
        Log.d("post_request_old_body", strOldBody)
        val body: RequestBody
        val strNewBody: String
        val key: String


        //create Header object to create JWT
        val headers_jwt: MutableMap<String, Any> = HashMap()
        val myjsonString: String
        val jws: String

        //set key according to authorization key
        key = if (is_authorized) {
            prefManager.token
        } else {
            keyThree
        }
        if (enable_encryption) {
            strNewBody =
                if (is_authorized) EncryptRequestData.getEncryptedData(strOldBody) // Encrypt request body
                else EncryptRequestData.encrypt(strOldBody) // Encrypt request body
            Log.d("post_request_new_body", "Encrypted body $strNewBody")
            myjsonString = "{\"params\":\"$strNewBody\"}" // add encrypted body in params
            Log.d("post_request_json", "Encrypted body inside params$myjsonString")
            val mediaType = "application/json".toMediaType()
            body = myjsonString.toRequestBody(mediaType)
            headers_jwt.clear()
            headers_jwt["alg"] = "HS256"
            headers_jwt["typ"] = "JWT"
            headers_jwt["channel"] = Constants.ANDROID
            if (is_authorized) {
                headers_jwt["user_id"] = user_id
                headers_jwt["device_id"] = CustomFunctions.getDeviceId()

                //get jws
                jws = Jwt.createJWT(headers_jwt, myjsonString, 0, key)
                Log.d(
                    "post_request_jws",
                    "Bearer that we are passing in Authorization API header for authenticate API $jws"
                )

                //pass API request call with JWT token
                request = request.newBuilder()
                    .post(body)
                    .addHeader("user_id", user_id)
                    .addHeader("channel", Constants.ANDROID)
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("Authorization", "Bearer $jws")
                    .build()
            } else {

                //get jws
                jws = Jwt.createJWT(headers_jwt, myjsonString, 0, key)
                Log.d(
                    "post_request_jws",
                    "Bearer that we are passing in Authorization API header for un-authenticate API $jws"
                )

                //if user is not logged in the  pass API request asit is
                request = request.newBuilder()
                    .post(body)
                    .addHeader("user_id", user_id)
                    .addHeader("channel", Constants.ANDROID)
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("Authorization", "Bearer $jws")
                    .build()
            }
        } else {
            //if API request does not require encryption then pass the request as it is without changing anything
            strNewBody = strOldBody
            Log.d("post_request_new_body", "Without encryption enabled body $strNewBody")
            myjsonString = "{\"params\":$strNewBody}"
            Log.d(
                "post_request_json",
                "Without encryption enabled body in params$myjsonString"
            )
            val mediaType = "application/json".toMediaType()
            body = myjsonString.toRequestBody(mediaType)
            request = if (is_authorized) {
                request.newBuilder()
                    .post(body)
                    .addHeader("user_id", user_id)
                    .addHeader("channel", Constants.ANDROID)
                    .build()
            } else {
                request.newBuilder()
                    .post(body)
                    .addHeader("channel", Constants.ANDROID)
                    .build()
            }
        }
        return try {
            val response: Response = chain.proceed(request) // get the API response


            //forcefully logout user if API response is 403
            if (response.code == 403 || response.code == 401) {
                handleForbiddenResponse()
                return createEmptyResponse(
                    chain,
                    "Something went wrong, Please try after sometime"
                )
            } else if (response.code == 500) {
                return createEmptyResponse(
                    chain,
                    "Something went wrong, Please try after sometime"
                )
            }

            Log.d("post_response_main", "Response from direct API $response")
            val stringJson = response.body!!.string()
            Log.d("post_response_body", "Converted response to string $stringJson")
            val jsonObject = JSONObject(stringJson)
            val decrypted_string: String?

            if (!enable_encryption) {
                decrypted_string =
                    jsonObject.toString() // without encryption , get response as it is
            } else {
                if (jsonObject.has("error")) {
                    //if response contains error oject then do no decrypt it
                    decrypted_string = jsonObject.toString()
                    Log.d(
                        "post_response",
                        "Error response from API direct string $decrypted_string"
                    )
                } else {
                    if (jsonObject.has("content")) {
                        //get the decoded json token
                        //  String token = Jwt.decodeJWT(jsonObject.get("content").toString(), key);
                        //  Log.d("post_response_token", token);
                        val token: String
                        Log.d(
                            "post_response",
                            "Response has content so going inside JWT Decode block..."
                        )
                        if (request.url.toString().contains("logout")) {
                            token = Jwt.decodeJWT(jsonObject["content"].toString(), keyThree)
                            Log.d(
                                "post_response_token",
                                "Decoded JWT value Logout API$token"
                            )
                            decrypted_string = EncryptRequestData.decryptByDefaultToken(token)
                        } else {
                            token = Jwt.decodeJWT(jsonObject["content"].toString(), key)
                            Log.d(
                                "post_response_token",
                                "Decoded JWT value Normal API$token"
                            )
                            decrypted_string =
                                if (is_authorized) EncryptRequestData.getDecryptedData(token) else EncryptRequestData.decryptByDefaultToken(
                                    token
                                )
                        }
                    } else {
                        decrypted_string = jsonObject.toString()
                        Log.d(
                            "post_response",
                            "Response has no error and no conetnt $decrypted_string"
                        )
                    }
                }
            }


            //remove null keys from the json object
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jp = JsonParser()

            val je = jp.parse(decrypted_string)
            val prettyJsonString = gson.toJson(je)
            Log.d(
                "post_response",
                "Remove all NULL keys from JSON Object$prettyJsonString"
            )
            val MainAPIResponse = gson.fromJson(
                prettyJsonString,
                MainAPIResponse::class.java
            )
            if (MainAPIResponse.code == 403 || MainAPIResponse.code == 401) {
                //logout user in  case MAIN api resonse code is 403
                handleForbiddenResponse()
                return createEmptyResponse(
                    chain,
                    "Something went wrong, Please try after sometime"
                )
            } else {
                //create response body with decrypted value and pass it to the respositories
                Log.d(
                    "post_response",
                    "creating the main response to send it to APIs for" + request.url
                )


                //this is temporary to show user actual database error
                if (MainAPIResponse.code == 500) {
                    if (MainAPIResponse.error == null) {
                        MainAPIResponse.error =
                            com.example.iiifa_fan_android.data.models.Error(userMessage = MainAPIResponse.message)
                    } else if (MainAPIResponse?.error?.userMessage == null) {
                        MainAPIResponse.error?.userMessage = MainAPIResponse.message
                    }
                }

                response.newBuilder().body(
                    Gson().toJson(MainAPIResponse).toResponseBody(response.body!!.contentType())
                ).build()
            }
        } catch (e: Exception) {
            Log.d("post_response_Issue with response of API ", request.url.toString())
            return handleException(chain, e)
        }
    }


    private fun handleException(
        chain: Interceptor.Chain,
        e: Exception
    ): Response? {
        Log.d("post_response_message", e.message + " " + e.javaClass.canonicalName)
        //logout user while facing error related to JWT
        return if (e is JwtException) {
            handleForbiddenResponse()
            return createEmptyResponse(
                chain,
                "Something went wrong, Please try after sometime"
            )
        } else if (e is SocketTimeoutException || e is UnknownHostException) {
            Log.d("response_create_post", e.message + "")
            return createEmptyResponse(
                chain,
                "Uh-Oh! Slow or no internet connection. Please check your internet settings and try again"
            )
        } else if (e is SSLException) {
            cert_count++
            prefManager.certSha = null
            handleForbiddenResponse()
            return createEmptyResponse(
                chain,
                "Something went wrong, Please try after sometime"
            )
        } else {
            //in case content/error dose not fount in json
            return createEmptyResponse(
                chain,
                "Something went wrong, Please try after sometime"
            )
        }
    }

    private fun createEmptyResponse(chain: Interceptor.Chain, errorMessage: String): Response? {
        val mediaType = "application/json".toMediaType()

        val error = com.example.iiifa_fan_android.data.models.Error()
        error.message = errorMessage

        val mainAPIResponse = MainAPIResponse(
            errorMessage,
            false,
            0,
            null,
            error
        )

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