package com.example.lendlyapp.core

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Intercepts all outgoing HTTP requests and injects the [x-api-key] header.
 * Required by the Postman Mock Server for all endpoints.
 */
@Singleton
class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val newRequest = originalRequest.newBuilder()
            .header("x-api-key", ApiConfig.API_KEY)
            .build()
            
        return chain.proceed(newRequest)
    }
}
