package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

// ─── API DTOs (SPEC_TECNICO §5.3) ─────────────────────────────────────────────

data class ProductResponse(
    val id: String,
    val name: String,
    @SerializedName("monthly_payment") val monthlyPayment: Double,
    @SerializedName("total_price") val totalPrice: Double,
    val category: String,
    val brand: String,
)

data class PurchaseRequest(
    @SerializedName("productId") val productId: String,
    val installments: Int,
)

data class PurchaseResponse(
    @SerializedName("purchaseId") val purchaseId: String,
    val status: String,
)
