package com.example.lendlyapp.model

data class Loan(
    val id: String,
    val amount: Double,
    val installments: Int,
    val monthlyPayment: Double,
    val totalToPay: Double,
    val status: LoanStatus,
    val balancePending: Double? = null,
    val nextPaymentDate: String? = null
)

enum class LoanStatus {
    PENDING,
    ACTIVE,
    PAID,
    REJECTED
}
