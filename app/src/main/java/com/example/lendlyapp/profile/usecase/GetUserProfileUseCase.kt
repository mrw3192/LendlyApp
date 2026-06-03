package com.example.lendlyapp.profile.usecase

import com.example.lendlyapp.model.User
import com.example.lendlyapp.profile.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Result<User> {
        return repository.getUserProfile(userId)
    }
}