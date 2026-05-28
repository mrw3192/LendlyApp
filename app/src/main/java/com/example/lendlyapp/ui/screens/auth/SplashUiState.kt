package com.example.lendlyapp.ui.screens.auth

/**
 * The three possible exits from the Splash screen (SPEC_TECNICO §4):
 *
 *  [Home]       → token is valid (user is authenticated)
 *  [Onboarding] → first app launch (has_seen_onboarding = false)
 *  [Login]      → returning user without an active session
 */
sealed class SplashDestination {
    data object Home : SplashDestination()
    data object Onboarding : SplashDestination()
    data object Login : SplashDestination()
}
