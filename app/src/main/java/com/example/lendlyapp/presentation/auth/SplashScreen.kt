package com.example.lendlyapp.presentation.auth

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.lendlyapp.theme.FigmaMintSplash
import com.example.lendlyapp.theme.FigmaNeonGreen
import com.example.lendlyapp.theme.LendlyAppTheme
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

// ─── Lendly Logo Composable ───────────────────────────────────────────────────
//
// Loads `logo_lendly.svg` from assets via Coil + SvgDecoder.
// The SVG was exported from Figma and already contains the exact design colors:
//   - Front rect : #7BF179 (FigmaNeonGreen), opacity 1.0
//   - Mid rect   : #7BF179, opacity 0.34
//   - Back rect  : #163300 (LogoShadow), opacity 1.0
//
// `size` scales the SVG proportionally:
//   - Splash screen  : 243×83dp  (Figma "Frame 134")
//   - Onboarding bar : 116×40dp

@Composable
fun LendlyLogo(
    modifier: Modifier = Modifier,
    // Default = splash size (matches Figma exactly). Pass 116.dp×40.dp for onboarding header.
    size: DpSize = DpSize(width = 243.dp, height = 83.dp),
    // Unused — kept for API compatibility; colors come from the SVG file itself.
    @Suppress("UNUSED_PARAMETER") color: Color = FigmaNeonGreen,
) {
    val context = LocalContext.current

    // Build a Coil ImageLoader that understands SVG files.
    val svgImageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(Uri.parse("file:///android_asset/logo_lendly.svg"))
            .build(),
        imageLoader = svgImageLoader,
        contentDescription = "Lendly",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size),
    )
}

// ─── Home Indicator Bar ────────────────────────────────────────────────────────
// Figma: INSTANCE 'Home Indicator' — 393×34dp container with a centered pill bar.
// Color:
//   Splash     → Color.Black  (on light FigmaMintSplash background)
//   Onboarding → Color.White  (on dark FigmaDarkForest background)

@Composable
fun HomeIndicatorBar(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
) {
    Box(
        modifier = modifier
            .size(width = 393.dp, height = 34.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = 134.dp, height = 5.dp)
                .background(color = color, shape = RoundedCornerShape(100.dp)),
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

@Preview(showBackground = true, backgroundColor = 0xFF002203)
@Composable
private fun LendlyLogoSmallPreview() {
    LendlyLogo(
        size = DpSize(116.dp, 40.dp),
    )
}
