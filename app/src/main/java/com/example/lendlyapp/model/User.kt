package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")               val id: Int = 0,
    @SerializedName("fullName")         val fullName: String = "",
    @SerializedName("phone")            val phone: String = "",
    @SerializedName("email")            val email: String = "",
    @SerializedName("avatar")           val avatar: String = "",
    @SerializedName("creditScore")      val creditScore: Int = 0,
    @SerializedName("availableBalance") val availableBalance: Double = 0.0,
    @SerializedName("memberSince")      val memberSince: String = "",
    @SerializedName("creditLimit")      val creditLimit: Double = 0.0,
    @SerializedName("totalLoanLimit")   val totalLoanLimit: Double = 0.0,
    @SerializedName("creditLevel")      val creditLevel: String = "",
    @SerializedName("birthDate")        val birthDate: String = "",
    @SerializedName("address")          val address: String = "",
    @SerializedName("isVerified")       val isVerified: Boolean = false,
)
