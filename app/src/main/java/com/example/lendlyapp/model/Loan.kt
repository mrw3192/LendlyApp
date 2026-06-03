package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

/**
 * Representa un préstamo en el sistema.
 * Combinación del modelo de master y campos necesarios para la UI de Préstamos.
 */
data class Loan(
    @SerializedName("id")               val id: String = "",
    @SerializedName("lender")           val companyName: String = "",
    @SerializedName("amountDue")        val amount: Double = 0.0,
    @SerializedName("nextPaymentLabel") val description: String? = null,
    @SerializedName("lenderLogo")       val lenderLogo: String? = null,
    @SerializedName("status")           val status: String = "",
    // Campos adicionales para compatibilidad con la UI previa
    val monthlyPayment: Double = 0.0,
    val totalToPay: Double = 0.0,
    val installments: Int = 0,
    val nextPaymentDate: String? = null
)

/**
 * Estados posibles de un préstamo (para lógica de UI).
 */
enum class LoanStatus {
    PENDING,
    ACTIVE,
    PAID,
    REJECTED
}

data class LoansApiResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("loans")   val loans: List<Loan> = emptyList(),
)

data class LoanApplyRequest(
    @SerializedName("amount")       val amount: Double,
    @SerializedName("installments") val installments: Int,
)

data class LoanApplyApiResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("loan")    val loan: LoanApplyLoan? = null,
)

data class LoanApplyLoan(
    @SerializedName("id")                val id: String = "",
    @SerializedName("amount")            val amount: Double = 0.0,
    @SerializedName("installmentAmount") val installmentAmount: Double = 0.0,
    @SerializedName("installmentPlan")   val installmentPlan: String = "",
    @SerializedName("interestRate")      val interestRate: Double = 0.0,
    @SerializedName("status")            val status: String = "",
    @SerializedName("nextPaymentDate")   val nextPaymentDate: String = "",
)
