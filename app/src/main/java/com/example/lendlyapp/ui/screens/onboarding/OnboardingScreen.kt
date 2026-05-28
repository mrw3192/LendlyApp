package com.example.lendlyapp.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lendlyapp.ui.shared.HomeIndicatorBar
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.theme.FigmaDarkForest
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaMintSplash
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.IllustrationGradientEnd
import com.example.lendlyapp.ui.theme.IllustrationGradientStart
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.MontserratFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.OnboardingDotInactive
import com.example.lendlyapp.ui.theme.OnboardingTitleGreen
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import com.example.lendlyapp.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch

// ═══════════════════════════════════════════════════════════════════════════════
// OnboardingScreen — HorizontalPager wrapping all three onboarding pages
//
// Figma nodes:
//   Page 0 → id=121:1311  "Onboarding 1"  (Quick loans)
//   Page 1 → id=189:3550  "Onboarding 2"  (Loan Product In-App)
//   Page 2 → id=189:3591  "Onboarding 3"  (Track & Pay Easily)
//
// Screen spec: 393×852dp, background #002203
// Illustration area: 698×481dp (clipped to 393dp screen width)
// Content area: 393×339dp, starts at y=513dp
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { pageIndex ->
        OnboardingPageContent(
            page = onboardingPages[pageIndex],
            pageIndex = pageIndex,
            pagerState = pagerState,
            onGetStarted = {
                // Pages 0 & 1: advance to next page
                scope.launch {
                    pagerState.animateScrollToPage(pageIndex + 1)
                }
            },
            onLogin = {
                viewModel.onNavigateAway()
                onNavigateToLogin()
            },
            onSignUp = {
                viewModel.onNavigateAway()
                onNavigateToRegister()
            },
        )
    }
}

// ─── Single pager page ─────────────────────────────────────────────────────────

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    pageIndex: Int,
    pagerState: PagerState,
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaDarkForest),
        // gap=32dp between illustration section and content section (Figma "Onboarding X-content")
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        // ── Upper illustration area (weight=1f adapts to screen height) ───────
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds(),  // clips the 698dp-wide inner content to the 393dp screen
        ) {
            OnboardingIllustration(page = page, pageIndex = pageIndex)
        }

        // ── Lower content area (339dp — fixed per Figma) ─────────────────────
        OnboardingBottomContent(
            page = page,
            pageIndex = pageIndex,
            pagerState = pagerState,
            onGetStarted = onGetStarted,
            onLogin = onLogin,
            onSignUp = onSignUp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),  // 16dp margin → 361dp effective width for buttons
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Illustration Area
// ═══════════════════════════════════════════════════════════════════════════════
//
// Figma structure (all pages share the same skeleton):
//   Frame 237/238/239  698×481dp  ← wider than screen, crops at 393dp
//     Frame 219  674×333dp  at offset (24, 148) ← gradient + hero image
//     Frame 229/230/231  393×116dp  at offset (0, 0) ← status bar + logo overlay

