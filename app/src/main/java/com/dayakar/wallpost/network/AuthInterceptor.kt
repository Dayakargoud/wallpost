package com.dayakar.wallpost.network

import com.dayakar.wallpost.utils.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Created By DAYAKAR GOUD BANDARI on 30-04-2022.
 */

class AuthInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder=chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${API_KEY}")
            .build()
        return chain.proceed(requestBuilder)
    }

}