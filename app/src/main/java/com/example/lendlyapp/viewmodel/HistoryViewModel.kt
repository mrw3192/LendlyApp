package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.shared.LendlyApiService
import com.example.lendlyapp.ui.screens.history.HistoryData
import com.example.lendlyapp.ui.screens.history.HistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val apiService: LendlyApiService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init { loadHistory() }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = HistoryUiState.Loading
            try {
                coroutineScope {
                    val txDef    = async { apiService.getTransactions() }
                    val loansDef = async { apiService.getLoans() }
                    _uiState.value = HistoryUiState.Success(
                        HistoryData(
                            transactions = txDef.await().transactions,
                            loans        = loansDef.await().loans,
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.value = HistoryUiState.Error(e.message ?: "Failed to load history")
            }
        }
    }
}
