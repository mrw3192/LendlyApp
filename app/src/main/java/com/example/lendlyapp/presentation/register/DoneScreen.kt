package com.example.lendlyapp.presentation.register

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.lendlyapp.presentation.components.LendlyBottomBar
import com.example.lendlyapp.presentation.components.LendlyLogo
import com.example.lendlyapp.theme.CloseButtonBg
import com.example.lendlyapp.theme.DoneHeadlineGreen
import com.example.lendlyapp.theme.FigmaDarkForest
import com.example.lendlyapp.theme.FigmaMintSplash
import com.example.lendlyapp.theme.FigmaNeonGreen
import com.example.lendlyapp.theme.InterFamily
import com.example.lendlyapp.theme.MontserratFamily

// ─── Done Page ─────────────────────────────────────────────────────────────────
// Figma node: 181:2290 "done-page" — 393×1068dp
// Background: #002203 (FigmaDarkForest)
//
// Layout:
//   Close button + Logo → large checkmark image → "ALL DONE!" headline
//   → subtitle → "Done" button (white home indicator)

@Composable
fun DoneScreen(
    onNavigateToHome: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaDarkForest)
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Top bar with close button + logo ───────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Close button (rounded dark bg)
                IconButton(
                    onClick = onNavigateToHome,
                    modifier = Modifier
                        .size(48.dp)
                        .background(CloseButtonBg, CircleShape),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = FigmaNeonGreen,
                    )
                }

                // Lendly Logo (smaller version)
                LendlyLogo(
                    size = DpSize(width = 116.dp, height = 40.dp),
                )

                // Spacer for symmetry (info icon placeholder)
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Checkmark illustration ──────────────────────────────────────
            // Figma: IMG:d0ee99f6e871 — 183×330dp
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Uri.parse("file:///android_asset/check_mark.png"))
                    .crossfade(true)
                    .build(),
                contentDescription = "Success checkmark",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(width = 183.dp, height = 330.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── "ALL DONE!" headline ───────────────────────────────────────
            // Figma: 36sp, fw=700, fill=#BDF0B3 (DoneHeadlineGreen)
            Text(
                text = "ALL DONE!",
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = DoneHeadlineGreen,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Subtitle ───────────────────────────────────────────────────
            // Figma: 22sp, fw=400, fill=#E5F5EA (FigmaMintSplash)
            Text(
                text = "You're ready to start a loan.",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                color = FigmaMintSplash,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // ── Bottom bar with "Done" button (white indicator) ────────────────
        LendlyBottomBar(
            buttonText = "Done",
            onClick = onNavigateToHome,
            modifier = Modifier.align(Alignment.BottomCenter),
            indicatorColor = Color.White,
        )
    }
}
