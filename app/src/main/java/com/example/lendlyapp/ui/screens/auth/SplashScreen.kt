package com.example.lendlyapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.theme.FigmaMintSplash
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import com.example.lendlyapp.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
private const val SPLASH_DELAY_MS = 1_500L

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val destination by viewModel.destination.collectAsState()

    LaunchedEffect(destination) {
        destination?.let { dest ->
            delay(SPLASH_DELAY_MS)
            when (dest) {
                SplashDestination.Onboarding -> onNavigateToOnboarding()
                SplashDestination.Login      -> onNavigateToLogin()
                SplashDestination.Home       -> onNavigateToHome()
            }
        }
    }

    SplashScreenContent()
}


@Composable
private fun SplashScreenContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaMintSplash),
    ) {
        LendlyLogo(
            modifier = Modifier.align(Alignment.Center),
            size = DpSize(width = 243.dp, height = 83.dp),
        )

<<<<<<< HEAD
        HomeIndicatorBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 8.dp),
            color = Color.Black,
        )
=======
        // Removed HomeIndicatorBar
>>>>>>> 8969aaf8bf2a7cdf0fc3eaa0b918595e1de561b0
    }
}


@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun SplashScreenPreview() {
    LendlyAppTheme(dynamicColor = false) {
        SplashScreenContent()
    }
}
