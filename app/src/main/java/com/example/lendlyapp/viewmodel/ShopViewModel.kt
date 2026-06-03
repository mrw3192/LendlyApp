package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.model.toDomain
import com.example.lendlyapp.model.ShopBrand
import com.example.lendlyapp.model.ShopCategory
import com.example.lendlyapp.shared.LendlyApiService
import com.example.lendlyapp.ui.screens.shop.ShopUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val apiService: LendlyApiService,
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
            try {
                val body = apiService.getShopData()
                _uiState.value = ShopUiState.Success(
                    featured    = body.featured.map { it.toDomain() },
                    bestSellers = body.products.map { it.toDomain() }.sortedByDescending { it.rating }.take(3),
                    categories  = body.categories.map { ShopCategory(it.id, it.name, it.icon) },
                    brands      = body.brands.map { ShopBrand(it.id, it.name, it.logo) },
                )
            } catch (e: Exception) {
                _uiState.value = ShopUiState.Error(e.message ?: "Error loading products")
            }
        }
    }

    fun retry() = loadProducts()
}
