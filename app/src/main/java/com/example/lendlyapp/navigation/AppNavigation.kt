package com.example.lendlyapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lendlyapp.ui.screens.auth.SplashScreen
import com.example.lendlyapp.ui.screens.cashin.CashInAmountScreen
import com.example.lendlyapp.ui.screens.cashin.CashInOnlineScreen
import com.example.lendlyapp.ui.screens.cashin.CashInOverTheCounterScreen
import com.example.lendlyapp.ui.screens.cashin.CashInScreen
import com.example.lendlyapp.ui.screens.cashin.SuccessfulTransactionScreen
import com.example.lendlyapp.ui.screens.onboarding.OnboardingScreen
import com.example.lendlyapp.ui.theme.FigmaDarkBg
import com.example.lendlyapp.ui.theme.FigmaDarkText

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppDestination.Splash.route) {

        composable(AppDestination.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = { navController.navigateSingle(AppDestination.Onboarding.route) },
                onNavigateToLogin      = { navController.navigateSingle(AppDestination.Login.route) },
                onNavigateToHome       = { navController.navigateSingle(AppDestination.Home.route) },
            )
        }

        composable(AppDestination.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin    = { navController.navigateSingle(AppDestination.Login.route) },
                onNavigateToRegister = { navController.navigateSingle(AppDestination.Register.route) },
            )
        }

        composable(AppDestination.Login.route) {
            PlaceholderScreen("Login Screen")
        }

        composable(AppDestination.Register.route) {
            PlaceholderScreen("Register Screen")
        }

        composable(AppDestination.Home.route) {
            MainScaffold(
                onNavigateToCashIn = { navController.navigate(AppDestination.CashIn.route) },
            )
        }

        composable(AppDestination.CashIn.route) {
            CashInScreen(
                onBack                     = { navController.popBackStack() },
                onNavigateToOnline         = { navController.navigate(AppDestination.CashInOnline.route) },
                onNavigateToOverTheCounter = { navController.navigate(AppDestination.CashInOverTheCounter.route) },
            )
        }

        composable(AppDestination.CashInOnline.route) {
            CashInOnlineScreen(
                onBack             = { navController.popBackStack() },
                onNavigateToAmount = { bankName -> navController.navigate(AppDestination.CashInAmount.createRoute(bankName)) },
            )
        }

        composable(AppDestination.CashInOverTheCounter.route) {
            CashInOverTheCounterScreen(
                onBack             = { navController.popBackStack() },
                onNavigateToAmount = { partnerName -> navController.navigate(AppDestination.CashInAmount.createRoute(partnerName)) },
            )
        }

        composable(
            route = AppDestination.CashInAmount.route,
            arguments = listOf(navArgument("bankName") { type = NavType.StringType }),
        ) { backStackEntry ->
            val bankName = backStackEntry.arguments?.getString("bankName") ?: ""
            CashInAmountScreen(
                bankName = bankName,
                onBack   = { navController.popBackStack() },
                onNext   = { amount -> navController.navigate(AppDestination.SuccessfulTransaction.createRoute(bankName, amount)) },
            )
        }

        composable(
            route = AppDestination.SuccessfulTransaction.route,
            arguments = listOf(
                navArgument("partnerName") { type = NavType.StringType },
                navArgument("amount")      { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val partnerName = backStackEntry.arguments?.getString("partnerName") ?: ""
            val amount      = backStackEntry.arguments?.getString("amount") ?: ""
            SuccessfulTransactionScreen(
                partnerName = partnerName,
                amount      = amount,
                onClose = { navController.navigateSingle(AppDestination.Home.route) },
                onDone  = { navController.navigateSingle(AppDestination.Home.route) },
            )
        }
    }
}

private fun NavController.navigateSingle(route: String) {
    navigate(route) { popUpTo(0) { inclusive = true } }
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
