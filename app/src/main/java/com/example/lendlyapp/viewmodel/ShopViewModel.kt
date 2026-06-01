package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.repository.ProductRepository
import com.example.lendlyapp.ui.screens.shop.ShopUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShopUiState>(ShopUiState.Loading)
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    private val _recentSearches = MutableStateFlow(emptyList<String>())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    fun addRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return
        val list = _recentSearches.value.toMutableList()
        list.remove(trimmed)
        list.add(0, trimmed)
        _recentSearches.value = list.take(20)
    }

    fun removeRecentSearch(query: String) {
        _recentSearches.value = _recentSearches.value.filter { it != query }
    }

    fun clearAllRecentSearches() {
        _recentSearches.value = emptyList()
    }

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = ShopUiState.Loading
            productRepository.getShopData()
                .onSuccess { data ->
                    _uiState.value = ShopUiState.Success(
                        featured    = data.featured,
                        bestSellers = data.products.sortedByDescending { it.rating }.take(3),
                        categories  = data.categories,
                        brands      = data.brands,
                    )
                }
                .onFailure { error -> _uiState.value = ShopUiState.Error(error.message ?: "Error loading products") }
        }
    }

    fun retry() = loadProducts()
}
