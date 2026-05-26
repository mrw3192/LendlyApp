package com.example.lendlyapp.data.network

import com.example.lendlyapp.data.network.model.LoginRequest
import com.example.lendlyapp.data.network.model.LoginResponse
import com.example.lendlyapp.data.network.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/auth/create")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>
}
