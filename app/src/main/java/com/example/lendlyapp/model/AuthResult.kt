package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName
data class UserApiResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("user")    val user: User? = null,
)
