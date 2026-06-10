package com.example.lendlyapp.navigation

import android.net.Uri
import com.example.lendlyapp.model.LoanApplyLoan

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Onboarding : AppDestination("onboarding")
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object Home : AppDestination("home")
    data object CashIn : AppDestination("cash_in")
    data object CashInOnline : AppDestination("cash_in_online")
    data object CashInOverTheCounter : AppDestination("cash_in_over_the_counter")
    data object CashInAmount : AppDestination("cash_in_amount/{bankName}") {
        fun createRoute(bankName: String) = "cash_in_amount/${Uri.encode(bankName)}"
    }
    data object SuccessfulTransaction : AppDestination("successful_transaction/{partnerName}/{amount}") {
        fun createRoute(partnerName: String, amount: String) =
            "successful_transaction/${Uri.encode(partnerName)}/${Uri.encode(amount)}"
    }
    data object VerifyPhone : AppDestination("verifyPhone")
    data object SmsVerification : AppDestination("smsVerification")
    data object ProfileDetail : AppDestination("profileDetail")
    data object CreatePassword : AppDestination("createPassword")
    data object Done : AppDestination("done")
    data object IdVerification : AppDestination("idVerification")
    data object FaceRecognition : AppDestination("faceRecognition")
    data object Signature : AppDestination("signature")
    data object Verified : AppDestination("verified")
    data object TransactionDetails : AppDestination("transaction_details/{transactionId}") {
        fun createRoute(transactionId: String) = "transaction_details/${Uri.encode(transactionId)}"
    }

    // ─── Loan Module ──────────────────────────────────────────────────────────
    data object LoanInfo : AppDestination("loan_info")
    data object LoanForm : AppDestination("loan_form")
    data object LoanActive : AppDestination("loan_active")
    data object LoanSuccess : AppDestination(
        "loan_success?loanId={loanId}&amount={amount}&installmentAmount={installmentAmount}" +
        "&installmentPlan={installmentPlan}&interestRate={interestRate}" +
        "&status={status}&nextPaymentDate={nextPaymentDate}"
    ) {
        fun createRoute(loan: LoanApplyLoan) =
            "loan_success?loanId=${Uri.encode(loan.id)}" +
            "&amount=${loan.amount}" +
            "&installmentAmount=${loan.installmentAmount}" +
            "&installmentPlan=${Uri.encode(loan.installmentPlan)}" +
            "&interestRate=${loan.interestRate}" +
            "&status=${Uri.encode(loan.status)}" +
            "&nextPaymentDate=${Uri.encode(loan.nextPaymentDate)}"
    }
    // ─── Manage Module ────────────────────────────────────────────────────────
    data object EditProfile : AppDestination("edit_profile")
    data object CreditScore : AppDestination("credit_score")
    data object ProfileDone : AppDestination("profile_done")
}
