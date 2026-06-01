package com.example.lendlyapp.ui.screens.register

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
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

// ─── Face Recognition Screen ─────────────────────────────────────────────────
// Figma node: 165:2078 "Face-recognation page" — 393×1068dp
// Background: #FCF8F8 (FigmaLightBg)
//
// Layout:
//   Top App Bar → headline → camera simulation with oval face frame → "Next" button

@Composable
fun FaceRecognitionScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    val backgroundBitmap by produceState<androidx.compose.ui.graphics.ImageBitmap?>(initialValue = null) {
        value = withContext(Dispatchers.IO) {
            try {
                val options = BitmapFactory.Options().apply { inSampleSize = 4 }
                context.assets.open("img_ec4b2d5a8922c8f2.png").use { stream ->
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
                    text = "Put your face in the frame",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    color = FigmaLightText,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle — 16sp Normal
                Text(
                    text = "Follow these instruction, and let us get you onboarded.",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = FormLabel,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Camera simulation area (Frame 179) ─────────────────────────
            // 393×357dp with background image and overlaid oval face frame
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

                // Oval face frame — 150×190dp, white border, no fill
                // Drawn with Canvas drawOval using a 2dp stroke
                Canvas(
                    modifier = Modifier
                        .align(Alignment.Center),
                ) {
                    val ovalWidth = 150.dp.toPx()
                    val ovalHeight = 190.dp.toPx()
                    drawOval(
                        color = Color.White,
                        topLeft = Offset(
                            x = center.x - ovalWidth / 2f,
                            y = center.y - ovalHeight / 2f,
                        ),
                        size = Size(ovalWidth, ovalHeight),
                        style = Stroke(width = 2.dp.toPx()),
                    )
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
