package com.example.lendlyapp.profile.usecase

import com.example.lendlyapp.model.User
import com.example.lendlyapp.profile.ProfileRepository
import javax.inject.Inject


class UpdateUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        if (user.fullName.isBlank()) {
            return Result.failure(Exception("El nombre no puede estar vacío"))
        }

        return repository.updateUserProfile(user)
    }
}