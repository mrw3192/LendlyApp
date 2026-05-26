package com.example.lendlyapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.network.model.RegisterRequest
import com.example.lendlyapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _dni = MutableStateFlow("")
    val dni: StateFlow<String> = _dni.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess.asStateFlow()

    fun onFirstNameChange(name: String) { _firstName.value = name; _error.value = null }
    fun onLastNameChange(lastName: String) { _lastName.value = lastName; _error.value = null }
    fun onEmailChange(email: String) { _email.value = email; _error.value = null }
    fun onPasswordChange(password: String) { _password.value = password; _error.value = null }
    fun onDniChange(dni: String) { _dni.value = dni; _error.value = null }

    fun register() {
        if (_firstName.value.isBlank() || _lastName.value.isBlank() || 
            _email.value.isBlank() || _password.value.isBlank() || _dni.value.isBlank()) {
            _error.value = "Por favor completa todos los campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val request = RegisterRequest(
                firstName = _firstName.value,
                lastName = _lastName.value,
                email = _email.value,
                password = _password.value,
                dni = _dni.value
            )
            
            val result = authRepository.register(request)

            result.fold(
                onSuccess = {
                    _registerSuccess.value = true
                },
                onFailure = {
                    _error.value = it.message ?: "Error al registrar la cuenta"
                }
            )

            _isLoading.value = false
        }
    }
}
