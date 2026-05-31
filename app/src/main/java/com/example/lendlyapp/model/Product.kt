package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

// GET /products → { "success": true, "products": [ ... ] }
//
// API field mapping:
//   monthlyInstallment → price        (shown as "₱1,200" in the card)
//   installmentMonths  → installments (shown as "x 24 mo")
//   image              → imageUrl     (URL — loaded directly by Coil)
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
