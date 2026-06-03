package com.example.lendlyapp.ui.screens.auth

sealed class SplashDestination {
    data object Home : SplashDestination()
    data object Onboarding : SplashDestination()
    data object Login : SplashDestination()
}
