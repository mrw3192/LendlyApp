package com.example.lendlyapp.ui.screens.onboarding

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─── Onboarding Page Data Model ────────────────────────────────────────────────
// Three data objects, one per Figma frame:
//   "Onboarding 1" id=121:1311
//   "Onboarding 2" id=189:3550
//   "Onboarding 3" id=189:3591

/**
 * Describes the layout configuration for the bottom content section of each page.
 *
 * Figma bottom-section layout differences per page (SPEC data extracted):
 *
 * | Page | Outer gap (title → indicators block) | Inner gap (dots → button) | CTA style |
 * |------|---------------------------------------|---------------------------|-----------|
 * |  0   | 81 dp                                 | 32 dp                     | single    |
 * |  1   | 87 dp                                 | 24 dp                     | single    |
 * |  2   | 71 dp                                 | N/A (two separate buttons)| double    |
 */
sealed class OnboardingLayout {
    /**
     * Pages 0 and 1: single "Get Started" / "Continue" button.
     * @param outerGap  vertical gap between [Frame 169] (title+sub) and [Frame 223/224] (dots+btn)
     * @param innerGap  vertical gap between the dots row and the CTA button (inside Frame 221/222)
     */
    data class SingleCta(
        val outerGap: Dp,
        val innerGap: Dp,
    ) : OnboardingLayout()

    /**
     * Page 2 (last): two CTAs — "Log In" (secondary/transparent) + "Sign up for free" (primary).
     * @param outerGap  vertical gap between the title text and [Frame 226] (dots+buttons+home)
     */
    data class DoubleCta(
        val outerGap: Dp,
    ) : OnboardingLayout()
}

/**
 * Immutable data model for a single onboarding page.
 *
 * @param title      Headline text — Montserrat ExtraBold 32sp — #B1D18A
 * @param subtitle   Body text    — Inter Regular 22sp        — #E5F5EA  (empty on last page)
 * @param imageAsset Asset path relative to `app/src/main/assets/` (loaded via Coil)
 * @param layout     Controls bottom-section spacing and CTA button configuration
 *
 * Decorative data baked in:
 * @param emojiCards  List of emoji characters shown in floating "reaction bubble" cards
 * @param iconResId   Material icon for the green circle badge (null = no badge)
 */
data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val imageAsset: String,
    val layout: OnboardingLayout,
    val emojiCards: List<String> = emptyList(),
)

// ─── Pages List ────────────────────────────────────────────────────────────────

val onboardingPages: List<OnboardingPage> = listOf(

    // ── Page 0 — "Quick loans" ────────────────────────────────────────────────
    // Figma: Onboarding 1 (id=121:1311)
    // Hero image: onboarding_1.png (imageRef 30e61e1c...) — 359×333dp at offset (82, 148)
    // Floating elements: 😄 👏 bubbles, two balance cards ("Loan Successful"), green payment icon
    OnboardingPage(
        title = "Quick loans",
        subtitle = "Trusted for easy,\nfast loan approvals.",
        imageAsset = "onboarding_1.png",
        layout = OnboardingLayout.SingleCta(outerGap = 81.dp, innerGap = 32.dp),
        emojiCards = listOf("😄", "👏"),
    ),

    // ── Page 1 — "Loan Product In-App" ────────────────────────────────────────
    // Figma: Onboarding 2 (id=189:3550)
    // Hero image: onboarding_2.png (imageRef 6b63c4ac...) — 299×333dp at offset (104, 148)
    // Floating elements: 💸 😲 bubbles, two iPhone product cards, green shopping icon
    OnboardingPage(
        title = "Loan Product\nIn-App",
        subtitle = "Many products to loan.",
        imageAsset = "onboarding_2.png",
        layout = OnboardingLayout.SingleCta(outerGap = 87.dp, innerGap = 24.dp),
        emojiCards = listOf("💸", "😲"),
    ),

    // ── Page 2 — "Track & Pay Easily" ─────────────────────────────────────────
    // Figma: Onboarding 3 (id=189:3591)
    // Hero image: onboarding_3.png (imageRef aedadd...) — 409×409dp at offset (91, 133)
    // Floating elements: 💵 😁 bubbles, Nike transaction card, green calendar icon
    OnboardingPage(
        title = "Track & Pay\nEasily",
        subtitle = "",  // last page has no subtitle text in Figma
        imageAsset = "onboarding_3.png",
        layout = OnboardingLayout.DoubleCta(outerGap = 71.dp),
        emojiCards = listOf("💵", "😊"),  // Figma node 189:3824
    ),
)
