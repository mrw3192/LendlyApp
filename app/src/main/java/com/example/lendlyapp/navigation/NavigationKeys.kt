package com.example.lendlyapp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// ─── App-wide Navigation Route Keys ───────────────────────────────────────────
// Each object is a serializable NavKey for Navigation 3 (androidx.navigation3).
// Full navigation graph: AppNavigation.kt → SPEC_TECNICO §4

/** Splash/branding screen — validates session and redirects. */
@Serializable data object SplashRoute : NavKey

/** First-launch onboarding flow (3 pages via HorizontalPager). */
@Serializable data object OnboardingRoute : NavKey

/** Login screen — email + password authentication. */
@Serializable data object LoginRoute : NavKey

/** Registration screen — create a new account. */
@Serializable data object RegisterRoute : NavKey

/** Home / Dashboard screen — post-auth entry point. */
@Serializable data object HomeRoute : NavKey

// ─── Legacy key — kept to avoid breaking MainScreen during development ─────────
@Serializable data object Main : NavKey
