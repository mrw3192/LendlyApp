package com.example.lendlyapp.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Figma Design Tokens — Global ─────────────────────────────────────────────
// Source: docs/figma.json → theme/Color.kt mapping (SPEC_TECNICO §2.1)

/** #7BF179 — Primary action: buttons, active dots, action icons */
val FigmaNeonGreen = Color(0xFF7BF179)

/** #4C662B — Links ("Change", "Forgot"), active borders */
val FigmaOliveGreen = Color(0xFF4C662B)

/** #002203 — Onboarding background, high-contrast dark surfaces */
val FigmaDarkForest = Color(0xFF002203)

/** #122300 — Dark surfaces (cards on dark background) */
val FigmaOliveSeed = Color(0xFF122300)

/** #FD7E14 — Alerts and warnings */
val FigmaOrangeAccent = Color(0xFFFD7E14)

/** #FCF8F8 — Light theme background, avatars */
val FigmaLightBg = Color(0xFFFCF8F8)

/** #FFFFFF — Cards, modals, input fields */
val FigmaLightSurface = Color(0xFFFFFFFF)

/** #171D1E — Primary text (light theme) */
val FigmaLightText = Color(0xFF171D1E)

/** #0B0B0B — Dark mode general background */
val FigmaDarkBg = Color(0xFF0B0B0B)

/** #FCF8F8 — Text on dark backgrounds */
val FigmaDarkText = Color(0xFFFCF8F8)

/** #E5F5EA — Splash screen background + onboarding subtitle text */
val FigmaMintSplash = Color(0xFFE5F5EA)

// ─── Screen-local color tokens ─────────────────────────────────────────────────
// Defined per-screen in SPEC_TECNICO §2.1 "Colores locales"

/** #B1D18A — Onboarding headline titles (chartreuse green) */
val OnboardingTitleGreen = Color(0xFFB1D18A)

/** #EADDFF — Onboarding inactive page indicator dots (lavender) */
val OnboardingDotInactive = Color(0xFFEADDFF)

/** #102000 — Text on primary green buttons */
val OnPrimaryGreen = Color(0xFF102000)

/** #454745 — Form field labels */
val FormLabel = Color(0xFF454745)

/** #6A6C6A — Subtitles and input placeholders */
val SubtitleGray = Color(0xFF6A6C6A)

/** #163300 — Lendly logo shadow/back rectangle */
val LogoShadow = Color(0xFF163300)

/** #8FFF85 — Onboarding illustration gradient start */
val IllustrationGradientStart = Color(0xFF8FFF85)

/** #39A0FF — Onboarding illustration gradient end */
val IllustrationGradientEnd = Color(0xFF39A0FF)

/** #BDF0B3 — Done page "All done!" headline */
val DoneHeadlineGreen = Color(0xFFBDF0B3)

/** #0B390F — Close button background on Done page */
val CloseButtonBg = Color(0xFF0B390F)

/** #005046 — "Didn't received a code?" link on SMS verification */
val OtpLinkTeal = Color(0xFF005046)
