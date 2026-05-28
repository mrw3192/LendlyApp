package com.example.lendlyapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.lendlyapp.HomeRoute
import com.example.lendlyapp.LoginRoute
import com.example.lendlyapp.OnboardingRoute
import com.example.lendlyapp.RegisterRoute
import com.example.lendlyapp.SplashRoute
import com.example.lendlyapp.presentation.auth.SplashScreen
import com.example.lendlyapp.presentation.onboarding.OnboardingScreen
import com.example.lendlyapp.theme.FigmaDarkBg
import com.example.lendlyapp.theme.FigmaDarkText

/**
 * Root navigation graph for LendlyApp.
 *
 * Start destination: [SplashRoute]
 * Navigation flow (SPEC_TECNICO §4):
 *   Splash ──► Onboarding ──► Login / Register ──► Home
 *           └──────────────────────────────────────────►
 *
 * Back-stack policy: all navigations from Splash / Onboarding clear the
 * entire back-stack before pushing the new destination, so the user can
 * never press Back to return to those screens.
 */
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(SplashRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            // ── Splash ──────────────────────────────────────────────────────────
            entry<SplashRoute> {
                SplashScreen(
                    onNavigateToOnboarding = {
                        navigateClearingStack(backStack, OnboardingRoute)
                    },
                    onNavigateToLogin = {
                        navigateClearingStack(backStack, LoginRoute)
                    },
                    onNavigateToHome = {
                        navigateClearingStack(backStack, HomeRoute)
                    },
                )
            }

            // ── Onboarding ───────────────────────────────────────────────────────
            entry<OnboardingRoute> {
                OnboardingScreen(
                    onNavigateToLogin = {
                        navigateClearingStack(backStack, LoginRoute)
                    },
                    onNavigateToRegister = {
                        navigateClearingStack(backStack, RegisterRoute)
                    },
                )
            }

            // ── Login ────────────────────────────────────────────────────────────
            entry<LoginRoute> {
                // TODO: Replace with LoginScreen composable once implemented.
                PlaceholderScreen("Login Screen")
            }

            // ── Register ─────────────────────────────────────────────────────────
            entry<RegisterRoute> {
                // TODO: Replace with RegisterScreen composable once implemented.
                PlaceholderScreen("Register Screen")
            }

            // ── Home ─────────────────────────────────────────────────────────────
            entry<HomeRoute> {
                // TODO: Replace with HomeScreen composable once implemented.
                MainScaffold()
            }
        },
    )
}

// ─── Helpers ───────────────────────────────────────────────────────────────────

/**
 * Replaces the entire back-stack with [destination], matching `popUpTo(inclusive=true)` behaviour.
 *
 * Strategy: add the destination first (so NavDisplay never sees an empty list),
 * then remove all previous entries.
 */
private fun navigateClearingStack(
    backStack: MutableList<androidx.navigation3.runtime.NavKey>,
    destination: androidx.navigation3.runtime.NavKey,
) {
    val existing = backStack.toList()
    backStack.add(destination)
    backStack.removeAll(existing.toSet())
}

/** Temporary screen shown for routes not yet implemented. */
@Composable
private fun PlaceholderScreen(label: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaDarkBg),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = FigmaDarkText)
    }
}
