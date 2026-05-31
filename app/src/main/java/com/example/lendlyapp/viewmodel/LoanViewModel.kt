package com.example.lendlyapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.model.Loan
import com.example.lendlyapp.usecase.ApplyForLoanUseCase
import com.example.lendlyapp.usecase.GetLoansUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Representa los diferentes estados de la UI para el módulo de Préstamos.
 */
sealed class LoanUiState {
    object Loading : LoanUiState()
    data class Success(val loans: List<Loan>) : LoanUiState()
    data class Error(val message: String) : LoanUiState()
}

@HiltViewModel
class LoanViewModel @Inject constructor(
    private val getLoansUseCase: GetLoansUseCase,
    private val applyForLoanUseCase: ApplyForLoanUseCase
) : ViewModel() {

    // El estado es privado para el ViewModel y se expone como inmutable (State)
    private val _uiState = mutableStateOf<LoanUiState>(LoanUiState.Loading)
    val uiState: State<LoanUiState> = _uiState

    init {
        fetchLoans()
    }

    /**
     * Obtiene la información de los préstamos al iniciar.
     */
    fun fetchLoans() {
        _uiState.value = LoanUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getLoansUseCase()
                _uiState.value = LoanUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = LoanUiState.Error("No se pudieron cargar los datos")
            }
        }
    }

    /**
     * Procesa la solicitud de un nuevo préstamo.
     */
    fun applyForLoan(amount: Double, installments: Int) {
        _uiState.value = LoanUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = applyForLoanUseCase(amount, installments)
                _uiState.value = LoanUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = LoanUiState.Error("Error al solicitar el préstamo")
            }
        }
    }
}
