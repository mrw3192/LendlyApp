package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.auth.AuthRepository
import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.model.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

/**
 * Holds the remembered / returning-user profile retrieved from DataStore.
 */
data class RememberedUser(
    val name: String,
    val phone: String,
    val email: String,
    val avatar: String?,
) {
    /** Two-letter initials, e.g. "John Doe" → "JD". */
    val initials: String
        get() = name.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercaseChar().toString() }
}

// ─── ViewModel ─────────────────────────────────────────────────────────────────

/**
 * Handles login form validation and authentication using [AuthRepository].
 *
 * Supports two modes:
 *  - **Standard login**: email + password fields.
 *  - **Returning user**: shows the saved user card (name, phone, initials)
 *    and only requires the password. The user can switch to standard login
 *    via [changeUser].
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
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

    // ── Returning user state ───────────────────────────────────────────────

    private val _isReturningUser = MutableStateFlow(false)
    val isReturningUser: StateFlow<Boolean> = _isReturningUser.asStateFlow()

    private val _rememberedUser = MutableStateFlow<RememberedUser?>(null)
    val rememberedUser: StateFlow<RememberedUser?> = _rememberedUser.asStateFlow()

    init {
        loadRememberedUser()
    }

    /** Checks DataStore for a remembered user profile. */
    private fun loadRememberedUser() {
        viewModelScope.launch {
            val name = userPreferences.rememberedName.first()
            val phone = userPreferences.rememberedPhone.first()
            val email = userPreferences.rememberedEmail.first()
            val avatar = userPreferences.rememberedAvatar.first()

            if (!name.isNullOrBlank() && !email.isNullOrBlank()) {
                _rememberedUser.value = RememberedUser(
                    name = name,
                    phone = phone ?: "",
                    email = email,
                    avatar = avatar,
                )
                _isReturningUser.value = true
                // Pre-fill email from remembered user for the login request
                _email.value = email
            }
        }
    }

    /**
     * Switches from "returning user" mode to standard login.
     * Called when the user taps the "Change" button on the returning-user card.
     */
    fun changeUser() {
        _isReturningUser.value = false
        _email.value = ""
        _password.value = ""
    }

    // ── Validation ─────────────────────────────────────────────────────────

    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // ── Login action ───────────────────────────────────────────────────────

    fun login() {
        val currentEmail = if (_isReturningUser.value) {
            _rememberedUser.value?.email?.trim() ?: _email.value.trim()
        } else {
            _email.value.trim()
        }
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

        // Perform login via repository
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            val result = authRepository.login(LoginRequest(currentEmail, currentPassword))
            result.onSuccess { token ->
                _uiState.value = LoginUiState.Success(token)
            }.onFailure { error ->
                _uiState.value = LoginUiState.Error(error.message ?: "An error occurred during login")
            }
        }
    }

    /** Resets the UI state to Idle (e.g. after dismissing an error). */
    fun clearError() {
        if (_uiState.value is LoginUiState.Error) {
            _uiState.value = LoginUiState.Idle
        }
    }
}
