package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.ui.screens.auth.SplashDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        val token = userPreferences.authToken
        val hasSeenOnboarding = userPreferences.hasSeenOnboarding

        _destination.value = when {
            !token.isNullOrEmpty() -> SplashDestination.Home
            !hasSeenOnboarding    -> SplashDestination.Onboarding
            else                  -> SplashDestination.Login
        }
    }
}
