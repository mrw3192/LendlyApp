package com.example.lendlyapp.navigation

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
import com.example.lendlyapp.ui.theme.FigmaDarkBg
import com.example.lendlyapp.ui.theme.FigmaDarkText
import com.example.lendlyapp.ui.screens.auth.SplashScreen
import com.example.lendlyapp.ui.screens.cashin.CashInAmountScreen
import com.example.lendlyapp.ui.screens.cashin.CashInOnlineScreen
import com.example.lendlyapp.ui.screens.cashin.CashInOverTheCounterScreen
import com.example.lendlyapp.ui.screens.cashin.CashInScreen
import com.example.lendlyapp.ui.screens.cashin.SuccessfulTransactionScreen
import com.example.lendlyapp.ui.screens.onboarding.OnboardingScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(HomeRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

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

            entry<LoginRoute> {
                // TODO: Replace with LoginScreen composable once implemented.
                PlaceholderScreen("Login Screen")
            }

            entry<RegisterRoute> {
                // TODO: Replace with RegisterScreen composable once implemented.
                PlaceholderScreen("Register Screen")
            }

            entry<HomeRoute> {
                MainScaffold(
                    onNavigateToCashIn = { backStack.add(CashInRoute) },
                )
            }

            entry<CashInRoute> {
                CashInScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNavigateToOnline = { backStack.add(CashInOnlineRoute) },
                    onNavigateToOverTheCounter = { backStack.add(CashInOverTheCounterRoute) },
                )
            }

            entry<CashInOnlineRoute> {
                CashInOnlineScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNavigateToAmount = { bankName -> backStack.add(CashInAmountRoute(bankName)) },
                )
            }

            entry<CashInOverTheCounterRoute> {
                CashInOverTheCounterScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNavigateToAmount = { partnerName -> backStack.add(CashInAmountRoute(partnerName)) },
                )
            }

            entry<CashInAmountRoute> { route ->
                CashInAmountScreen(
                    bankName = route.bankName,
                    onBack = { backStack.removeLastOrNull() },
                    onNext = { amount -> backStack.add(SuccessfulTransactionRoute(route.bankName, amount)) },
                )
            }

            entry<SuccessfulTransactionRoute> { route ->
                SuccessfulTransactionScreen(
                    partnerName = route.partnerName,
                    amount = route.amount,
                    onClose = { navigateClearingStack(backStack, HomeRoute) },
                    onDone = { navigateClearingStack(backStack, HomeRoute) },
                )
            }
        },
    )
}

private fun navigateClearingStack(
    backStack: MutableList<androidx.navigation3.runtime.NavKey>,
    destination: androidx.navigation3.runtime.NavKey,
) {
    backStack.add(destination)
    backStack.subList(0, backStack.size - 1).clear()
}
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
