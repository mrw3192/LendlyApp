package com.example.lendlyapp.ui.screens.shop

import com.example.lendlyapp.model.Product
import com.example.lendlyapp.model.ShopBrand
import com.example.lendlyapp.model.ShopCategory

sealed class ShopUiState {
    data object Idle    : ShopUiState()
    data object Loading : ShopUiState()
    data class Success(
        val featured: List<Product>,
        val bestSellers: List<Product>,
        val categories: List<ShopCategory>,
        val brands: List<ShopBrand>,
    ) : ShopUiState()
    data class Error(val message: String) : ShopUiState()
}
