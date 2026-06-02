package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for POST /auth/login request.
 */
data class LoginRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
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
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("creditScore") val creditScore: Int? = null,
    @SerializedName("availableBalance") val availableBalance: Double? = null,
    @SerializedName("memberSince") val memberSince: String? = null,
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
    @SerializedName("success") val success: Boolean,
    @SerializedName("token") val token: String?,
    @SerializedName("user") val user: UserDto? = null,
    // Optional error message if the API returns one on failure
    @SerializedName("message") val message: String? = null,
)

/**
 * Data Transfer Object for GET /users/{id} response.
 */
data class UserResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("user") val user: UserDto? = null,
    @SerializedName("message") val message: String? = null,
)
