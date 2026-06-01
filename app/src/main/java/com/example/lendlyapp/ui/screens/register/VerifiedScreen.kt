package com.example.lendlyapp.ui.screens.register

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.shared.LendlyBottomBar
import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily

// ─── Verified Screen ─────────────────────────────────────────────────────────
// Figma node: 224:3190 "Verfied page" — 393×1068dp
// Background: #FCF8F8 (FigmaLightBg)
//
// Layout:
//   Top App Bar → centered content (shield icon + titles + security card)
//   → "Next" button

@Composable
fun VerifiedScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    val shieldBitmap = remember {
        BitmapFactory.decodeStream(context.assets.open("img_dc808577345276dc.png"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        // ── Top App Bar ────────────────────────────────────────────────────
        LendlyTopAppBar(onBackClick = onBackClick)

        // ── Main content — vertically centered ─────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            // Shield icon — 108×130dp
            Image(
                bitmap = shieldBitmap.asImageBitmap(),
                contentDescription = "Verification shield",
                modifier = Modifier.size(width = 108.dp, height = 130.dp),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Title — 28sp SemiBold
            Text(
                text = "Woah, Your face and ID are the same!",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                color = FigmaLightText,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle — 16sp Normal
            Text(
                text = "We are just a few questions away from opening your own lendly account.",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = FormLabel,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Security card ──────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                ) {
                    // Card title — 16sp SemiBold
                    Text(
                        text = "Security Guard",
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = FigmaLightText,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card body — 16sp Normal
                    Text(
                        text = "Our online security feature world-class protection against hackers. Your transactions are safe.",
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = FormLabel,
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
