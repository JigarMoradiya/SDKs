package com.example.iiifa_fan_android.data.network


import com.example.iiifa_fan_android.data.models.Error
import com.example.iiifa_fan_android.BuildConfig
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.example.iiifa_fan_android.data.pref.AppPreferencesHelper
import com.example.iiifa_fan_android.data.pref.PreferencesHelper
import com.example.iiifa_fan_android.utils.*
import com.example.iiifa_fan_android.utils.CustomClasses.handleForbiddenResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.jsonwebtoken.JwtException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.SSLException


object APIClient {
    private val prefManager = AppPreferencesHelper(MyApplication.getInstance(), Constants.PREF_NAME)
    var client: OkHttpClient? = null
    private var retrofit: Retrofit? = null
    private var cert_count = 0
    fun getClient(base_url: String?): Retrofit? {
        return try {
            if (TextUtils.isEmpty(prefManager.getCertSha())) {
                downloadCertAndCreateFile(base_url)
            } else {
                createClient(File(prefManager.getCertSha()), base_url)
            }
        } catch (e: Exception) {
            cert_count++
            if (cert_count < 3) {
                prefManager.setCertSha(null)
                getClient(base_url)
            } else  //create fake client
                getClientWithCertPin(base_url, "sha256/")
        }
    }

    @Throws(
        ExecutionException::class,
        InterruptedException::class,
        FileNotFoundException::class,
        NoSuchAlgorithmException::class,
        CertificateException::class
    )
    fun downloadCertAndCreateFile(base_url: String?): Retrofit? {
        val downloadCertificateTask = DownloadCertificateTask(
            File(
                MyApplication.getInstance().filesDir.path+"test.crt"
            )
        )
        val outputFile: File? = downloadCertificateTask.execute().get()
        prefManager.setCertSha(outputFile.toString())
        return createClient(outputFile, base_url)
    }

    @Throws(
        FileNotFoundException::class,
        NoSuchAlgorithmException::class,
        CertificateException::class
    )
    fun createClient(cert: File?, base_url: String?): Retrofit? {
        val cert_string: String = PeerCertificateExtractor.extract(cert)
        return getClientWithCertPin(base_url, cert_string)
    }

