package com.example.iiifa_fan_android.di

import android.app.Application
import android.content.Context
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.data.api.CommonApi
import com.example.iiifa_fan_android.data.api.LoginApi
import com.example.iiifa_fan_android.data.api.RemoteDataSource
import com.example.iiifa_fan_android.data.pref.AppPreferencesHelper
import com.example.iiifa_fan_android.data.pref.PreferenceInfo
import com.example.iiifa_fan_android.data.pref.PreferencesHelper
import com.example.iiifa_fan_android.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    // Preferences
    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String = Constants.PREF_NAME

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper = appPreferencesHelper

    @Singleton
    @Provides
    fun provideLoginApi(@ApplicationContext context: Context,remoteDataSource: RemoteDataSource): LoginApi {
        return remoteDataSource.buildApi(LoginApi::class.java, context, BuildConfig.FAN_MODULE)
    }

    @Singleton
    @Provides
    fun provideCommonApi(@ApplicationContext context: Context,remoteDataSource: RemoteDataSource): CommonApi {
        return remoteDataSource.buildApi(CommonApi::class.java,context,BuildConfig.COMMON_MODULE)
    }
}