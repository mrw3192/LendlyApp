package com.example.lendlyapp.data.repository

import com.example.lendlyapp.model.Loan
import com.example.lendlyapp.model.LoanApplyRequest
import com.example.lendlyapp.shared.LendlyApiService
import javax.inject.Inject

class LoanRepository @Inject constructor(
    private val api: LendlyApiService
) {
    suspend fun getLoans(): List<Loan> {
        val response = api.getLoans()
        return if (response.success) response.loans else emptyList()
    }

    suspend fun applyForLoan(amount: Double, installments: Int): List<Loan> {
        val request = LoanApplyRequest(amount, installments)
        val response = api.applyLoan(request)
        // For presentation purposes, we return the list of loans after apply
        return getLoans()
    }
}
