package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for POST /auth/login request.
 */
data class LoginRequest(
    val email: String,
    val password: String,
)

/**
 * User profile information returned by the API inside login/register responses.
 *
 * Maps to the "user" field in the mock response:
 * ```json
 * {
 *   "id": 1,
 *   "fullName": "John Doe",
 *   "phone": "+63-923456790",
 *   "email": "john.doe@email.com",
 *   "avatar": "https://i.pravatar.cc/150?img=3",
 *   "creditScore": 720,
 *   "availableBalance": 2500.00,
 *   "memberSince": "2023-01-15"
 * }
 * ```
 */
data class UserDto(
    val id: Int,
    val fullName: String,
    val phone: String,
    val email: String,
    val avatar: String?,
    val creditScore: Int? = null,
    val availableBalance: Double? = null,
    val memberSince: String? = null,
)

/**
 * Data Transfer Object for POST /auth/login response.
 *
 * Example mock server response:
 * {
 *   "success": true,
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "user": { ... }
 * }
 */
data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val user: UserDto? = null,
    // Optional error message if the API returns one on failure
    val message: String? = null,
)
