package com.example.lendlyapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── Registration Step ─────────────────────────────────────────────────────────

enum class RegisterStep {
    VERIFY_PHONE,
    SMS_VERIFICATION,
    PROFILE_DETAIL,
    CREATE_PASSWORD,
    DONE,
}

// ─── Registration State ────────────────────────────────────────────────────────

data class RegisterState(
    val countryCode: String = "+65",
    val phone: String = "",
    val otpCode: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dobDay: String = "",
    val dobMonth: String = "",
    val dobYear: String = "",
    val address: String = "",
    val city: String = "",
    val postalCode: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistrationComplete: Boolean = false,
)

// ─── ViewModel ─────────────────────────────────────────────────────────────────

/**
 * Shared ViewModel for the 5-step registration flow.
 *
 * Each screen reads/writes relevant fields in [RegisterState].
 * On final step, calls POST /auth/create (mocked) and saves auth token.
 *
 * API contract (SPEC_TECNICO §5.1):
 *   POST /auth/create { firstName, lastName, dni, email, password } → { status, message }
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    // ── Field updaters ─────────────────────────────────────────────────────

    fun onPhoneChange(value: String) { _state.update { it.copy(phone = value) } }
    fun onCountryCodeChange(value: String) { _state.update { it.copy(countryCode = value) } }
    fun onOtpChange(value: String) { _state.update { it.copy(otpCode = value) } }
    fun onFirstNameChange(value: String) { _state.update { it.copy(firstName = value) } }
    fun onLastNameChange(value: String) { _state.update { it.copy(lastName = value) } }
    fun onDobDayChange(value: String) { _state.update { it.copy(dobDay = value) } }
    fun onDobMonthChange(value: String) { _state.update { it.copy(dobMonth = value) } }
    fun onDobYearChange(value: String) { _state.update { it.copy(dobYear = value) } }
    fun onAddressChange(value: String) { _state.update { it.copy(address = value) } }
    fun onCityChange(value: String) { _state.update { it.copy(city = value) } }
    fun onPostalCodeChange(value: String) { _state.update { it.copy(postalCode = value) } }
    fun onPasswordChange(value: String) { _state.update { it.copy(password = value) } }

    fun clearError() { _state.update { it.copy(error = null) } }

    // ── Step validations ───────────────────────────────────────────────────

    fun validatePhone(): Boolean {
        val phone = _state.value.phone.trim()
        if (phone.length < 6) {
            _state.update { it.copy(error = "Please enter a valid phone number") }
            return false
        }
        return true
    }

    fun validateOtp(): Boolean {
        val otp = _state.value.otpCode.trim()
        if (otp.length < 6) {
            _state.update { it.copy(error = "Please enter all 6 digits") }
            return false
        }
        return true
    }

    fun validateProfile(): Boolean {
        val s = _state.value
        if (s.firstName.isBlank()) {
            _state.update { it.copy(error = "First name is required") }
            return false
        }
        if (s.lastName.isBlank()) {
            _state.update { it.copy(error = "Last name is required") }
            return false
        }
        if (s.dobDay.isBlank() || s.dobMonth.isBlank() || s.dobYear.isBlank()) {
            _state.update { it.copy(error = "Date of birth is required") }
            return false
        }
        if (s.address.isBlank()) {
            _state.update { it.copy(error = "Address is required") }
            return false
        }
        if (s.city.isBlank()) {
            _state.update { it.copy(error = "City is required") }
            return false
        }
        if (s.postalCode.isBlank()) {
            _state.update { it.copy(error = "Postal code is required") }
            return false
        }
        if (s.phone.isBlank()) {
            _state.update { it.copy(error = "Phone number is required") }
            return false
        }
        return true
    }

    fun validatePassword(): Boolean {
        val pwd = _state.value.password
        if (pwd.length < 9) {
            _state.update { it.copy(error = "Password must be at least 9 characters") }
            return false
        }
        if (!pwd.any { it.isLetter() } || !pwd.any { it.isDigit() }) {
            _state.update { it.copy(error = "Password must contain a letter and a number") }
            return false
        }
        return true
    }

    // ── Send OTP (mocked) ──────────────────────────────────────────────────

    fun sendOtp() {
        if (!validatePhone()) return
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(600) // Simulate network call
            _state.update { it.copy(isLoading = false) }
        }
    }

    // ── Complete Registration (mocked) ─────────────────────────────────────

    fun completeRegistration() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(800) // Simulate POST /auth/create
            val fakeToken = "mock_register_token_${System.currentTimeMillis()}"
            userPreferences.saveAuthToken(fakeToken)
            _state.update { it.copy(isLoading = false, isRegistrationComplete = true) }
        }
    }
}
