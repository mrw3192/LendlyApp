package com.example.lendlyapp.data.repository

import com.example.lendlyapp.model.Loan
import javax.inject.Inject

class LoanRepository @Inject constructor(
    private val api: LendlyApiService
) {
    suspend fun getLoans(): List<Loan> {
        return api.getLoans().toModel()
    }

    suspend fun applyForLoan(amount: Double, installments: Int): List<Loan> {
        val requestBody = mapOf(
            "amount" to amount,
            "installments" to installments
        )
        return api.applyForLoan(requestBody).toModel()
    }
}
