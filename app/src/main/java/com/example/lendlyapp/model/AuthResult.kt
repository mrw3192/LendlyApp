package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String,
)

data class LoginResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("token")   val token: String = "",
    @SerializedName("user")    val user: User? = null,
)


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

data class UserApiResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("user")    val user: User? = null,
)