@Composable
private fun OnboardingIllustration(
    page: OnboardingPage,
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {

        // ── Gradient backdrop ────────────────────────────────────────────────
        // Figma node 121:1322 "Rectangle 4" — 674×333dp at offset(24, 148).
        // gradientHandlePositions [0,0.5]→[1,0.5] = horizontal (left→right).
        // stroke: white opacity=0.41, strokeWeight=0.43
        // effects: BACKGROUND_BLUR radius=5.48 → Modifier.blur(5.dp) approximation
        Box(
            modifier = Modifier
                .offset(x = 24.dp, y = 148.dp)
                .width(674.dp)
                .height(333.dp)
                .clip(RoundedCornerShape(12.dp))
                .blur(5.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(IllustrationGradientStart, IllustrationGradientEnd),
                    ),
                )
                .border(
                    width = 0.43.dp,
                    color = Color.White.copy(alpha = 0.41f),
                    shape = RoundedCornerShape(12.dp),
                ),
        )

        // ── Hero image ───────────────────────────────────────────────────────
        // Loaded from assets/ via Coil.  Falls back to gradient background if missing.
        // Positions per page (Figma absolute → relative to screen):
        //   Page 0: offset(82,148) size=359×333
        //   Page 1: offset(104,148) size=299×333
        //   Page 2: offset(91,133) size=409×409
        val (imgOffsetX, imgOffsetY, imgW, imgH) = when (pageIndex) {
            0    -> Quad(82.dp,  148.dp, 359.dp, 333.dp)
            1    -> Quad(104.dp, 148.dp, 299.dp, 333.dp)
            else -> Quad(91.dp,  133.dp, 409.dp, 409.dp)
        }

        // Figma node 121:1461: rotation=180°, filters={saturation:-1, contrast:-0.13} (page 0 only)
        val heroColorFilter = if (pageIndex == 0) {
            ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
        } else null

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/${page.imageAsset}")
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = heroColorFilter,
            modifier = Modifier
                .offset(x = imgOffsetX, y = imgOffsetY)
                .size(width = imgW, height = imgH)
                .clip(RoundedCornerShape(8.dp))
                .then(if (pageIndex == 0) Modifier.graphicsLayer { scaleX = -1f } else Modifier),
        )

        // ── Floating decorative elements ──────────────────────────────────────
        // Approximated positions — exact canvas offsets not extractable without
        // downloading the Figma layout tree for sub-frame absolute positions.
        // These are visually representative placements on top of the hero image.
        FloatingDecorations(page = page, pageIndex = pageIndex)

        // ── Green icon circle badge ──────────────────────────────────────────
        // Figma: 'Balance-card' fill=#7BF179 r=1000 → circle with icon
        // Positioned bottom-right of the visible illustration area
        val badgeIcon: ImageVector = when (pageIndex) {
            0    -> Icons.Default.Payments
            1    -> Icons.Default.LocalMall
            else -> Icons.Default.DateRange
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 16.dp)
                .size(40.dp)                          // Figma: 40×40dp (era 44dp)
                .background(color = FigmaNeonGreen, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = badgeIcon,
                contentDescription = null,
                tint = OnPrimaryGreen,
                modifier = Modifier.size(24.dp),
            )
        }

        // ── Status bar + Logo overlay ────────────────────────────────────────
        // Figma Frame 229/230/231 (393×116dp) — overlaid at top of illustration
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(116.dp)
                .padding(top = 44.dp, start = 16.dp),   // 44dp = status bar height
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            // Lendly Logo — small variant (116×40dp per Figma)
            LendlyLogo(
                size = DpSize(width = 116.dp, height = 40.dp),
                color = FigmaNeonGreen,
            )
        }
    }
}

// ─── Helper data carrier for image positioning ────────────────────────────────
private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Suppress("UNCHECKED_CAST")
private operator fun <A, B, C, D> Quad<A, B, C, D>.component1() = first
@Suppress("UNCHECKED_CAST")
private operator fun <A, B, C, D> Quad<A, B, C, D>.component2() = second
@Suppress("UNCHECKED_CAST")
private operator fun <A, B, C, D> Quad<A, B, C, D>.component3() = third
@Suppress("UNCHECKED_CAST")
private operator fun <A, B, C, D> Quad<A, B, C, D>.component4() = fourth

// ─── Floating Decorations ────────────────────────────────────────────────────
// Simplified approximation of the floating emoji-reaction and balance cards
// visible in the Figma illustration area.  Exact canvas positions are
// not available without full sub-frame absolute coordinate extraction.

@Composable
private fun FloatingDecorations(
    page: OnboardingPage,
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Emoji reaction bubble #1 — upper right quadrant
        if (page.emojiCards.isNotEmpty()) {
            EmojiBubble(
                emoji = page.emojiCards[0],
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 100.dp, end = 32.dp),
            )
        }
        // Emoji reaction bubble #2 — mid right
        if (page.emojiCards.size >= 2) {
            EmojiBubble(
                emoji = page.emojiCards[1],
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, top = 60.dp),
            )
        }

        // ── Page-specific floating card decorations ────────────────────────
        when (pageIndex) {
            0 -> {
                // Figma Frame 227 (189:3701): dos instancias de LoanSuccessCard apiladas
                // 189:3693 at y≈-66, 189:3673 at y≈-28 (gap ≈ 8dp entre tarjetas)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LoanSuccessCard()
                    LoanSuccessCard()
                }
            }
            1 -> {
                // Product price card — left/center area
                ProductPriceCard(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, top = 48.dp),
                )
            }
            2 -> {
                // Nike transaction card — center area
                TransactionCard(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = 16.dp),
                )
            }
        }
    }
}

// ─── Floating card sub-composables ────────────────────────────────────────────

