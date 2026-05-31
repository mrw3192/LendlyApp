package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

// ── POST /auth/login ──────────────────────────────────────────────────────────
// { "success": true, "token": "...", "user": { ... } }

data class LoginRequest(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String,
)

data class LoginResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("token")   val token: String = "",
    @SerializedName("user")    val user: User? = null,
)

// ── POST /auth/create ─────────────────────────────────────────────────────────
// { "success": true, "message": "...", "user": { ... }, "token": "..." }

data class RegisterRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName")  val lastName: String,
    @SerializedName("dni")       val dni: String,
    @SerializedName("email")     val email: String,
    @SerializedName("password")  val password: String,
)

data class RegisterResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("user")    val user: User? = null,
    @SerializedName("token")   val token: String = "",
)

// ── GET /users/{id} ───────────────────────────────────────────────────────────
// { "success": true, "user": { ... } }

data class UserApiResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("user")    val user: User? = null,
)
