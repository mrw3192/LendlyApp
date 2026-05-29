package com.example.lendlyapp.shared

import com.example.lendlyapp.model.LoginRequest
import com.example.lendlyapp.model.LoginResponse
import com.example.lendlyapp.model.RegisterRequest
import com.example.lendlyapp.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface for Authentication endpoints.
 */
interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/create")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
}
