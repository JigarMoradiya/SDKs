package com.sdk.network

import com.sdk.data.MainAPIResponse
import retrofit2.http.*

interface Api {

	@POST("loginsdkusers")
	suspend fun loginSdkUsers(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetallpodcast")
	suspend fun getAllPodcast(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetallpodcastbyemotion")
	suspend fun getAllPodcastByEmotionId(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetpodcastbyid")
	suspend fun getPodcastById(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetallarticle")
	suspend fun getAllArticle(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetarticlebyid")
	suspend fun getArticleById(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetallarticlebyemotion")
	suspend fun getAllArticleByEmotionId(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetallclip")
	suspend fun getAllClip(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetclipbyid")
	suspend fun getClipById(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetallclipbyemotion")
	suspend fun getAllClipByEmotionId(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgettrendingfeed")
	suspend fun getTrendingFeed(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

	@POST("sdkgetfeedemotions")
	suspend fun getFeedEmotions(@Body params: Map<String?, @JvmSuppressWildcards Any?>?): MainAPIResponse?

}
