package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.model.toDomain
import com.example.lendlyapp.shared.LendlyApiService
import com.example.lendlyapp.ui.screens.home.HomeData
import com.example.lendlyapp.ui.screens.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: LendlyApiService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val userId = "1"

                coroutineScope {
                    val userDef  = async { apiService.getUser(userId) }
                    val loansDef = async { apiService.getLoans() }
                    val shopDef  = async { apiService.getShopData() }

                    val user     = userDef.await().user
                        ?: throw Exception("User data unavailable")
                    val loans    = loansDef.await().loans
                    val products = shopDef.await().products.map { it.toDomain() }

                    _uiState.value = HomeUiState.Success(
                        HomeData(
                            balance = user.availableBalance,
                            unpaidLoans = loans.filter { it.status == "ACTIVE" },
                            recommendedProducts = products,
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Failed to load data")
            }
        }
    }
}
