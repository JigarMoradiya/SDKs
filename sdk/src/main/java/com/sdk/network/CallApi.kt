package com.sdk.network

import android.util.Log
import com.sdk.BuildConfig
import com.sdk.ConnectedMindSDK
import com.sdk.ui.ListView
import com.sdk.ui.interfaces.ArticleClickListener
import com.sdk.ui.interfaces.ClipClickListener
import com.sdk.ui.interfaces.PodcastClickListener
import com.sdk.utils.Constants
import com.sdk.utils.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.HashMap

object CallApi {
    val TAG = "connectedMind"
    fun login(userName : String? = null, password : String? = null, listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            if (!userName.isNullOrEmpty() && !password.isNullOrEmpty()){
                val param: MutableMap<String?, Any?> = HashMap()
                param["username"] = userName
                param["password"] = password
                CoroutineScope(Dispatchers.Main).launch {
                    ConnectedMindSDK().instance?.let {
                        try{
                            val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).loginSdkUsers(param)
                            if (data?.code == 200) {
                                data.content?.let { content ->
                                    content.get("data")?.let { data ->
                                        val secret = data.asJsonObject.get("secret").asString
                                        val userId = data.asJsonObject.get("id").asString
                                        val prefManager = PrefManager(it)
                                        prefManager.setToken(secret)
                                        prefManager.setUserId(userId)
                                        Log.e("jigarLogs","secret = "+secret)
                                        Log.e("jigarLogs","userId = "+userId)
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }
                            }else{
                                listener?.error(data?.error?.userMessage, apiName)
                            }
                        } catch (e: HttpException) {
                            listener?.error(e.response()?.message(), apiName)
                        } catch (e: IOException) {
                            listener?.error(e.message, apiName)
                        }
                    }
                }
            }else{
                listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getAllPodcast(listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    val param: MutableMap<String?, Any?> = HashMap()
                    param["user_id"] = prefManager.getUserId()
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getAllPodcast(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }
    fun getPodcastById(id : String,listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    val param: MutableMap<String?, Any?> = HashMap()
                    param["user_id"] = prefManager.getUserId()
                    param["podcast_id"] = id
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getPodcastById(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getAllPodcastByEmotionId(param: MutableMap<String?, Any?>, listener: ApiResponsesListener? = null, apiName: String? = null, clickListener : PodcastClickListener? = null,isViewNeed : Boolean = false){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    param["user_id"] = prefManager.getUserId()
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getAllPodcastByEmotionId(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        val view = if (isViewNeed){
                                            ListView.getPodcastByEmotionIdView(content,clickListener)
                                        }else{
                                            null
                                        }
                                        listener?.success(apiName = apiName, content = content, view = view)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getAllArticle(listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    val param: MutableMap<String?, Any?> = HashMap()
                    param["user_id"] = prefManager.getUserId()
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getAllArticle(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getArticleById(id : String,listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    val param: MutableMap<String?, Any?> = HashMap()
                    param["user_id"] = prefManager.getUserId()
                    param["article_id"] = id
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getArticleById(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getAllArticleByEmotionId(param: MutableMap<String?, Any?>, listener: ApiResponsesListener? = null, apiName : String? = null, clickListener : ArticleClickListener? = null, isViewNeed : Boolean = false){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    param["user_id"] = prefManager.getUserId()
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getAllArticleByEmotionId(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        val view = if (isViewNeed){
                                            ListView.getArticleByEmotionIdView(content,clickListener)
                                        }else{
                                            null
                                        }
                                        listener?.success(apiName = apiName, content = content,view = view)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getAllClip(listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    val param: MutableMap<String?, Any?> = HashMap()
                    param["user_id"] = prefManager.getUserId()
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getAllClip(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getClipById(id : String,listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    val param: MutableMap<String?, Any?> = HashMap()
                    param["user_id"] = prefManager.getUserId()
                    param["clip_id"] = id
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getClipById(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getAllClipByEmotionId(param: MutableMap<String?, Any?>,listener: ApiResponsesListener? = null, apiName : String? = null, clickListener : ClipClickListener? = null, isViewNeed : Boolean = false){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    param["user_id"] = prefManager.getUserId()
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getAllClipByEmotionId(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        val view = if (isViewNeed){
                                            ListView.getClipByEmotionIdView(content,clickListener)
                                        }else{
                                            null
                                        }
                                        listener?.success(apiName = apiName, content = content, view = view)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    fun getTrendingFeed(feed_type : List<String> = arrayListOf(), search_string : String? = null, listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    val param: MutableMap<String?, Any?> = HashMap()
                    param["user_id"] = prefManager.getUserId()
                    param["feed_type"] = feed_type
                    if (!search_string.isNullOrEmpty()){
                        param["search_string"] = search_string
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getTrendingFeed(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }
    fun getFeedEmotions(feed_type : String, listener: ApiResponsesListener? = null, apiName : String? = null){
        if (checkIsInitialize()){
            ConnectedMindSDK().instance?.let { context ->
                val prefManager = PrefManager(context)
                if (!prefManager.getToken().isNullOrEmpty()){
                    val param: MutableMap<String?, Any?> = HashMap()
                    param["user_id"] = prefManager.getUserId()
                    param["feed_type"] = feed_type
                    CoroutineScope(Dispatchers.Main).launch {
                        ConnectedMindSDK().instance?.let {
                            try{
                                val data = ServiceGenerator.buildApi(Api::class.java, it, BuildConfig.BASE_URL).getFeedEmotions(param)
                                if (data?.code == 200) {
                                    data.content?.let { content ->
                                        listener?.success(apiName = apiName, content = content)
                                    }
                                }else{
                                    listener?.error(data?.error?.userMessage, apiName)
                                }
                            } catch (e: HttpException) {
                                listener?.error(e.response()?.message(), apiName)
                            } catch (e: IOException) {
                                listener?.error(e.message, apiName)
                            }
                        }
                    }
                }else{
                    listener?.error(Constants.AUTH_LOGIN_ERROR,apiName)
                }
            }
        }else{
            listener?.error(Constants.AUTH_SDK_ERROR,apiName)
        }
    }

    private fun checkIsInitialize(): Boolean {
        return ConnectedMindSDK().instance != null
    }
}