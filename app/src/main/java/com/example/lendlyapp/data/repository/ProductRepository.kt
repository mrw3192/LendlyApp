package com.example.lendlyapp.data.repository

import com.example.lendlyapp.model.ShopData

interface ProductRepository {
    suspend fun getShopData(): Result<ShopData>
}
