package com.example.lendlyapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── UI State ──────────────────────────────────────────────────────────────────

/** Login screen state following the sealed-class pattern (SPEC_TECNICO §6). */
sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

// ─── ViewModel ─────────────────────────────────────────────────────────────────

/**
 * Handles login form validation and authentication.
 *
 * API contract (SPEC_TECNICO §5.1):
 *   POST /auth/login  { email, password } → { token, userId, email }
 *
 * Currently mocked: any valid email + non-empty password → success with fake token.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // ── Form fields ────────────────────────────────────────────────────────

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }

    // ── Validation ─────────────────────────────────────────────────────────

    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // ── Login action ───────────────────────────────────────────────────────

    fun login() {
        val currentEmail = _email.value.trim()
        val currentPassword = _password.value

        // Validate email
        if (!isEmailValid(currentEmail)) {
            _uiState.value = LoginUiState.Error("Please enter a valid email address")
            return
        }

        // Validate password
        if (currentPassword.isEmpty()) {
            _uiState.value = LoginUiState.Error("Password cannot be empty")
            return
        }

        // Perform login (mocked)
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            delay(800) // Simulate network call
            val fakeToken = "mock_token_${System.currentTimeMillis()}"
            userPreferences.saveAuthToken(fakeToken)
            _uiState.value = LoginUiState.Success(fakeToken)
        }
    }

    /** Resets the UI state to Idle (e.g. after dismissing an error). */
    fun clearError() {
        if (_uiState.value is LoginUiState.Error) {
            _uiState.value = LoginUiState.Idle
        }
    }
}
