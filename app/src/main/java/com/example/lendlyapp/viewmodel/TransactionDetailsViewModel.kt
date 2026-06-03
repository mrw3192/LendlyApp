package com.example.lendlyapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.shared.LendlyApiService
import com.example.lendlyapp.ui.screens.history.TransactionDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val apiService: LendlyApiService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val transactionId: String = checkNotNull(savedStateHandle["transactionId"])

    private val _uiState = MutableStateFlow<TransactionDetailsUiState>(TransactionDetailsUiState.Loading)
    val uiState: StateFlow<TransactionDetailsUiState> = _uiState.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            _uiState.value = TransactionDetailsUiState.Loading
            try {
                val transaction = apiService.getTransactions().transactions
                    .find { it.id == transactionId }
                    ?: throw Exception("Transaction not found")
                _uiState.value = TransactionDetailsUiState.Success(transaction)
            } catch (e: Exception) {
                _uiState.value = TransactionDetailsUiState.Error(e.message ?: "Failed to load")
            }
        }
    }
}
