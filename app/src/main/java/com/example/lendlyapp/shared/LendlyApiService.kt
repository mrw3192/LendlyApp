package com.example.lendlyapp.shared

import com.example.lendlyapp.model.LoanApplyApiResponse
import com.example.lendlyapp.model.LoanApplyRequest
import com.example.lendlyapp.model.LoginRequest
import com.example.lendlyapp.model.LoginResponse
import com.example.lendlyapp.model.LoansApiResponse
import com.example.lendlyapp.model.ProductsApiResponse
import com.example.lendlyapp.model.RegisterRequest
import com.example.lendlyapp.model.RegisterResponse
import com.example.lendlyapp.model.TransactionsApiResponse
import com.example.lendlyapp.model.UserApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// All endpoints require x-api-key header — injected globally by AuthInterceptor.
// Base URL: ApiConfig.BASE_URL  (SPEC_TECNICO §5)
//
// Every response is wrapped: { "success": true, "<data>": ... }
interface LendlyApiService {

    // ── Auth ──────────────────────────────────────────────────────────────────
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/create")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    // ── User ──────────────────────────────────────────────────────────────────
    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: String): UserApiResponse

    // ── Loans ─────────────────────────────────────────────────────────────────
    @GET("loans")
    suspend fun getLoans(): LoansApiResponse

    @POST("loans/apply")
    suspend fun applyLoan(@Body request: LoanApplyRequest): LoanApplyApiResponse

    // ── Transactions ──────────────────────────────────────────────────────────
    @GET("transactions")
    suspend fun getTransactions(): TransactionsApiResponse

    // ── Products ──────────────────────────────────────────────────────────────
    @GET("products")
    suspend fun getProducts(): ProductsApiResponse
}
