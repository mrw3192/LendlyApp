package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.ui.screens.auth.SplashDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            val token = userPreferences.authToken.firstOrNull()
            val hasSeenOnboarding = userPreferences.hasSeenOnboarding.firstOrNull() ?: false
            _destination.value = when {
                !token.isNullOrEmpty() -> SplashDestination.Home
                !hasSeenOnboarding -> SplashDestination.Onboarding
                else -> SplashDestination.Login
            }
        }
    }
}
