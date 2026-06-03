package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")                  val id: String = "",
    @SerializedName("name")                val name: String = "",
    @SerializedName("monthlyInstallment")  val price: Double = 0.0,
    @SerializedName("installmentMonths")   val installments: Int = 12,
    @SerializedName("image")               val imageUrl: String? = null,
)

// Wrapper for GET /products
data class ProductsApiResponse(
    @SerializedName("success")  val success: Boolean = false,
    @SerializedName("products") val products: List<Product> = emptyList(),
)
