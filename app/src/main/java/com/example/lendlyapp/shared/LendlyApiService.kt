package com.example.lendlyapp.shared

import com.example.lendlyapp.model.LoanApplyApiResponse
import com.example.lendlyapp.model.LoanApplyRequest
import com.example.lendlyapp.model.LoginRequest
import com.example.lendlyapp.model.LoginResponse
import com.example.lendlyapp.model.LoansApiResponse
import com.example.lendlyapp.model.PurchaseRequest
import com.example.lendlyapp.model.PurchaseResponse
import com.example.lendlyapp.model.RegisterRequest
import com.example.lendlyapp.model.RegisterResponse
import com.example.lendlyapp.model.ShopResponse
import com.example.lendlyapp.model.TransactionsApiResponse
import com.example.lendlyapp.model.UserApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LendlyApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/create")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: String): UserApiResponse

    @GET("loans")
    suspend fun getLoans(): LoansApiResponse

    @POST("loans/apply")
    suspend fun applyLoan(@Body request: LoanApplyRequest): LoanApplyApiResponse

    @GET("transactions")
    suspend fun getTransactions(): TransactionsApiResponse

    @GET("products")
    suspend fun getShopData(): Response<ShopResponse>
}
