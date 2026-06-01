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

/** Registration step 1 — verify phone number. */
@Serializable data object VerifyPhoneRoute : NavKey

/** Registration step 2 — SMS OTP verification. */
@Serializable data object SmsVerificationRoute : NavKey

/** Registration step 3 — personal details form. */
@Serializable data object ProfileDetailRoute : NavKey

/** Registration step 4 — create password. */
@Serializable data object CreatePasswordRoute : NavKey

/** Registration step 5 — success / done page. */
@Serializable data object DoneRoute : NavKey

// ── KYC / Optional Verification Routes ─────────────────────────────────────────

/** KYC step 1 — Scan ID document. */
@Serializable data object IdVerificationRoute : NavKey

/** KYC step 2 — Face recognition selfie. */
@Serializable data object FaceRecognitionRoute : NavKey

/** KYC step 3 — Interactive signature canvas. */
@Serializable data object SignatureRoute : NavKey

/** KYC step 4 — Identity verified success page. */
@Serializable data object VerifiedRoute : NavKey

// ─── Legacy key — kept to avoid breaking MainScreen during development ─────────
@Serializable data object Main : NavKey
