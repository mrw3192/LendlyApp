package com.example.lendlyapp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object SplashRoute : NavKey

@Serializable data object OnboardingRoute : NavKey

@Serializable data object LoginRoute : NavKey

@Serializable data object RegisterRoute : NavKey

@Serializable data object HomeRoute : NavKey

@Serializable data object CashInRoute : NavKey

@Serializable data object Main : NavKey
