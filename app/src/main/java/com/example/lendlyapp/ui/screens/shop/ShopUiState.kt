package com.example.lendlyapp.ui.screens.shop

import com.example.lendlyapp.model.Product

sealed class ShopUiState {
    data object Idle    : ShopUiState()
    data object Loading : ShopUiState()
    data class Success(val products: List<Product>) : ShopUiState()
    data class Error(val message: String) : ShopUiState()
}
