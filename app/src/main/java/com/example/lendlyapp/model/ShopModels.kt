package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

// ─── API response wrapper ─────────────────────────────────────────────────────

data class ShopResponse(
    val success: Boolean,
    val featured: List<ProductResponse>,
    val categories: List<CategoryResponse>,
    val brands: List<BrandResponse>,
    val products: List<ProductResponse>,
)

// ─── API DTOs ─────────────────────────────────────────────────────────────────

data class ProductResponse(
    val id: String,
    val name: String,
    val brand: String,
    val category: String,
    val price: Double,
    val image: String,
    val currency: String,
    @SerializedName("monthlyInstallment") val monthlyInstallment: Double,
    @SerializedName("installmentMonths") val installmentMonths: Int,
    @SerializedName("isAvailable") val isAvailable: Boolean,
    @SerializedName("isFeatured") val isFeatured: Boolean = false,
    @SerializedName("interestRate") val interestRate: Double = 0.0,
    val rating: Double = 0.0,
    @SerializedName("reviewCount") val reviewCount: Int = 0,
    val description: String? = null,
)

data class CategoryResponse(
    val id: String,
    val name: String,
    val icon: String,
)

data class BrandResponse(
    val id: String,
    val name: String,
    val logo: String,
)

data class PurchaseRequest(
    @SerializedName("productId") val productId: String,
    val installments: Int,
)

data class PurchaseResponse(
    @SerializedName("purchaseId") val purchaseId: String,
    val status: String,
)

// ─── Domain models ────────────────────────────────────────────────────────────

data class ShopCategory(val id: String, val name: String, val icon: String)

data class ShopBrand(val id: String, val name: String, val logoUrl: String)

data class ShopData(
    val featured: List<Product>,
    val products: List<Product>,
    val categories: List<ShopCategory>,
    val brands: List<ShopBrand>,
)
