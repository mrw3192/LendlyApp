package com.example.lendlyapp.data.repository

import com.example.lendlyapp.model.Product
import com.example.lendlyapp.model.ProductResponse
import com.example.lendlyapp.model.ShopBrand
import com.example.lendlyapp.model.ShopCategory
import com.example.lendlyapp.model.ShopData
import com.example.lendlyapp.shared.LendlyApiService
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: LendlyApiService,
) : ProductRepository {

    override suspend fun getShopData(): Result<ShopData> {
        return try {
            val response = api.getShopData()
            if (response.isSuccessful) {
                val body = response.body()
                    ?: return Result.failure(Exception("Empty response"))
                Result.success(
                    ShopData(
                        featured   = body.featured.map { it.toDomain() },
                        products   = body.products.map { it.toDomain() },
                        categories = body.categories.map { ShopCategory(it.id, it.name, it.icon) },
                        brands     = body.brands.map { ShopBrand(it.id, it.name, it.logo) },
                    )
                )
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun ProductResponse.toDomain() = Product(
        id                = id,
        name              = name,
        shortName         = name.take(14),
        imageAsset        = image,
        monthlyPayment    = monthlyInstallment,
        totalPrice        = price,
        category          = category,
        brand             = brand,
        installmentMonths = installmentMonths,
        currency          = currencySymbol(currency),
        isAvailable       = isAvailable,
        description       = description ?: "",
        rating            = rating,
        reviewCount       = reviewCount,
    )

    private fun currencySymbol(code: String) = when (code.uppercase()) {
        "PHP" -> "₱"
        "USD" -> "$"
        else  -> code
    }
}
