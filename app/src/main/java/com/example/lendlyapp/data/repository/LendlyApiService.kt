package com.example.lendlyapp.data.repository

import com.example.lendlyapp.model.LoansResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface LendlyApiService {
    
    @GET("loans")
    suspend fun getLoans(): LoansResponse

    @POST("loans/apply")
    suspend fun applyForLoan(@Body request: Map<String, Any>): LoansResponse
}
