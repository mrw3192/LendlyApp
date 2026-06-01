package com.example.lendlyapp.data.repository

import com.example.lendlyapp.model.Product

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
}
