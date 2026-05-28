package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.ui.screens.auth.SplashDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── ViewModel ─────────────────────────────────────────────────────────────────

/**
 * Reads [UserPreferences] once on creation and emits the correct [SplashDestination].
 *
 * The destination is null until the DataStore read completes so [SplashScreen]
 * can wait before triggering navigation.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)

    /**
     * The resolved navigation target.
     * Null = still reading preferences (DataStore IO in progress).
     */
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            val token = userPreferences.authToken.first()
            val hasSeenOnboarding = userPreferences.hasSeenOnboarding.first()

            _destination.value = when {
                !token.isNullOrEmpty() -> SplashDestination.Home
                !hasSeenOnboarding    -> SplashDestination.Onboarding
                else                  -> SplashDestination.Login
            }
        }
    }
}
