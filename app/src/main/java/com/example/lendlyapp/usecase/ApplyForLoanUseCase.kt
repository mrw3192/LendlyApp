package com.example.lendlyapp.usecase

import com.example.lendlyapp.data.repository.LoanRepository
import com.example.lendlyapp.model.LoanApplyLoan
import javax.inject.Inject

class ApplyForLoanUseCase @Inject constructor(
    private val repository: LoanRepository
) {
    suspend operator fun invoke(amount: Double, installments: Int): LoanApplyLoan? {
        return repository.applyForLoan(amount, installments)
    }
}
