package com.example.lendlyapp.ui.screens.register

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.shared.LendlyBottomBar
import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily

// ─── ID Verification Screen ──────────────────────────────────────────────────
// Figma node: 180:2232 "ID-verification page" — 393×1068dp
// Background: #FCF8F8 (FigmaLightBg)
//
// Layout:
//   Top App Bar → headline → camera simulation with scan frame → "Next" button

@Composable
fun IdVerificationScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    // Load asset images asynchronously to prevent blocking the main thread (ANR)
    val backgroundBitmap by produceState<androidx.compose.ui.graphics.ImageBitmap?>(initialValue = null) {
        value = withContext(Dispatchers.IO) {
            try {
                val options = BitmapFactory.Options().apply { inSampleSize = 4 }
                context.assets.open("img_9fe268f1c8fbf341.png").use { stream ->
                    BitmapFactory.decodeStream(stream, null, options)?.asImageBitmap()
                }
            } catch (e: Exception) { null }
        }
    }
    
    val idCardBitmap by produceState<androidx.compose.ui.graphics.ImageBitmap?>(initialValue = null) {
        value = withContext(Dispatchers.IO) {
            try {
                val options = BitmapFactory.Options().apply { inSampleSize = 2 }
                context.assets.open("img_36f4f0154d5547c9.png").use { stream ->
                    BitmapFactory.decodeStream(stream, null, options)?.asImageBitmap()
                }
            } catch (e: Exception) { null }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        // ── Top App Bar ────────────────────────────────────────────────────
        LendlyTopAppBar(onBackClick = onBackClick)

        // ── Content ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            // ── Headline section ───────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                // Title — 28sp SemiBold
                Text(
                    text = "Let's scan your ID",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    color = FigmaLightText,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle — 16sp Normal
                Text(
                    text = "Always keep your phone in portrait mode, and here are some more tips.",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = FormLabel,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Camera simulation area (Frame 179) ─────────────────────────
            // 393×357dp with background image and overlaid scan target frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(357.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (backgroundBitmap != null) {
                    Image(
                        bitmap = backgroundBitmap!!,
                        contentDescription = "Camera preview background",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }

                // Scan target frame — white rounded rectangle border
                // 361×230dp with 16dp corners and 2dp white border
                Box(
                    modifier = Modifier
                        .size(width = 361.dp, height = 230.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (idCardBitmap != null) {
                        Image(
                            bitmap = idCardBitmap!!,
                            contentDescription = "ID card",
                            modifier = Modifier.size(width = 297.dp, height = 187.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
        }

        // ── Bottom bar ─────────────────────────────────────────────────────
        LendlyBottomBar(
            buttonText = "Next",
            onClick = onNextClick,
        )
    }
}
