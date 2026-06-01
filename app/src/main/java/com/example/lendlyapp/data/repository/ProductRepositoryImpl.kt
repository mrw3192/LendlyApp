package com.example.lendlyapp.data.repository

import com.example.lendlyapp.model.Product
import com.example.lendlyapp.model.ProductResponse
import com.example.lendlyapp.shared.LendlyApiService
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: LendlyApiService,
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = api.getProducts()
            if (response.isSuccessful) {
                val body = response.body()
                if (!body.isNullOrEmpty()) {
                    Result.success(body.map { it.toDomain() })
                } else {
                    Result.success(mockProducts)
                }
            } else {
                Result.success(mockProducts)
            }
        } catch (e: Exception) {
            Result.success(mockProducts)
        }
    }

    private fun ProductResponse.toDomain() = Product(
        id = id,
        name = name,
        shortName = name.take(14),
        imageAsset = "img_1d9e70f017321361.png",
        monthlyPayment = monthlyPayment,
        totalPrice = totalPrice,
        category = category,
        brand = brand,
    )
}

// ─── Static mock fallback (Figma asset-backed) ────────────────────────────────

val mockProducts: List<Product> = listOf(
    Product("1", "Apple iPhone 12 Pro Max", "iPhone 12 Pro...", "img_1d9e70f017321361.png", 1200.0, 28800.0, "Phone", "Apple"),
    Product("2", "Apple iPhone 12 Pro", "iPhone 12 Pro...", "img_f4ce8621a75bef92.png", 1200.0, 28800.0, "Phone", "Apple"),
    Product("3", "Apple iPhone 12", "iPhone 12 Pro...", "img_4212ff9d8c27fb90.png", 1200.0, 28800.0, "Phone", "Apple"),
    Product("4", "Surface Laptop", "Surface Laptop", "product_1.png", 1200.0, 28800.0, "Electronics", "Microsoft"),
    Product("5", "Apple iPhone 12 Pro", "iPhone 12 Pro...", "img_84afada02ee9075f.png", 1200.0, 28800.0, "Phone", "Apple"),
    Product("6", "PS4 PlayStation", "PS4 Play Stati...", "img_0feafd9ff159fc89.png", 1200.0, 28800.0, "Gaming", "Sony"),
)
