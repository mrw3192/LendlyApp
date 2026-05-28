package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [OnboardingScreen].
 *
 * Responsibility: persist [UserPreferences.HAS_SEEN_ONBOARDING] = true before the
 * user navigates away from the onboarding flow so the flow is never shown again.
 *
 * The DataStore write is a fire-and-forget launched on [viewModelScope]; navigation
 * is not gated on the write completing so the UX feels instant.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    /**
     * Call this when the user exits the onboarding flow (via "Log In" or "Sign up for free").
     * Persists the flag asynchronously so Splash will skip onboarding on next launch.
     */
    fun onNavigateAway() {
        viewModelScope.launch {
            userPreferences.setHasSeenOnboarding(seen = true)
        }
    }
}
