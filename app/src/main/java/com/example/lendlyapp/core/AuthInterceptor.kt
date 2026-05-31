package com.example.lendlyapp.core

import okhttp3.Interceptor
import okhttp3.Response
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(ApiConfig.API_KEY_HEADER, ApiConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }
}
