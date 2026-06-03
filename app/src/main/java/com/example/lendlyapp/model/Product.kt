package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")                 val id: String,
    @SerializedName("name")               val name: String,
                                          val shortName: String,
    @SerializedName("image")              val imageAsset: String,
    @SerializedName("monthlyInstallment") val monthlyPayment: Double,
    @SerializedName("price")              val totalPrice: Double,
    @SerializedName("category")           val category: String,
    @SerializedName("brand")              val brand: String,
    @SerializedName("installmentMonths")  val installmentMonths: Int,
    @SerializedName("currency")           val currency: String,
    @SerializedName("isAvailable")        val isAvailable: Boolean,
    @SerializedName("description")        val description: String,
    @SerializedName("rating")             val rating: Double,
    @SerializedName("reviewCount")        val reviewCount: Int,
)
