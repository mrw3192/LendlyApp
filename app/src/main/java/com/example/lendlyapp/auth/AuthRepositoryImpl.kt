package com.example.lendlyapp.auth

import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.model.LoginRequest
import com.example.lendlyapp.model.RegisterRequest
import com.example.lendlyapp.shared.AuthApi
import com.example.lendlyapp.data.local.room.UserDao
import com.example.lendlyapp.data.local.room.UserEntity
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val userPreferences: UserPreferences,
    private val userDao: UserDao,
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<String> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && !body.token.isNullOrEmpty()) {
                    // Save token locally
                    userPreferences.saveAuthToken(body.token)
                    // Save returning-user ID for next Login screen visit
                    body.user?.let { user ->
                        userPreferences.saveRememberedUserId(user.id)
                        userDao.insertUser(
                            UserEntity(
                                id = user.id,
                                name = user.fullName,
                                phone = user.phone,
                                email = user.email,
                                avatar = user.avatar
                            )
                        )
                    }
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
                    // Removed auto-login: the user will be redirected to the Login Screen
                    // where they will use the saved remembered user profile to log in.
                    // Save returning-user ID
                    body.user?.let { user ->
                        userPreferences.saveRememberedUserId(user.id)
                        userDao.insertUser(
                            UserEntity(
                                id = user.id,
                                name = user.fullName,
                                phone = user.phone,
                                email = user.email,
                                avatar = user.avatar
                            )
                        )
                    }
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

    override suspend fun getUser(id: Int): Result<com.example.lendlyapp.model.UserDto> {
        return try {
            // 1. Try to fetch from API
            val response = api.getUser(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.user != null) {
                    val user = body.user
                    // Cache in Room
                    userDao.insertUser(
                        UserEntity(
                            id = user.id,
                            name = user.fullName,
                            phone = user.phone,
                            email = user.email,
                            avatar = user.avatar
                        )
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to fetch user from API"))
                }
            } else {
                // Network call failed, fallback to Room
                fallbackToRoom(id)
            }
        } catch (e: Exception) {
            // Exception (e.g. timeout, no internet), fallback to Room
            fallbackToRoom(id)
        }
    }

    private suspend fun fallbackToRoom(id: Int): Result<com.example.lendlyapp.model.UserDto> {
        val cachedUser = userDao.getUserById(id)
        return if (cachedUser != null) {
            Result.success(
                com.example.lendlyapp.model.UserDto(
                    id = cachedUser.id,
                    fullName = cachedUser.name,
                    phone = cachedUser.phone,
                    email = cachedUser.email,
                    avatar = cachedUser.avatar
                )
            )
        } else {
            Result.failure(Exception("User not found in local database and API is unreachable"))
        }
    }
}
