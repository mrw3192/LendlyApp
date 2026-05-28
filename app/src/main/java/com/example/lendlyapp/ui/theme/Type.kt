package com.example.lendlyapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.R

// ─── Font Families (bundled TTFs in res/font/) ────────────────────────────────
// Fonts sourced from Google Fonts and bundled locally to avoid runtime network
// dependency. Files live in app/src/main/res/font/.

/** Montserrat — used for onboarding page titles (32sp ExtraBold) */
val MontserratFamily = FontFamily(
    Font(R.font.montserrat_regular,   FontWeight.Normal),
    Font(R.font.montserrat_semibold,  FontWeight.SemiBold),
    Font(R.font.montserrat_bold,      FontWeight.Bold),
    Font(R.font.montserrat_extrabold, FontWeight.ExtraBold),
)

/** Inter — used for body text, subtitles, and button labels */
val InterFamily = FontFamily(
    Font(R.font.inter_regular,  FontWeight.Normal),
    Font(R.font.inter_medium,   FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold,     FontWeight.Bold),
)

// ─── Material 3 Typography ─────────────────────────────────────────────────────
// Figma text-role mapping:
//   displaySmall  → onboarding title    (Montserrat 32sp ExtraBold, lh 36sp)
//   titleMedium   → onboarding subtitle (Inter 22sp Regular, lh 28sp)
//   labelLarge    → button labels       (Inter 14sp SemiBold, lh 20sp)  ← M3 default for buttons
//   bodyLarge     → general body copy   (Inter 16sp Normal, lh 24sp)
//   bodyMedium    → secondary body      (Inter 14sp Normal, lh 20sp)

val Typography = Typography(
    displaySmall = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
)
