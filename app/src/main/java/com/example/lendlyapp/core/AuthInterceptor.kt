package com.example.lendlyapp.core

import com.example.lendlyapp.data.local.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Intercepts outgoing HTTP requests and injects the `Authorization: Bearer <token>`
 * header if a session token is present in the local DataStore.
 * 
 * Note: Uses runBlocking to fetch the flow synchronously because OkHttp Interceptors
 * run on a background thread and require synchronous return.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip adding token if request already has an Authorization header
        if (originalRequest.header("Authorization") != null) {
            return chain.proceed(originalRequest)
        }

        // Fetch token synchronously (safe since Interceptors run on background threads)
        val token = runBlocking {
            userPreferences.authToken.firstOrNull()
        }

        val requestBuilder = originalRequest.newBuilder()
        
        if (!token.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
