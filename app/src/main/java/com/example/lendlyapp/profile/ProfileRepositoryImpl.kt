package com.example.lendlyapp.profile

import com.example.lendlyapp.model.User
import com.example.lendlyapp.shared.LendlyApiService
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: LendlyApiService
) : ProfileRepository {

    override suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            val response = api.getUser(userId)
            if (response.success && response.user != null) {
                // Adaptamos el UserDto al modelo de dominio User
                Result.success(response.user)
            } else {
                Result.failure(Exception("Error al obtener perfil"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            // Simulamos la actualización por ahora ya que la API no tiene el POST de user
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}