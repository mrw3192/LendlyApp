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

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    fun onPhoneChange(value: String) { 
        if (value.isEmpty() || value.matches(Regex("^\\+?[0-9]*$"))) {
            _phone.value = value 
            _phoneError.value = null
        }
    }
    fun onPasswordChange(value: String) { 
        _password.value = value 
        _passwordError.value = null
    }

    // ── Returning user state ───────────────────────────────────────────────

    private val _isReturningUser = MutableStateFlow(false)
    val isReturningUser: StateFlow<Boolean> = _isReturningUser.asStateFlow()

    private val _rememberedUser = MutableStateFlow<RememberedUser?>(null)
    val rememberedUser: StateFlow<RememberedUser?> = _rememberedUser.asStateFlow()

    init {
        loadRememberedUser()
    }

    /** Checks DataStore for a remembered user ID and fetches profile. */
    private fun loadRememberedUser() {
        viewModelScope.launch {
            val userId = userPreferences.rememberedUserId.first()
            if (userId != null) {
                // Fetch from Repository (API + Room cache)
                val result = authRepository.getUser(userId)
                result.onSuccess { userDto ->
                    _rememberedUser.value = RememberedUser(
                        name = userDto.fullName,
                        phone = userDto.phone,
                        email = userDto.email,
                        avatar = userDto.avatar,
                    )
                    _isReturningUser.value = true
                    // Pre-fill phone from remembered user for the login request
                    _phone.value = userDto.phone
                }
            } else {
                // Fallback for legacy data (if any) or first install behavior
                val name = userPreferences.rememberedName.first()
                val phone = userPreferences.rememberedPhone.first()
                val email = userPreferences.rememberedEmail.first()
                val avatar = userPreferences.rememberedAvatar.first()

                if (!name.isNullOrBlank() && !phone.isNullOrBlank()) {
                    _rememberedUser.value = RememberedUser(
                        name = name,
                        phone = phone,
                        email = email ?: "",
                        avatar = avatar,
                    )
                    _isReturningUser.value = true
                    _phone.value = phone
                }
            }
        }
    }

    /**
     * Switches from "returning user" mode to standard login.
     * Called when the user taps the "Change" button on the returning-user card.
     */
    fun changeUser() {
        _isReturningUser.value = false
        _phone.value = ""
        _password.value = ""
    }

    // ── Validation ─────────────────────────────────────────────────────────

    private fun isPhoneValid(phone: String): Boolean {
        return phone.isNotBlank() && phone.length >= 6
    }

    fun onPhoneFocusLost() {
        if (_phone.value.isBlank()) {
            _phoneError.value = "Phone is required"
        } else if (!isPhoneValid(_phone.value)) {
            _phoneError.value = "Please enter a valid phone number"
        }
    }

    fun onPasswordFocusLost() {
        if (_password.value.isEmpty()) {
            _passwordError.value = "Password cannot be empty"
        }
    }

    // ── Login action ───────────────────────────────────────────────────────

    fun login() {
        val currentPhone = if (_isReturningUser.value) {
            _rememberedUser.value?.phone?.trim() ?: _phone.value.trim()
        } else {
            _phone.value.trim()
        }
        val currentPassword = _password.value

        // Validate phone
        if (!isPhoneValid(currentPhone)) {
            if (_isReturningUser.value) {
                _uiState.value = LoginUiState.Error("Invalid remembered phone")
            } else {
                _phoneError.value = "Please enter a valid phone number"
            }
            return
        }

        // Validate password
        if (currentPassword.isEmpty()) {
            _passwordError.value = "Password cannot be empty"
            return
        }

        // Perform login via repository
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            val result = authRepository.login(LoginRequest(currentPhone, currentPassword))
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
