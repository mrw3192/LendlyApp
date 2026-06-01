package com.example.lendlyapp.ui.screens.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Payments
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

sealed class OnboardingLayout {
    data class SingleCta(val outerGap: Dp, val innerGap: Dp) : OnboardingLayout()
    data class DoubleCta(val outerGap: Dp) : OnboardingLayout()
}

data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val imageAsset: String,
    val layout: OnboardingLayout,
    val emojiCards: List<String> = emptyList(),
    val badgeIcon: ImageVector = Icons.Default.Payments,
    val badgeOffset: DpOffset = DpOffset(16.dp, 210.dp),
    val emojiOffsets: List<DpOffset> = listOf(
        DpOffset(123.dp, 175.dp),
        DpOffset(48.dp, 337.dp),
    ),
)

val onboardingPages: List<OnboardingPage> = listOf(

    OnboardingPage(
        title = "Quick loans",
        subtitle = "Trusted for easy,\nfast loan approvals.",
        imageAsset = "onboarding_1.png",
        layout = OnboardingLayout.SingleCta(outerGap = 81.dp, innerGap = 32.dp),
        badgeIcon = Icons.Default.Payments,
        badgeOffset = DpOffset(18.dp, 145.dp),
        emojiCards = listOf("😄", "👏"),
        emojiOffsets = listOf(
            DpOffset(140.dp, 125.dp),
            DpOffset(48.dp, 310.dp),
        ),
    ),

    OnboardingPage(
        title = "Loan Product\nIn-App",
        subtitle = "Many products to loan.",
        imageAsset = "onboarding_2.png",
        layout = OnboardingLayout.SingleCta(outerGap = 87.dp, innerGap = 24.dp),
        badgeIcon = Icons.Default.LocalMall,
        badgeOffset = DpOffset(20.dp, 140.dp),
        emojiCards = listOf("💸", "😲"),
        emojiOffsets = listOf(
            DpOffset(250.dp, 175.dp),
            DpOffset(48.dp, 250.dp),
        ),
    ),

    OnboardingPage(
        title = "Track & Pay\nEasily",
        subtitle = "",
        imageAsset = "onboarding_3.png",
        layout = OnboardingLayout.DoubleCta(outerGap = 71.dp),
        badgeIcon = Icons.Default.DateRange,
        badgeOffset = DpOffset(198.dp, 215.dp),
        emojiCards = listOf("💵", "😊"),
        emojiOffsets = listOf(
            DpOffset(80.dp, 315.dp),
            DpOffset(25.dp, 190.dp),
        ),
    ),
)
