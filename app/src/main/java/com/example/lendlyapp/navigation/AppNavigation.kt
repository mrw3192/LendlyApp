package com.example.lendlyapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.lendlyapp.ui.screens.auth.LoginScreen
import com.example.lendlyapp.ui.screens.auth.SplashScreen
import com.example.lendlyapp.ui.screens.onboarding.OnboardingScreen
import com.example.lendlyapp.ui.screens.register.VerifyPhoneScreen
import com.example.lendlyapp.ui.screens.register.SmsVerificationScreen
import com.example.lendlyapp.ui.screens.register.ProfileDetailScreen
import com.example.lendlyapp.ui.screens.register.CreatePasswordScreen
import com.example.lendlyapp.ui.screens.register.DoneScreen
import com.example.lendlyapp.viewmodel.RegisterViewModel
import com.example.lendlyapp.ui.theme.FigmaDarkBg
import com.example.lendlyapp.ui.theme.FigmaDarkText
import com.example.lendlyapp.ui.screens.auth.SplashScreen
import com.example.lendlyapp.ui.screens.onboarding.OnboardingScreen
import com.example.lendlyapp.ui.screens.loans.LoanInfoScreen
import com.example.lendlyapp.ui.screens.loans.LoanFormScreen
import com.example.lendlyapp.viewmodel.LoanViewModel
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Root navigation graph for LendlyApp.
 *
 * Start destination: [SplashRoute]
 * Navigation flow (SPEC_TECNICO §4):
 *   Splash ──► Onboarding ──► Login / Register ──► Home
 *           └──────────────────────────────────────────►
 *
 * Registration flow:
 *   VerifyPhone → SmsVerification → ProfileDetail → CreatePassword → Done → Home
 *
 * Back-stack policy: all navigations from Splash / Onboarding clear the
 * entire back-stack before pushing the new destination, so the user can
 * never press Back to return to those screens.
 */
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(SplashRoute)

    // Shared RegisterViewModel — scoped to the navigation graph so all register
    // screens share the same instance. Using hiltViewModel() at this level.
    val registerViewModel: RegisterViewModel = hiltViewModel()

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
                        navigateClearingStack(backStack, VerifyPhoneRoute)
                    },
                )
            }

            // ── Login ────────────────────────────────────────────────────────────
            entry<LoginRoute> {
                LoginScreen(
                    onNavigateToHome = {
                        navigateClearingStack(backStack, HomeRoute)
                    },
                    onNavigateToRegister = {
                        backStack.add(VerifyPhoneRoute)
                    },
                )
            }

            // ── Register Step 1: Verify Phone ────────────────────────────────────
            entry<VerifyPhoneRoute> {
                VerifyPhoneScreen(
                    viewModel = registerViewModel,
                    onNavigateToSms = {
                        backStack.add(SmsVerificationRoute)
                    },
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                )
            }

            // ── Register Step 2: SMS Verification ────────────────────────────────
            entry<SmsVerificationRoute> {
                SmsVerificationScreen(
                    viewModel = registerViewModel,
                    onNavigateToProfile = {
                        backStack.add(ProfileDetailRoute)
                    },
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                )
            }

            // ── Register Step 3: Profile Detail ──────────────────────────────────
            entry<ProfileDetailRoute> {
                ProfileDetailScreen(
                    viewModel = registerViewModel,
                    onNavigateToPassword = {
                        backStack.add(CreatePasswordRoute)
                    },
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                )
            }

            // ── Register Step 4: Create Password ─────────────────────────────────
            entry<CreatePasswordRoute> {
                CreatePasswordScreen(
                    viewModel = registerViewModel,
                    onNavigateToDone = {
                        backStack.add(DoneRoute)
                    },
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                )
            }

            // ── Register Step 5: Done ────────────────────────────────────────────
            entry<DoneRoute> {
                DoneScreen(
                    onNavigateToHome = {
                        navigateClearingStack(backStack, HomeRoute)
                    },
                )
            }

            // ── Home ─────────────────────────────────────────────────────────────
            entry<HomeRoute> {
                // TODO: Replace with HomeScreen composable once implemented.
                MainScaffold()
            }

            // ── Loans ─────────────────────────────────────────────────────────────
            entry<LoanInfoRoute> {
                LoanInfoScreen(
                    onNavigateToForm = {
                        backStack.add(LoanFormRoute)
                    }
                )
            }

            entry<LoanFormRoute> {
                val viewModel: LoanViewModel = hiltViewModel()
                LoanFormScreen(
                    viewModel = viewModel,
                    onBack = { backStack.removeLastOrNull() },
                    onSuccess = {
                        // Navegar a éxito (limpiando el stack del formulario)
                        backStack.removeLastOrNull()
                        backStack.add(LoanSuccessRoute)
                    }
                )
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
