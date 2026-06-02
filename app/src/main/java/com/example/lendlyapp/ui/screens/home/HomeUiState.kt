package com.example.lendlyapp.ui.screens.home

import com.example.lendlyapp.model.Loan
import com.example.lendlyapp.model.Product

data class HomeData(
    val balance: Double,
    val unpaidLoans: List<Loan>,
    val recommendedProducts: List<Product>,
)

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: HomeData) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