@Composable
private fun EmojiBubble(
    emoji: String,
    modifier: Modifier = Modifier,
) {
    // Figma nodes 189:3710 / 189:3753:
    //   size=30×30dp, cornerRadius=38.99 (≈ full pill)
    //   backgroundColor=rgba(1,1,1,0.27) — frosted glass semi-transparent
    //   stroke: white opacity=0.51, weight=0.39
    //   BACKGROUND_BLUR 5.85 — omitted (Modifier.blur blurs texto también)
    //   emoji font: Montserrat SemiBold 18sp
    Box(
        modifier = modifier
            .size(30.dp)
            .background(
                color = Color.White.copy(alpha = 0.27f),
                shape = RoundedCornerShape(100.dp),
            )
            .border(
                width = 0.39.dp,
                color = Color.White.copy(alpha = 0.51f),
                shape = RoundedCornerShape(100.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = emoji,
            fontSize = 14.sp,
            fontFamily = MontserratFamily,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun LoanSuccessCard(modifier: Modifier = Modifier) {
    // Figma nodos 189:3693 / 189:3673 — Frame 19 (hijo):
    //   backgroundColor=rgba(1,1,1,0.27), cornerRadius=6.24dp, padding=8.32dp
    //   Frame 18 (HORIZONTAL): itemSpacing=3.12dp
    //   stroke: white opacity=0.51, weight=0.52
    //   Hijos: avatar (d6c8be → avatar.png) + "Loan Successful" (#E5F5EA) + "+ ₱ 2,000.00" (verde)
    Row(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.27f),
                shape = RoundedCornerShape(6.dp),
            )
            .border(
                width = 0.52.dp,
                color = Color.White.copy(alpha = 0.51f),
                shape = RoundedCornerShape(6.dp),
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/avatar.png")
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape),
        )
        Text(
            text = "Loan Successful",
            fontSize = 8.sp,
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold,
            color = FigmaMintSplash,     // Figma: rgba(0.898,0.961,0.918) = #E5F5EA
        )
        Text(
            text = "+ ₱ 2,000.00",
            fontSize = 8.sp,
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold,
            color = FigmaNeonGreen,
        )
    }
}

@Composable
private fun ProductPriceCard(modifier: Modifier = Modifier) {
    // Figma nodo Product-card (189:3734): frosted glass, r≈4dp, padding≈6dp, itemSpacing≈3dp
    // stroke: white opacity=0.51, backgroundColor=rgba(1,1,1,0.27)
    Column(
        modifier = modifier
            .width(80.dp)
            .background(
                color = Color.White.copy(alpha = 0.27f),
                shape = RoundedCornerShape(4.dp),
            )
            .border(
                width = 0.52.dp,
                color = Color.White.copy(alpha = 0.51f),
                shape = RoundedCornerShape(4.dp),
            )
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = "iPhone 12 Pro",
            fontSize = 7.sp,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            color = FigmaMintSplash,     // Figma: texto label claro sobre fondo oscuro
        )
        Text(
            text = "₱1,200 x 24 mo",
            fontSize = 7.sp,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            color = FigmaNeonGreen,
        )
    }
}

