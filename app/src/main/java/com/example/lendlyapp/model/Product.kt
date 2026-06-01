package com.example.lendlyapp.model

/**
 * Domain entity for a loanable product shown in the Shop catalog.
 * [imageAsset] is the filename inside app/src/main/assets/.
 */
data class Product(
    val id: String,
    val name: String,
    val shortName: String,
    val imageAsset: String,
    val monthlyPayment: Double,
    val totalPrice: Double,
    val category: String,
    val brand: String,
)
