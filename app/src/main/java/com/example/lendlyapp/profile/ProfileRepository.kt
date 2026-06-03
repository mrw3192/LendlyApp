package com.example.lendlyapp.profile

import com.example.lendlyapp.model.User

interface ProfileRepository {
    suspend fun getUserProfile(userId: String): Result<User>
    suspend fun updateUserProfile(user: User): Result<Unit>
}