@Composable
private fun TransactionCard(modifier: Modifier = Modifier) {
    // Figma nodo 189:3792 — size=197×49dp, frosted glass
    // backgroundColor=rgba(1,1,1,0.27), r=6dp, padding=8dp
    // stroke: white opacity=0.51, itemSpacing=2dp
    Row(
        modifier = modifier
            .width(197.dp)
            .height(49.dp)
            .background(
                color = Color.White.copy(alpha = 0.27f),
                shape = RoundedCornerShape(6.dp),
            )
            .border(
                width = 0.52.dp,
                color = Color.White.copy(alpha = 0.51f),
                shape = RoundedCornerShape(6.dp),
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Nike Inc.",
                fontSize = 11.sp,
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                color = FigmaMintSplash,
            )
            Text(
                text = "Fees of February",
                fontSize = 9.sp,
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                color = FigmaMintSplash,
            )
        }
        Text(
            text = "₱400.00",
            fontSize = 10.sp,
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold,
            color = FigmaNeonGreen,
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Bottom Content Section
// ═══════════════════════════════════════════════════════════════════════════════
//
// Total height: 339dp  (Figma Frame 233/234/235)
// Horizontal padding: 16dp applied by parent → content fills 361dp effective width
// Layout varies per page — controlled by OnboardingLayout sealed class.

@Composable
private fun OnboardingBottomContent(
    page: OnboardingPage,
    pageIndex: Int,
    pagerState: PagerState,
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (val layout = page.layout) {

        // ── Pages 0 & 1 : single CTA ────────────────────────────────────────
        // Figma structure (Frame 233 / Frame 234):
        //   Column gap=[outerGap]
        //     Frame 169 (title + subtitle, gap=20dp)
        //     Frame 223/224 (indicators + button + home, gap=16dp)
        //       Frame 221/222 (indicators + button, gap=[innerGap])
        is OnboardingLayout.SingleCta -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(layout.outerGap),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Frame 169 — title + subtitle
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OnboardingTitle(text = page.title)
                    OnboardingSubtitle(text = page.subtitle)
                }

                // Frame 223/224 — indicators + button + home
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Frame 221/222 — indicators + button
                    Column(
                        verticalArrangement = Arrangement.spacedBy(layout.innerGap),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        PageIndicatorDots(
                            currentPage = pageIndex,
                            pageCount = onboardingPages.size,
                        )
                        LendlyPrimaryButton(
                            text = "Get Started",
                            onClick = onGetStarted,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    HomeIndicatorBar(
                        color = Color.White,
                        modifier = Modifier.navigationBarsPadding(),
                    )
                }
            }
        }

        // ── Page 2 : double CTA ──────────────────────────────────────────────
        // Figma structure (Frame 235):
        //   Column gap=[outerGap]
        //     TEXT "Track & Pay Easily"   ← directly in Frame 235 (no Frame 169 wrapper)
        //     Frame 226 (indicators + button group + home, gap=24dp)
        //       Frame 9 (dots)
        //       Frame 225 (buttons + home, gap=16dp)
        //         Frame 220 (two buttons, gap=16dp)
        //         Home Indicator
        is OnboardingLayout.DoubleCta -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(layout.outerGap),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Title only — no subtitle on last page
                OnboardingTitle(text = page.title)

                // Frame 226 — indicators + buttons + home
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PageIndicatorDots(
                        currentPage = pageIndex,
                        pageCount = onboardingPages.size,
                    )

                    // Frame 225 — buttons + home
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Frame 220 — two CTAs stacked, gap=16dp
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            // "Log In" — secondary / transparent, white text
                            LendlySecondaryButton(
                                text = "Log In",
                                onClick = onLogin,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            // "Sign up for free" — primary / green
                            LendlyPrimaryButton(
                                text = "Sign up for free",
                                onClick = onSignUp,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        HomeIndicatorBar(
                            color = Color.White,
                            modifier = Modifier.navigationBarsPadding(),
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Reusable UI Components
// ═══════════════════════════════════════════════════════════════════════════════

// ─── Onboarding Title ─────────────────────────────────────────────────────────
// Figma: Montserrat ExtraBold 32sp, lineHeight 35–40sp, color #B1D18A, CENTER aligned

@Composable
fun OnboardingTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        color = OnboardingTitleGreen,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth(),
    )
}

// ─── Onboarding Subtitle ──────────────────────────────────────────────────────
// Figma: Inter Regular 22sp, lineHeight 28sp, color #E5F5EA, CENTER aligned

@Composable
fun OnboardingSubtitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = FigmaMintSplash,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth(),
    )
}

// ─── Page Indicator Dots ──────────────────────────────────────────────────────
// Figma "Frame 9": 52×12dp container, 3 ellipses 12×12dp, gap=8dp between edges
// Active: #7BF179 opacity=1.0   Inactive: #EADDFF opacity=0.16

@Composable
fun PageIndicatorDots(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = FigmaNeonGreen,
    inactiveColor: Color = OnboardingDotInactive.copy(alpha = 0.16f),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = if (index == currentPage) activeColor else inactiveColor,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

// ─── Primary CTA Button ───────────────────────────────────────────────────────
// Figma: fill=#7BF179, cornerRadius=100dp (full pill), text=#102000
// Width: 361dp (parent provides fillMaxWidth with 16dp horizontal padding)
// Height: 48dp

@Composable
fun LendlyPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FigmaNeonGreen,
            contentColor = OnPrimaryGreen,
        ),
    ) {
        Text(
            text = text,
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
    }
}

// ─── Secondary CTA Button (Log In) ────────────────────────────────────────────
// Figma: no fill (transparent background), 1dp border, text=#FFFFFF
// Width: 361dp, Height: 48dp, cornerRadius=100dp

@Composable
fun LendlySecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.6f),
        ),
    ) {
        Text(
            text = text,
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════════════════════════

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingPage0Preview() {
    LendlyAppTheme(dynamicColor = false) {
        OnboardingPageContent(
            page = onboardingPages[0],
            pageIndex = 0,
            pagerState = rememberPagerState(pageCount = { 3 }),
            onGetStarted = {},
            onLogin = {},
            onSignUp = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingPage2Preview() {
    LendlyAppTheme(dynamicColor = false) {
        OnboardingPageContent(
            page = onboardingPages[2],
            pageIndex = 2,
            pagerState = rememberPagerState(pageCount = { 3 }),
            onGetStarted = {},
            onLogin = {},
            onSignUp = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF002203)
@Composable
private fun PageIndicatorDotsPreview() {
    LendlyAppTheme(dynamicColor = false) {
        PageIndicatorDots(currentPage = 1, pageCount = 3)
    }
}