    fun getClientWithCertPin(base_url: String?, cert: String?): Retrofit? {
        val certificatePinner: CertificatePinner = CertificatePinner.Builder()
            .add("apis.midigiworld.com", cert?:"")
            .build()
        client = OkHttpClient.Builder().addInterceptor(Interceptor { chain -> forwardNext(chain)!! })
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

    fun handleException(chain: Interceptor.Chain, e: Exception, response: Response?): Response? {
        Log.d("post_response_message", e.message + " " + e.javaClass.canonicalName)
        //logout user while facing error related to JWT
        return if (e is JwtException) {
            handleForbiddenResponse()
            null
        } else if (e is SocketTimeoutException || e is UnknownHostException) {
            Log.d("response_create_post", e.message + "")
            val error = Error()
            error.message = "Uh-Oh! Slow or no internet connection. Please check your internet settings and try again"
            val mainAPIResponse = MainAPIResponse(error.message, false, 0, null, error)
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message(error.message?:"")
                .body(Gson().toJson(mainAPIResponse).toResponseBody(mediaType))
                .build()
        } else if (e is SSLException) {
            cert_count++
            prefManager.setCertSha(null)
            handleForbiddenResponse()
            val error = Error()
            error.message = "Something went wrong, Please try after sometime"
            val mainAPIResponse = MainAPIResponse(
                "Something went wrong, Please try after sometime",
                false,
                0,
                null,
                error
            )
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("Something went wrong, Please try after sometime")
                .body(Gson().toJson(mainAPIResponse).toResponseBody(mediaType))
                .build()
        } else {
            //in case content/error dose not fount in json
            val error = Error()
            error.message = "Something went wrong, Please try after sometime"
            val mainAPIResponse = MainAPIResponse(
                "Something went wrong, Please try after sometime",
                false,
                0,
                null,
                error
            )
            response!!.newBuilder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("Something went wrong, Please try after sometime")
                .body(
                    Gson().toJson(mainAPIResponse)
                        .toResponseBody(response.body!!.contentType())
                )
                .build()
        }
    }

    @Throws(IOException::class)
    fun forwardNext(chain: Interceptor.Chain): Response? {
        var request: Request = chain.request()
        Log.d("post_request", request.url.toString() + "")
        //get user token
        val user_id: String = prefManager.getUserId()?:""


        //check the user has logged in or not
        var is_authorized = !TextUtils.isEmpty(user_id)

        //check weather to turn on encryption on request and rersponse
        val enable_encryption: Boolean = !BuildConfig.BUILD_TYPE.contains("WithoutEncryption")
        //        Algorithm algorithm;


        //turn of authrization for some specific APIs even if user is logged in
        if (request.url.toString().contains("getspecialization") ||
            request.url.toString().contains("addexpert") ||
            request.url.toString().contains("expertloginwithsocialmedia") ||
            request.url.toString().contains("validatereferralcode")
        ) {
            is_authorized = false
        }

//        if (request.url().toString().contains("logout")) {
//            is_authorized = false;
//            user_id = prefManager.getPreviousUserId();
//        }

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
            prefManager.getToken()?:""
        } else {
            CustomFunctions.keyThree
        }
        if (enable_encryption) {
            strNewBody =
                if (is_authorized) EncryptRequestData.getEncryptedData(strOldBody)?:"" // Encrypt request body
                else EncryptRequestData.encrypt(strOldBody)?:"" // Encrypt request body
            Log.d("post_request_new_body", "Encrypted body $strNewBody")
            myjsonString = "{\"params\":\"$strNewBody\"}" // add encrypted body in params
            Log.d("post_request_json", "Encrypted body inside params$myjsonString")
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
            body = myjsonString
                .toRequestBody(mediaType) // create in new request body  with changed params
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
                    "Bearer that we are passing in Authorization API header for authenticate API$jws"
                )

                //pass API request call with JWT token
                request = request.newBuilder()
                    .post(body)
                    .addHeader("user_id", user_id)
                    .addHeader("channel", Constants.ANDROID)
                    .addHeader("Authorization", "Bearer $jws")
                    .build()
            } else {

                //get jws
                jws = Jwt.createJWT(headers_jwt, myjsonString, 0, key)
                Log.d(
                    "post_request_jws",
                    "Bearer that we are passing in Authorization API header for un-authenticate API$jws"
                )

                //if user is not logged in the  pass API request asit is
                request = request.newBuilder()
                    .post(body)
                    .addHeader("user_id", user_id)
                    .addHeader("channel", Constants.ANDROID)
                    .addHeader("Authorization", "Bearer $jws")
                    .addHeader("device_id", CustomFunctions.getDeviceId())
                    .build()
            }
        } else {
            //if API request does not require encryption then pass the request as it is without changing anything
            strNewBody = strOldBody
            Log.d(
                "post_request_new_body",
                "Without encryption enabled body $strNewBody"
            )
            myjsonString = "{\"params\":$strNewBody}"
            Log.d(
                "post_request_json",
                "Without encryption enabled body in params$myjsonString"
            )
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
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
            Log.d("post_response_main", "Response from direct API $response")
            val stringJson = response.body!!.string()
            Log.d("post_response_body", "Converted response to string $stringJson")
            val jsonObject = JSONObject(stringJson)
            val decrypted_string: String

            //forcefully logout user if API response is 403
            if (response.code == 403 || response.code == 401) {
                handleForbiddenResponse()
                return null
            } else if (response.code == 500) {
                ThreadUtils.runOnUiThread {
                    val inflater = MyApplication.Companion.getInstance().applicationContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    CustomViews.showFailToast(inflater, "Something went wrong")
                }
                return null
            }
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
                            token = Jwt.decodeJWT(jsonObject["content"].toString(),CustomFunctions.keyThree)
                            Log.d(
                                "post_response_token",
                                "Decoded JWT value Logout API$token"
                            )
                            decrypted_string = EncryptRequestData.decryptByDefaultToken(token)?:""
                        } else {
                            token = Jwt.decodeJWT(jsonObject["content"].toString(), key)
                            Log.d(
                                "post_response_token",
                                "Decoded JWT value Normal API$token"
                            )
                            decrypted_string = if (is_authorized) EncryptRequestData.getDecryptedData(token)?:"" else EncryptRequestData.decryptByDefaultToken(token)?:""
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
            assert(decrypted_string != null)
            val je = jp.parse(decrypted_string)
            val prettyJsonString = gson.toJson(je)
            Log.d(
                "post_response",
                "Remove all NULL keys from JSON Object$prettyJsonString"
            )
            val MainAPIResponse: MainAPIResponse =
                gson.fromJson(prettyJsonString, MainAPIResponse::class.java)
            if (MainAPIResponse.code == 403 || MainAPIResponse.code == 401) {
                //logout user in  case MAIN api resonse code is 403
                handleForbiddenResponse()
                null
            } else {
                //create response body with decrypted value and pass it to the respositories
                Log.d("post_response", "creating the main response to send it to APIs")
                response.newBuilder()
                    .body(
                        Gson().toJson(MainAPIResponse)
                            .toResponseBody(response.body!!.contentType())
                    )
                    .build()
            }
        } catch (e: Exception) {
            handleException(chain, e, null)
        }
    }
}