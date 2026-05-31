package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

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
            totalToPay = dto.amount * 1.15, // Ejemplo de lógica de negocio (tasa sim)
            status = try {
                LoanStatus.valueOf(dto.status.uppercase())
            } catch (e: Exception) {
                LoanStatus.PENDING
            },
            balancePending = dto.balancePending,
            nextPaymentDate = dto.nextPaymentDate
        )
    }
}
