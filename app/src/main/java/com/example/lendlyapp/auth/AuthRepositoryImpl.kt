package com.example.lendlyapp.auth

import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.model.LoginRequest
import com.example.lendlyapp.model.RegisterRequest
import com.example.lendlyapp.shared.AuthApi
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<String> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && !body.token.isNullOrEmpty()) {
                    // Save token locally
                    userPreferences.saveAuthToken(body.token)
                    Result.success(body.token)
                } else {
                    Result.failure(Exception(body?.message ?: "Unknown login error"))
                }
            } else {
                Result.failure(Exception("HTTP Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(body?.message ?: "Unknown registration error"))
                }
            } else {
                Result.failure(Exception("HTTP Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
