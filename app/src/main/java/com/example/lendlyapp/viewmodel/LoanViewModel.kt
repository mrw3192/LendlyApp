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

    // Estado principal de la UI (Carga, Éxito, Error)
    private val _uiState = mutableStateOf<LoanUiState>(LoanUiState.Loading)
    val uiState: State<LoanUiState> = _uiState

    var amountInput = mutableStateOf("")
        private set
    
    var installmentsInput = mutableStateOf("12")
        private set

    var purposeInput = mutableStateOf("Educational")
        private set

    // Simulación reactiva: se calcula cada vez que cambian los inputs
    val simulatedMonthlyPayment: Double
        get() = (amountInput.value.toDoubleOrNull() ?: 0.0) * 1.15 / (installmentsInput.value.toIntOrNull() ?: 1)

    val simulatedTotal: Double
        get() = (amountInput.value.toDoubleOrNull() ?: 0.0) * 1.15

    init {
        fetchLoans()
    }

    fun onAmountChange(newValue: String) {
        if (newValue.all { it.isDigit() }) {
            amountInput.value = newValue
        }
    }

    fun onInstallmentsChange(newValue: String) {
        installmentsInput.value = newValue
    }

    fun onPurposeChange(newValue: String) {
        purposeInput.value = newValue
    }

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
     * Procesa la solicitud de un nuevo préstamo usando los datos del formulario.
     */
    fun applyForLoan() {
        val amount = amountInput.value.toDoubleOrNull() ?: return
        val installments = installmentsInput.value.toIntOrNull() ?: return

        _uiState.value = LoanUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = applyForLoanUseCase(amount, installments)
                _uiState.value = LoanUiState.Success(result)
                // Limpiamos el formulario tras el éxito
                amountInput.value = ""
            } catch (e: Exception) {
                _uiState.value = LoanUiState.Error("Error al solicitar el préstamo")
            }
        }
    }
}
