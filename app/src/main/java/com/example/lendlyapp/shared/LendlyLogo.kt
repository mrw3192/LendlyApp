package com.example.lendlyapp.shared

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.lendlyapp.ui.theme.FigmaNeonGreen

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
