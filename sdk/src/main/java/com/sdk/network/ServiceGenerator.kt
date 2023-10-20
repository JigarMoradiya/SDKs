package com.sdk.network

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sdk.BuildConfig
import com.sdk.data.MainAPIResponse
import com.sdk.network.customs.DownloadCertificateTask
import com.sdk.network.customs.EncryptRequestData
import com.sdk.network.customs.Jwt
import com.sdk.network.customs.PeerCertificateExtractor
import com.sdk.utils.*
import com.sdk.utils.CustomFunctions.keyThree
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
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLException

object ServiceGenerator {
	var client: OkHttpClient? = null
	private var retrofit: Retrofit? = null
	private var cert_count = 0
	private lateinit var prefManager: PrefManager
	fun <Api> buildApi(
		api: Class<Api>,
		context: Context,
		base_url: String?
	): Api {
		return getClient(context, base_url)?.create(api)!!
	}

	private fun getClient(context: Context, base_url: String?): Retrofit? {
		prefManager = PrefManager(context)
		return try {
			if (TextUtils.isEmpty(prefManager.getCertSha())) {
				downloadCertAndCreateFile(context, base_url)
			} else {
				createClient(context, File(prefManager.getCertSha()), base_url)
			}
		} catch (e: java.lang.Exception) {
			cert_count++
			if (cert_count < 3) {
				prefManager.setCertSha(null)
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
		prefManager.setCertSha(outputFile.toString())
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
				.cache(null)
				.addInterceptor(Interceptor { chain ->
					forwardNext(context, chain)!!
				})
				.readTimeout(5, TimeUnit.MINUTES).connectTimeout(5, TimeUnit.MINUTES)
				.certificatePinner(certificatePinner).build()


		retrofit = client?.let {
			Retrofit.Builder()
				.baseUrl(base_url)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.client(it)
				.build()
		}

		return retrofit
	}

	@Throws(IOException::class)
	fun forwardNext(context: Context, chain: Interceptor.Chain): Response? {
		var request: Request = chain.request()

		Log.d("post_request", request.url.toString() + "")
		val prefManager = PrefManager(context)

		//get user token
		val user_id = prefManager.getUserId()?:""


		//check the user has logged in or not
		var is_authorized = prefManager.getUserId() != null && !TextUtils.isEmpty(prefManager.getUserId())
		if ((request.url.toString().contains("loginsdkusers"))) {
			is_authorized = false
		}
		//check weather to turn on encryption on request and rersponse
		val enable_encryption = !BuildConfig.BUILD_TYPE.contains("WithoutEncryption")
		//        Algorithm algorithm;


		//get Response Body sent by Repositary APT calls
		val oldBody = request.body
		val buffer = Buffer()
		oldBody!!.writeTo(buffer)
		val strOldBody = buffer.readUtf8()
		Log.d("post_request_old_body", strOldBody)
		val body: RequestBody
		val strNewBody: String


		//create Header object to create JWT
		val headers_jwt: MutableMap<String, Any> = HashMap()
		val myjsonString: String
		val jws: String

		//set key according to authorization key
		val key: String = if (is_authorized) {
			prefManager.getToken()?:""
		} else {
			keyThree
		}
		if (enable_encryption) {
			strNewBody =
				if (is_authorized) EncryptRequestData.getEncryptedData(context,strOldBody).toString() // Encrypt request body
				else EncryptRequestData.encrypt(strOldBody).toString() // Encrypt request body
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
//					.addHeader("version", BuildConfig.VERSION_NAMEE)
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
//					.addHeader("version", BuildConfig.VERSION_NAME)
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
			when (response.code) {
				403 -> {
					handleForbiddenResponse()
					return createEmptyResponse(chain,null,null)
				}
				401 -> {
					handleForbiddenResponse()
					return createEmptyResponse(
						chain,
						"Something went wrong, Please try after sometime"
					)
				}
				500 -> {
					return createEmptyResponse(
						chain,
						"Something went wrong, Please try after sometime"
					)
				}
			}

			Log.d("post_response_main", "Response from direct API $response")
			val stringJson = response.body!!.string()
			Log.d("post_response_body", "Converted response to string $stringJson")
			val jsonObject = JSONObject(stringJson)
			val decrypted_string: String?

			if (!enable_encryption) {
				decrypted_string = jsonObject.toString() // without encryption , get response as it is
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
						// TODO decode by local token becuase server token is deleted for this api's
						if (request.url.toString().contains("logout") || request.url.toString().contains("deleteaccount")) {
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
			val je = JsonParser.parseString(decrypted_string)
			val prettyJsonString = gson.toJson(je)
			Log.d(
				"post_response",
				"Remove all NULL keys from JSON Object$prettyJsonString"
			)
			val MainAPIResponse = gson.fromJson(
				prettyJsonString,
				MainAPIResponse::class.java
			)
			when (MainAPIResponse.code) {
				403 -> {
					//logout user in  case MAIN api resonse code is 403
					handleForbiddenResponse()
					return createEmptyResponse(chain,null,null)
				}
				401 -> {
					//logout user in  case MAIN api resonse code is 403
					handleForbiddenResponse()
					return createEmptyResponse(chain,"Something went wrong, Please try after sometime")
				}
				else -> {
					//create response body with decrypted value and pass it to the respositories
					Log.d(
						"post_response",
						"creating the main response to send it to APIs for" + request.url
					)

					println("MainAPIResponse_jigar = $MainAPIResponse")


					//this is temporary to show user actual database error
					if (MainAPIResponse.code == 500) {
						if (MainAPIResponse.error == null) {
							MainAPIResponse.error =
								com.sdk.data.Error(userMessage = MainAPIResponse.message)
						} else if (MainAPIResponse?.error?.userMessage == null) {
							MainAPIResponse.error?.userMessage = MainAPIResponse.message
						}
					}

					try{
						response.newBuilder().body(
							Gson().toJson(MainAPIResponse).toResponseBody(response.body!!.contentType())
						).build()
					} catch (e: Exception) {
						return handleException(chain, e)
					}

				}
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
		return when (e) {
			is JwtException -> {
				handleForbiddenResponse()
				return createEmptyResponse(chain,e.message)
			}
			is SocketTimeoutException, is UnknownHostException -> {
				return createEmptyResponse(chain,Constants.NO_INTERNET_MESSAGE,errorType = Constants.UNKNOWN_HOST_EXCEPTION)
			}
			is SSLException -> {
				cert_count++
				prefManager.setCertSha(null)
				handleForbiddenResponse()
				return createEmptyResponse(chain,e.message)
			}
			else -> {
				//in case content/error dose not fount in json
				return createEmptyResponse(chain, e.message)
			}
		}
	}

	private fun handleForbiddenResponse() {

	}

	private fun createEmptyResponse(
		chain: Interceptor.Chain,
		errorMessage: String?,
		errorType: String? = null
	): Response {
		val mediaType = "application/json".toMediaType()

		val error = com.sdk.data.Error()
		error.message = errorMessage

		//this is for unknownnhost
		if (errorType != null) {
			error.errorType = errorType
		}

		val mainAPIResponse = MainAPIResponse(
			errorMessage,
			false,
			0,
			null,
			error
		)

		val responseBody = Gson().toJson(mainAPIResponse).toResponseBody(mediaType)

		if (errorMessage == null) {
			return Response.Builder()
				.request(chain.request())
				.protocol(Protocol.HTTP_1_1)
				.code(500)
				.message("403")
				.body(responseBody)
				.build()
		} else {
			return Response.Builder()
				.request(chain.request())
				.protocol(Protocol.HTTP_1_1)
				.code(500)
				.message(errorMessage)
				.body(responseBody)
				.build()

		}


	}

}
