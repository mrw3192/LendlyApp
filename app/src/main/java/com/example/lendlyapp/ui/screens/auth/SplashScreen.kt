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
import com.example.lendlyapp.shared.HomeIndicatorBar
import com.example.lendlyapp.shared.LendlyLogo
import com.example.lendlyapp.theme.FigmaMintSplash
import com.example.lendlyapp.theme.LendlyAppTheme
import com.example.lendlyapp.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

// ─── Splash Screen ─────────────────────────────────────────────────────────────
// Figma node: 35:1097 "Splash Screen" — 393×852dp
// Background: #E5F5EA (FigmaMintSplash)
// Layout: centered logo + home indicator at bottom
//
// Navigation:
//   LaunchedEffect collects SplashDestination from SplashViewModel.
//   Once destination is resolved a 1 500 ms delay gives the logo time to render
//   before the transition fires. This avoids a flash of unstyled content.

private const val SPLASH_DELAY_MS = 1_500L

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val destination by viewModel.destination.collectAsState()

    // Auto-navigate once destination is resolved
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

// ─── Stateless UI ──────────────────────────────────────────────────────────────

@Composable
private fun SplashScreenContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaMintSplash),
    ) {
        // ── Centered Lendly Logo ──────────────────────────────────────────────
        // Figma Frame 134: pos=(+75,+389) size=242.8×83.3dp on 393×852 screen
        // The logo is centered both horizontally and vertically.
        LendlyLogo(
            modifier = Modifier.align(Alignment.Center),
            size = DpSize(width = 243.dp, height = 83.dp),
        )

        // ── Home Indicator ───────────────────────────────────────────────────
        // Figma: pos=(+0,+817) 393×34dp — black pill bar centered at bottom
        HomeIndicatorBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 8.dp),
            color = Color.Black,
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun SplashScreenPreview() {
    LendlyAppTheme(dynamicColor = false) {
        SplashScreenContent()
    }
}
