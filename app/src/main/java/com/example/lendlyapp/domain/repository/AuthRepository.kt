package com.example.lendlyapp.domain.repository

import com.example.lendlyapp.data.network.model.LoginRequest
import com.example.lendlyapp.data.network.model.LoginResponse
import com.example.lendlyapp.data.network.model.RegisterRequest

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun register(request: RegisterRequest): Result<LoginResponse>
    suspend fun logout()
}
