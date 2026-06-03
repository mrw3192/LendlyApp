package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

/**
 * Legacy wrapper for backward compatibility during merge.
 */
data class LoansResponse(
    @SerializedName("results")
    val results: List<LoanDTO>
) {
    fun toModel(): List<Loan> = results.map { dto ->
        Loan(
            id = dto.id,
            amount = dto.amount,
            installments = dto.installments,
            monthlyPayment = dto.monthlyPayment,
            totalToPay = dto.amount * 1.15,
            status = dto.status,
            nextPaymentDate = dto.nextPaymentDate
        )
    }
}
