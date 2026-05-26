package com.example.lendlyapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.network.model.LoginRequest
import com.example.lendlyapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _error.value = null
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _error.value = null
    }

    fun login() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Por favor completa todos los campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val request = LoginRequest(email = _email.value, password = _password.value)
            val result = authRepository.login(request)

            result.fold(
                onSuccess = {
                    _loginSuccess.value = true
                },
                onFailure = {
                    _error.value = it.message ?: "Error al iniciar sesión"
                }
            )

            _isLoading.value = false
        }
    }
}
