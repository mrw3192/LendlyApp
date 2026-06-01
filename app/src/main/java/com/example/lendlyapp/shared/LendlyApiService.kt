package com.example.lendlyapp.shared

import com.example.lendlyapp.model.PurchaseRequest
import com.example.lendlyapp.model.PurchaseResponse
import com.example.lendlyapp.model.ShopResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LendlyApiService {

    @GET("products")
    suspend fun getShopData(): Response<ShopResponse>
}
