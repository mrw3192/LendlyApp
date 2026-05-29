package com.example.lendlyapp.model

/**
 * Data Transfer Object for POST /auth/create request.
 */
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val dob: String, // format YYYY-MM-DD
    val address: String,
    val city: String,
    val postalCode: String,
    val phone: String,
    val password: String,
)

/**
 * Data Transfer Object for POST /auth/create response.
 *
 * Example mock server response:
 * {
 *   "success": true,
 *   "message": "User registered successfully.",
 *   "user": { ... }
 * }
 */
data class RegisterResponse(
    val success: Boolean,
    val message: String?,
)
