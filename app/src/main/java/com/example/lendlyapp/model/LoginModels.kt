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
 * Data Transfer Object for POST /auth/login response.
 *
 * Example mock server response:
 * {
 *   "success": true,
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 * }
 */
data class LoginResponse(
    val success: Boolean,
    val token: String?,
    // Optional error message if the API returns one on failure
    val message: String? = null,
)
