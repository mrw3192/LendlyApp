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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lendlyapp.ui.screens.auth.LoginScreen
import com.example.lendlyapp.ui.screens.auth.SplashScreen
import com.example.lendlyapp.ui.screens.cashin.CashInAmountScreen
import com.example.lendlyapp.ui.screens.cashin.CashInOnlineScreen
import com.example.lendlyapp.ui.screens.cashin.CashInOverTheCounterScreen
import com.example.lendlyapp.ui.screens.cashin.CashInScreen
import com.example.lendlyapp.ui.screens.cashin.SuccessfulTransactionScreen
import com.example.lendlyapp.ui.screens.history.TransactionDetailsScreen
import com.example.lendlyapp.ui.screens.onboarding.OnboardingScreen
import com.example.lendlyapp.ui.screens.register.VerifyPhoneScreen
import com.example.lendlyapp.ui.screens.register.SmsVerificationScreen
import com.example.lendlyapp.ui.screens.register.ProfileDetailScreen
import com.example.lendlyapp.ui.screens.profile.CreditScoreScreen
import com.example.lendlyapp.ui.screens.profile.EditProfileScreen
import com.example.lendlyapp.ui.screens.profile.ProfileSuccessScreen
import com.example.lendlyapp.ui.screens.register.CreatePasswordScreen
import com.example.lendlyapp.ui.screens.register.DoneScreen
import com.example.lendlyapp.ui.screens.register.FaceRecognitionScreen
import com.example.lendlyapp.ui.screens.register.IdVerificationScreen
import com.example.lendlyapp.ui.screens.register.ProfileDetailScreen
import com.example.lendlyapp.ui.screens.register.SignatureScreen
import com.example.lendlyapp.ui.screens.register.SmsVerificationScreen
import com.example.lendlyapp.ui.screens.register.VerifiedScreen
import com.example.lendlyapp.ui.screens.register.VerifyPhoneScreen
import com.example.lendlyapp.ui.screens.loans.LoanInfoScreen
import com.example.lendlyapp.ui.screens.loans.LoanFormScreen
import com.example.lendlyapp.ui.screens.loans.LoanSuccessScreen
import com.example.lendlyapp.ui.screens.loans.ActiveLoanScreen
import com.example.lendlyapp.ui.screens.loans.TransactionDetails
import com.example.lendlyapp.viewmodel.ProfileViewModel
import com.example.lendlyapp.viewmodel.RegisterViewModel
import com.example.lendlyapp.viewmodel.LoanViewModel
import com.example.lendlyapp.ui.theme.FigmaDarkBg
import com.example.lendlyapp.ui.theme.FigmaDarkText

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
    val navController = rememberNavController()

    // Shared RegisterViewModel — scoped to the navigation graph so all register
    // screens share the same instance. Using hiltViewModel() at this level.
    val registerViewModel: RegisterViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = AppDestination.Splash.route
    ) {

        // ── Splash ──────────────────────────────────────────────────────────
        composable(AppDestination.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(AppDestination.Onboarding.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        // ── Onboarding ───────────────────────────────────────────────────────
        composable(AppDestination.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppDestination.VerifyPhone.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        // ── Login ────────────────────────────────────────────────────────────
        composable(AppDestination.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppDestination.VerifyPhone.route)
                },
            )
        }

        // ── Register Step 1: Verify Phone ────────────────────────────────────
        composable(AppDestination.VerifyPhone.route) {
            VerifyPhoneScreen(
                viewModel = registerViewModel,
                onNavigateToSms = {
                    navController.navigate(AppDestination.SmsVerification.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // ── Register Step 2: SMS Verification ────────────────────────────────
        composable(AppDestination.SmsVerification.route) {
            SmsVerificationScreen(
                viewModel = registerViewModel,
                onNavigateToProfile = {
                    navController.navigate(AppDestination.ProfileDetail.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // ── Register Step 3: Profile Detail ──────────────────────────────────
        composable(AppDestination.ProfileDetail.route) {
            ProfileDetailScreen(
                viewModel = registerViewModel,
                onNavigateToPassword = {
                    navController.navigate(AppDestination.CreatePassword.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // ── Register Step 4: Create Password ─────────────────────────────────
        composable(AppDestination.CreatePassword.route) {
            CreatePasswordScreen(
                viewModel = registerViewModel,
                onNavigateToDone = {
                    navController.navigate(AppDestination.IdVerification.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // ── KYC Step 1: ID Verification ──────────────────────────────────────
        // TODO (Figma Fix): Modificamos el flujo de la pantalla de registro/login para un orden apropiado de 
        // pantallas, dejando para el final las opcionales de KYC (excepto la de done).
        composable(AppDestination.IdVerification.route) {
            IdVerificationScreen(
                onNextClick = {
                    navController.navigate(AppDestination.FaceRecognition.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // ── KYC Step 2: Face Recognition ─────────────────────────────────────
        composable(AppDestination.FaceRecognition.route) {
            FaceRecognitionScreen(
                onNextClick = {
                    navController.navigate(AppDestination.Verified.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // ── KYC Step 4: Signature ────────────────────────────────────────────
        composable(AppDestination.Signature.route) {
            SignatureScreen(
                onNextClick = {
                    navController.navigate(AppDestination.Done.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // ── KYC Step 3: Verified ─────────────────────────────────────────────
        composable(AppDestination.Verified.route) {
            VerifiedScreen(
                onNextClick = {
                    navController.navigate(AppDestination.Signature.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // ── Register Step 5: Done ────────────────────────────────────────────
        composable(AppDestination.Done.route) {
            DoneScreen(
                // TODO (Figma Fix): Hicimos que luego de crearse la cuenta se vaya a la pantalla 
                // de login en lugar de ir directamente al home.
                onNavigateToHome = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        // ── Home ─────────────────────────────────────────────────────────────
        composable(AppDestination.Home.route) {
            MainScaffold(
              navController = navController,
                onNavigateToCashIn = { navController.navigate(AppDestination.CashIn.route) },
                onNavigateToTransactionDetails = { id ->
                    navController.navigate(AppDestination.TransactionDetails.createRoute(id))
                },
                onNavigateToLoanForm = { navController.navigate(AppDestination.LoanForm.route) }
            )
        }

        // ── Loans Module ─────────────────────────────────────────────────────
        composable(AppDestination.LoanForm.route) {
            val viewModel: LoanViewModel = hiltViewModel()
            LoanFormScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    val loan = viewModel.appliedLoan.value
                    if (loan != null) {
                        navController.navigate(AppDestination.LoanSuccess.createRoute(loan)) {
                            popUpTo(AppDestination.Home.route)
                        }
                    }
                }
            )
        }

        composable(
            route = AppDestination.LoanSuccess.route,
            arguments = listOf(
                navArgument("loanId") { type = NavType.StringType; defaultValue = "" },
                navArgument("amount") { type = NavType.StringType; defaultValue = "0.0" },
                navArgument("installmentAmount") { type = NavType.StringType; defaultValue = "0.0" },
                navArgument("installmentPlan") { type = NavType.StringType; defaultValue = "" },
                navArgument("interestRate") { type = NavType.StringType; defaultValue = "0.0" },
                navArgument("status") { type = NavType.StringType; defaultValue = "" },
                navArgument("nextPaymentDate") { type = NavType.StringType; defaultValue = "" },
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val details = TransactionDetails(
                amount = "₱ ${args.getString("amount") ?: "0.0"}",
                source = "Lendly",
                monthlyFee = "₱ ${args.getString("installmentAmount") ?: "0.0"}",
                interest = "${args.getString("interestRate") ?: "0"}%",
                installmentPlan = args.getString("installmentPlan") ?: "",
                dateTime = args.getString("nextPaymentDate") ?: "",
                transactionNumber = "#${args.getString("loanId") ?: ""}"
            )
            LoanSuccessScreen(
                details = details,
                onClose = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Home.route) { inclusive = true }
                    }
                },
                onDone = {
                    navController.navigate(AppDestination.LoanActive.route) {
                        popUpTo(AppDestination.Home.route)
                    }
                }
            )
        }

        composable(AppDestination.LoanActive.route) {
            val viewModel: LoanViewModel = hiltViewModel()
            ActiveLoanScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ── Cash In ───────────────────────────────────────────────────────────
        composable(AppDestination.CashIn.route) {
            CashInScreen(
                onBack = { navController.popBackStack() },
                onNavigateToOnline = { navController.navigate(AppDestination.CashInOnline.route) },
                onNavigateToOverTheCounter = { navController.navigate(AppDestination.CashInOverTheCounter.route) },
            )
        }

        composable(AppDestination.CashInOnline.route) {
            CashInOnlineScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAmount = { bankName ->
                    navController.navigate(AppDestination.CashInAmount.createRoute(bankName))
                },
            )
        }

        composable(AppDestination.CashInOverTheCounter.route) {
            CashInOverTheCounterScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAmount = { bankName ->
                    navController.navigate(AppDestination.CashInAmount.createRoute(bankName))
                },
            )
        }

        composable(
            route = AppDestination.CashInAmount.route,
            arguments = listOf(navArgument("bankName") { type = NavType.StringType }),
        ) { backStackEntry ->
            val bankName = backStackEntry.arguments?.getString("bankName") ?: ""
            CashInAmountScreen(
                bankName = bankName,
                onBack = { navController.popBackStack() },
                onNext = { amount ->
                    navController.navigate(AppDestination.SuccessfulTransaction.createRoute(bankName, amount))
                },
            )
        }

        composable(
            route = AppDestination.TransactionDetails.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.StringType }),
        ) {
            TransactionDetailsScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = AppDestination.SuccessfulTransaction.route,
            arguments = listOf(
                navArgument("partnerName") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val partnerName = backStackEntry.arguments?.getString("partnerName") ?: ""
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            SuccessfulTransactionScreen(
                partnerName = partnerName,
                amount = amount,
                onClose = {
                    navController.navigate(AppDestination.Home.route) { popUpTo(0) { inclusive = true } }
                },
                onDone = {
                    navController.navigate(AppDestination.Home.route) { popUpTo(0) { inclusive = true } }
                },
            )
        }
        // ─── Manage Module ───────────────────────────────────────────────────
        composable(AppDestination.EditProfile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            EditProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(AppDestination.ProfileDone.route) {
                        popUpTo(AppDestination.Home.route)
                    }
                }
            )
        }

        composable(AppDestination.CreditScore.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            CreditScoreScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppDestination.ProfileDone.route) {
            ProfileSuccessScreen(
                onDone = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
