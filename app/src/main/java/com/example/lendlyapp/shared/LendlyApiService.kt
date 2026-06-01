package com.example.lendlyapp.shared

import com.example.lendlyapp.model.ProductResponse
import com.example.lendlyapp.model.PurchaseRequest
import com.example.lendlyapp.model.PurchaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit interface for Shop-related endpoints (SPEC_TECNICO §5.3).
 */
interface LendlyApiService {

    @GET("products")
    suspend fun getProducts(): Response<List<ProductResponse>>

    @POST("purchases/create")
    suspend fun createPurchase(
        @Body request: PurchaseRequest,
    ): Response<PurchaseResponse>
}
