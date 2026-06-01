package com.example.lendlyapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.lendlyapp.ui.screens.loans.LoanInfoScreen
import com.example.lendlyapp.ui.screens.loans.LoanFormScreen
import com.example.lendlyapp.ui.screens.loans.LoanSuccessScreen
import com.example.lendlyapp.ui.screens.loans.ActiveLoanScreen
import com.example.lendlyapp.viewmodel.RegisterViewModel
import com.example.lendlyapp.viewmodel.LoanViewModel
import com.example.lendlyapp.ui.theme.FigmaDarkBg
import com.example.lendlyapp.ui.theme.FigmaDarkText

/**
 * Root navigation graph for LendlyApp.
 */
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(SplashRoute)

    // Shared RegisterViewModel — scoped to the navigation graph.
    val registerViewModel: RegisterViewModel = hiltViewModel()

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            // ── Splash ──────────────────────────────────────────────────────────
            entry<SplashRoute> {
                SplashScreen(
                    onNavigateToOnboarding = { navigateClearingStack(backStack, OnboardingRoute) },
                    onNavigateToLogin = { navigateClearingStack(backStack, LoginRoute) },
                    onNavigateToHome = { navigateClearingStack(backStack, HomeRoute) },
                )
            }

            // ── Onboarding ───────────────────────────────────────────────────────
            entry<OnboardingRoute> {
                OnboardingScreen(
                    onNavigateToLogin = { navigateClearingStack(backStack, LoginRoute) },
                    onNavigateToRegister = { navigateClearingStack(backStack, VerifyPhoneRoute) },
                )
            }

            // ── Login ────────────────────────────────────────────────────────────
            entry<LoginRoute> {
                LoginScreen(
                    onNavigateToHome = { navigateClearingStack(backStack, HomeRoute) },
                    onNavigateToRegister = { backStack.add(VerifyPhoneRoute) },
                )
            }

            // ── Register Flow ───────────────────────────────────────────────────
            entry<VerifyPhoneRoute> {
                VerifyPhoneScreen(
                    viewModel = registerViewModel,
                    onNavigateToSms = { backStack.add(SmsVerificationRoute) },
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

            entry<SmsVerificationRoute> {
                SmsVerificationScreen(
                    viewModel = registerViewModel,
                    onNavigateToProfile = { backStack.add(ProfileDetailRoute) },
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

            entry<ProfileDetailRoute> {
                ProfileDetailScreen(
                    viewModel = registerViewModel,
                    onNavigateToPassword = { backStack.add(CreatePasswordRoute) },
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

            entry<CreatePasswordRoute> {
                CreatePasswordScreen(
                    viewModel = registerViewModel,
                    onNavigateToDone = { backStack.add(DoneRoute) },
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

            entry<DoneRoute> {
                DoneScreen(
                    onNavigateToHome = { navigateClearingStack(backStack, HomeRoute) },
                )
            }

            // ── Home / Main App ──────────────────────────────────────────────────
            entry<HomeRoute> {
                MainScaffold(
                    onNavigateToLoanForm = { backStack.add(LoanFormRoute) }
                )
            }

            // ── Loans ─────────────────────────────────────────────────────────────
            entry<LoanFormRoute> {
                val viewModel: LoanViewModel = hiltViewModel()
                LoanFormScreen(
                    viewModel = viewModel,
                    onBack = { backStack.removeLastOrNull() },
                    onSuccess = {
                        backStack.removeLastOrNull()
                        backStack.add(LoanSuccessRoute)
                    }
                )
            }

            // ── Loan Success ─────────────────────────────────────────────────────
            entry<LoanSuccessRoute> {
                LoanSuccessScreen(
                    onClose = { navigateClearingStack(backStack, HomeRoute) },
                    onDone = { navigateClearingStack(backStack, HomeRoute) }
                )
            }

            // ── Active Loans ─────────────────────────────────────────────────────
            entry<ActiveLoanRoute> {
                ActiveLoanScreen(
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        },
    )
}

private fun navigateClearingStack(
    backStack: MutableList<androidx.navigation3.runtime.NavKey>,
    destination: androidx.navigation3.runtime.NavKey,
) {
    val existing = backStack.toList()
    backStack.add(destination)
    backStack.removeAll(existing.toSet())
}

@Composable
private fun PlaceholderScreen(label: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(FigmaDarkBg),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = FigmaDarkText)
    }
}
