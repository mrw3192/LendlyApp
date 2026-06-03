package com.example.lendlyapp.usecase

import com.example.lendlyapp.data.repository.LoanRepository
import com.example.lendlyapp.model.Loan
import javax.inject.Inject

class ApplyForLoanUseCase @Inject constructor(
    private val repository: LoanRepository
) {
    suspend operator fun invoke(amount: Double, installments: Int): List<Loan> {
        // Acá podríamos agregar lógica de validación de negocio antes de llamar al repositorio
        return repository.applyForLoan(amount, installments)
    }
}
