package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.auth.AuthRepository
import com.example.lendlyapp.model.RegisterRequest
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
    
    // Inline Errors
    val phoneError: String? = null,
    val otpError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val dobError: String? = null,
    val addressError: String? = null,
    val cityError: String? = null,
    val postalCodeError: String? = null,
    val passwordError: String? = null,
)

// ─── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val nameRegex = Regex("^[a-zA-ZÀ-ÿ \\-']*$")

    // ── Field updaters ─────────────────────────────────────────────────────

    fun onPhoneChange(value: String) { 
        if (value.all { it.isDigit() }) {
            _state.update { it.copy(phone = value, phoneError = null, error = null) } 
        }
    }
    fun onCountryCodeChange(value: String) { _state.update { it.copy(countryCode = value) } }
    fun onOtpChange(value: String) { _state.update { it.copy(otpCode = value, otpError = null, error = null) } }
    
    fun onFirstNameChange(value: String) { 
        if (value.length <= 50 && value.matches(nameRegex)) {
            _state.update { it.copy(firstName = value, firstNameError = null, error = null) } 
        }
    }
    fun onLastNameChange(value: String) { 
        if (value.length <= 50 && value.matches(nameRegex)) {
            _state.update { it.copy(lastName = value, lastNameError = null, error = null) } 
        }
    }
    fun onDobDayChange(value: String) { _state.update { it.copy(dobDay = value.filter { char -> char.isDigit() }.take(2), dobError = null, error = null) } }
    fun onDobMonthChange(value: String) { _state.update { it.copy(dobMonth = value.filter { char -> char.isDigit() }.take(2), dobError = null, error = null) } }
    fun onDobYearChange(value: String) { _state.update { it.copy(dobYear = value.filter { char -> char.isDigit() }.take(4), dobError = null, error = null) } }
    
    fun onAddressChange(value: String) { 
        if (value.length <= 100) _state.update { it.copy(address = value, addressError = null, error = null) } 
    }
    fun onCityChange(value: String) { 
        if (value.length <= 50) _state.update { it.copy(city = value, cityError = null, error = null) } 
    }
    fun onPostalCodeChange(value: String) { 
        if (value.length <= 10) _state.update { it.copy(postalCode = value, postalCodeError = null, error = null) } 
    }
    fun onPasswordChange(value: String) { 
        if (value.length <= 64) _state.update { it.copy(password = value, passwordError = null, error = null) } 
    }

    fun clearError() { _state.update { it.copy(error = null) } }

    // ── Focus Lost Validations ─────────────────────────────────────────────

    fun onPhoneFocusLost() { validatePhone() }
    fun onOtpFocusLost() { validateOtp() }
    
    fun onFirstNameFocusLost() {
        if (_state.value.firstName.isBlank()) {
            _state.update { it.copy(firstNameError = "First name is required") }
        }
    }
    
    fun onLastNameFocusLost() {
        if (_state.value.lastName.isBlank()) {
            _state.update { it.copy(lastNameError = "Last name is required") }
        }
    }
    
    fun onDobFocusLost() {
        val s = _state.value
        val day = s.dobDay.toIntOrNull() ?: 0
        val month = s.dobMonth.toIntOrNull() ?: 0
        val year = s.dobYear.toIntOrNull() ?: 0
        
        if (s.dobDay.isBlank() || s.dobMonth.isBlank() || s.dobYear.isBlank()) {
            _state.update { it.copy(dobError = "Date of birth is required") }
        } else if (day !in 1..31) {
            _state.update { it.copy(dobError = "Day must be between 1 and 31") }
        } else if (month !in 1..12) {
            _state.update { it.copy(dobError = "Month must be between 1 and 12") }
        } else if (year !in 1901..2026) {
            _state.update { it.copy(dobError = "Year must be between 1901 and 2026") }
        } else {
            _state.update { it.copy(dobError = null) }
        }
    }
    
    fun onAddressFocusLost() {
        if (_state.value.address.isBlank()) {
            _state.update { it.copy(addressError = "Address is required") }
        }
    }
    
    fun onCityFocusLost() {
        if (_state.value.city.isBlank()) {
            _state.update { it.copy(cityError = "City is required") }
        }
    }
    
    fun onPostalCodeFocusLost() {
        if (_state.value.postalCode.isBlank()) {
            _state.update { it.copy(postalCodeError = "Postal code is required") }
        }
    }
    
    fun onPasswordFocusLost() { validatePassword() }

    // ── Step validations ───────────────────────────────────────────────────

    fun validatePhone(): Boolean {
        val phone = _state.value.phone.trim()
        if (phone.length < 6) {
            _state.update { it.copy(phoneError = "Please enter a valid phone number") }
            return false
        }
        return true
    }

    fun validateOtp(): Boolean {
        val otp = _state.value.otpCode.trim()
        if (otp.length < 6) {
            _state.update { it.copy(otpError = "Please enter all 6 digits") }
            return false
        }
        return true
    }

    fun validateProfile(): Boolean {
        onFirstNameFocusLost()
        onLastNameFocusLost()
        onDobFocusLost()
        onAddressFocusLost()
        onCityFocusLost()
        onPostalCodeFocusLost()
        onPhoneFocusLost()
        
        val s = _state.value
        return s.firstNameError == null && s.lastNameError == null && 
               s.dobError == null && s.addressError == null && 
               s.cityError == null && s.postalCodeError == null && 
               s.phoneError == null
    }

    fun validatePassword(): Boolean {
        val pwd = _state.value.password
        if (pwd.length < 9) {
            _state.update { it.copy(passwordError = "Password must be at least 9 characters") }
            return false
        }
        if (!pwd.any { it.isLetter() } || !pwd.any { it.isDigit() }) {
            _state.update { it.copy(passwordError = "Password must contain a letter and a number") }
            return false
        }
        _state.update { it.copy(passwordError = null) }
        return true
    }

    // ── Send OTP (mocked) ──────────────────────────────────────────────────

    fun sendOtp() {
        if (!validatePhone()) return
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(600) // Keep simulated network call for OTP (Not mapped in API yet)
            _state.update { it.copy(isLoading = false) }
        }
    }

    // ── Complete Registration ─────────────────────────────────────

    fun completeRegistration() {
        if (!validatePassword()) return
        
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val currentState = _state.value
            val request = RegisterRequest(
                firstName = currentState.firstName,
                lastName = currentState.lastName,
                dob = "${currentState.dobYear}-${currentState.dobMonth}-${currentState.dobDay}",
                address = currentState.address,
                city = currentState.city,
                postalCode = currentState.postalCode,
                phone = currentState.countryCode + currentState.phone,
                password = currentState.password
            )
            
            val result = authRepository.register(request)
            result.onSuccess {
                _state.update { it.copy(isLoading = false, isRegistrationComplete = true) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message ?: "Failed to register") }
            }
        }
    }
}
