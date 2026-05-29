package com.example.lendlyapp.auth

import com.example.lendlyapp.model.LoginRequest
import com.example.lendlyapp.model.RegisterRequest

/**
 * Repository interface for Authentication tasks.
 */
interface AuthRepository {
    
    /**
     * Attempts to log in the user.
     * @param request The login credentials.
     * @return Result containing the token on success, or an Exception on failure.
     */
    suspend fun login(request: LoginRequest): Result<String>

    /**
     * Attempts to register a new user.
     * @param request The user details.
     * @return Result containing Unit on success, or an Exception on failure.
     */
    suspend fun register(request: RegisterRequest): Result<Unit>
}
