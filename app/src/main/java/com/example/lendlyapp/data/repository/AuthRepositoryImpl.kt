package com.example.lendlyapp.data.repository

import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.data.network.AuthApi
import com.example.lendlyapp.data.network.model.LoginRequest
import com.example.lendlyapp.data.network.model.LoginResponse
import com.example.lendlyapp.data.network.model.RegisterRequest
import com.example.lendlyapp.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                userPreferences.saveAuthToken(loginResponse.token)
                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Error de credenciales"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<LoginResponse> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                userPreferences.saveAuthToken(loginResponse.token)
                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Error al registrar el usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        userPreferences.clearAuthToken()
    }
}
