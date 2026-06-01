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

/** Loan Module — Information and conditions screen. */
@Serializable data object LoanInfoRoute : NavKey

/** Loan Module — Request form screen. */
@Serializable data object LoanFormRoute : NavKey

/** Loan Module — Active loan management screen. */
@Serializable data object ActiveLoanRoute : NavKey

/** Loan Module — Successful transaction feedback. */
@Serializable data object LoanSuccessRoute : NavKey

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

// ─── Legacy key — kept to avoid breaking MainScreen during development ─────────
@Serializable data object Main : NavKey
