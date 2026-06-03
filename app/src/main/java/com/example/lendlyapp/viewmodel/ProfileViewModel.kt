package com.example.lendlyapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.model.User
import com.example.lendlyapp.profile.usecase.GetUserProfileUseCase
import com.example.lendlyapp.profile.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    // Estado principal
    private val _uiState = mutableStateOf<ProfileUiState>(ProfileUiState.Loading)
    val uiState: State<ProfileUiState> = _uiState

    // --- Estados del formulario de edición ---
    // Los mantenemos aquí para que sobrevivan a la rotación de pantalla
    var firstNameInput = mutableStateOf("")
        private set
    var lastNameInput = mutableStateOf("")
        private set
    var addressInput = mutableStateOf("")
        private set
    var cityInput = mutableStateOf("")
        private set
    var postalCodeInput = mutableStateOf("")
        private set
    var phoneInput = mutableStateOf("")
        private set

    init {
        loadProfile()
    }

    /**
     * Carga la información del usuario desde el caso de uso.
     */
    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            getUserProfileUseCase("1").onSuccess { user ->
                _uiState.value = ProfileUiState.Success(user)
                // Inicializamos los campos del formulario con los datos actuales
                initializeForm(user)
            }.onFailure {
                _uiState.value = ProfileUiState.Error("No se pudo cargar el perfil")
            }
        }
    }

    private fun initializeForm(user: User) {
        val names = user.fullName.split(" ")
        firstNameInput.value = names.firstOrNull() ?: ""
        lastNameInput.value = names.drop(1).joinToString(" ")
        addressInput.value = user.address
        phoneInput.value = user.phone
    }

    // --- Métodos para actualizar el formulario (llamados desde la UI) ---
    fun onFirstNameChange(newValue: String) { firstNameInput.value = newValue }
    fun onLastNameChange(newValue: String) { lastNameInput.value = newValue }
    fun onAddressChange(newValue: String) { addressInput.value = newValue }
    fun onCityChange(newValue: String) { cityInput.value = newValue }
    fun onPostalCodeChange(newValue: String) { postalCodeInput.value = newValue }
    fun onPhoneChange(newValue: String) { phoneInput.value = newValue }

    /**
     * Guarda los cambios llamando al caso de uso.
     */
    fun updateProfile(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success) {
            val updatedUser = currentState.user.copy(
                fullName = "${firstNameInput.value} ${lastNameInput.value}".trim(),
                address = addressInput.value,
                phone = phoneInput.value
            )

            viewModelScope.launch {
                updateUserProfileUseCase(updatedUser).onSuccess {
                    onSuccess()
                }.onFailure {
                }
            }
        }
    }
}
