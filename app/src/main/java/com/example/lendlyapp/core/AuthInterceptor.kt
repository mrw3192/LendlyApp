package com.example.lendlyapp.core

import okhttp3.Interceptor
import okhttp3.Response

// Adds the mandatory x-api-key header to every outgoing request (SPEC_TECNICO §5).
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(ApiConfig.API_KEY_HEADER, ApiConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }
}
