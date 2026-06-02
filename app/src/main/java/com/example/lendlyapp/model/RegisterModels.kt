package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for POST /auth/create request.
 */
data class RegisterRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("dob") val dob: String, // format YYYY-MM-DD
    @SerializedName("address") val address: String,
    @SerializedName("city") val city: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
)

/**
 * Data Transfer Object for POST /auth/create response.
 *
 * Example mock server response:
 * {
 *   "success": true,
 *   "message": "User registered successfully.",
 *   "user": { ... },
 *   "token": "eyJhbGciOiJIUzI1NiIs..."
 * }
 */
data class RegisterResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("user") val user: UserDto? = null,
    @SerializedName("token") val token: String? = null,
)
