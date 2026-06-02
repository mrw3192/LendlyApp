package com.example.lendlyapp.ui.screens.onboarding

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class OnboardingLayout {
    data class SingleCta(
        val outerGap: Dp,
        val innerGap: Dp,
    ) : OnboardingLayout()

    data class DoubleCta(
        val outerGap: Dp,
    ) : OnboardingLayout()
}

data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val imageAsset: String,
    val layout: OnboardingLayout,
    val emojiCards: List<String> = emptyList(),
)

val onboardingPages: List<OnboardingPage> = listOf(

    OnboardingPage(
        title = "Quick loans",
        subtitle = "Trusted for easy,\nfast loan approvals.",
        imageAsset = "onboarding_1.png",
        layout = OnboardingLayout.SingleCta(outerGap = 81.dp, innerGap = 32.dp),
        emojiCards = listOf("😄", "👏"),
    ),

    OnboardingPage(
        title = "Loan Product\nIn-App",
        subtitle = "Many products to loan.",
        imageAsset = "onboarding_2.png",
        layout = OnboardingLayout.SingleCta(outerGap = 87.dp, innerGap = 24.dp),
        emojiCards = listOf("💸", "😲"),
    ),

    OnboardingPage(
        title = "Track & Pay\nEasily",
        subtitle = "",  // last page has no subtitle text in Figma
        imageAsset = "onboarding_3.png",
        layout = OnboardingLayout.DoubleCta(outerGap = 71.dp),
        emojiCards = listOf("💵", "😊"),  // Figma node 189:3824
    ),
)
