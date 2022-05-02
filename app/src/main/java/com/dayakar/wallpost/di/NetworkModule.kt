package com.dayakar.wallpost.di

import com.dayakar.wallpost.network.AuthInterceptor
import com.dayakar.wallpost.network.PexelApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @Created By DAYAKAR GOUD BANDARI on 30-04-2022.
 */


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{


    @Provides
    @Singleton
    fun getRetroFit():Retrofit {
        val httpClient=OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
        return Retrofit.Builder().baseUrl(PexelApi.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getPexelAPI(retrofit: Retrofit):PexelApi{
        return retrofit.create(PexelApi::class.java)
    }
}