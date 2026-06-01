package com.example.lendlyapp.model

data class Product(
    val id: String,
    val name: String,
    val shortName: String,
    val imageAsset: String,
    val monthlyPayment: Double,
    val totalPrice: Double,
    val category: String,
    val brand: String,
    val installmentMonths: Int,
    val currency: String,
    val isAvailable: Boolean,
    val description: String,
    val rating: Double,
    val reviewCount: Int,
)
