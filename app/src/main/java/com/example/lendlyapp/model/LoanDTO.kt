package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

data class LoanDTO(
    @SerializedName("id") val id: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("installments") val installments: Int,
    @SerializedName("monthly_payment") val monthlyPayment: Double,
    @SerializedName("status") val status: String,
    @SerializedName("balance_pending") val balancePending: Double?,
    @SerializedName("next_payment_date") val nextPaymentDate: String?
)
