package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.shared.LendlyApiService
import com.example.lendlyapp.ui.screens.home.HomeData
import com.example.lendlyapp.ui.screens.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── HomeViewModel ─────────────────────────────────────────────────────────────
// Calls GET /users/{id}, GET /loans and GET /products in parallel.
// All responses are wrapped objects — unwrapped here before emitting to UI.
//
// TODO: replace hardcoded "1" with userId stored in UserPreferences after login.

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: LendlyApiService,
    private val userPreferences: UserPreferences,
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
                val userId = userPreferences.userId.first() ?: "1"

                coroutineScope {
                    val userDef     = async { apiService.getUser(userId) }
                    val loansDef    = async { apiService.getLoans() }
                    val productsDef = async { apiService.getProducts() }

                    val user     = userDef.await().user
                        ?: throw Exception("User data unavailable")
                    val loans    = loansDef.await().loans
                    val products = productsDef.await().products

                    _uiState.value = HomeUiState.Success(
                        HomeData(
                            balance = user.availableBalance,
                            // Only show loans with pending payments
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
