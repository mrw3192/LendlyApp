package com.example.lendlyapp.ui.screens.history

import com.example.lendlyapp.model.Loan
import com.example.lendlyapp.model.Transaction

data class HistoryData(
    val transactions: List<Transaction>,
    val loans: List<Loan>,
)

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    data class Success(val data: HistoryData) : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}

sealed class TransactionDetailsUiState {
    object Loading : TransactionDetailsUiState()
    data class Success(val transaction: Transaction) : TransactionDetailsUiState()
    data class Error(val message: String) : TransactionDetailsUiState()
}
