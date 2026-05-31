package com.example.lendlyapp.usecase

import com.example.lendlyapp.data.repository.LoanRepository
import com.example.lendlyapp.model.Loan
import javax.inject.Inject

class GetLoansUseCase @Inject constructor(
    private val repository: LoanRepository
) {
    suspend operator fun invoke(): List<Loan> {
        return repository.getLoans()
    }
}
