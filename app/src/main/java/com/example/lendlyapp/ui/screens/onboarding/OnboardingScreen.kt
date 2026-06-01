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
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lendlyapp.ui.shared.HomeIndicatorBar
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.theme.FigmaDarkForest
import com.example.lendlyapp.ui.theme.IllustrationGradientEnd
import com.example.lendlyapp.ui.theme.IllustrationGradientStart
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaMintSplash
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FormLabel
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = false
        }
    }

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
        // ── Upper illustration area (Figma Frame 232: FIXED 481dp) ──────────
        Box(
            modifier = Modifier
                .height(481.dp)
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
@Composable
private fun OnboardingIllustration(
    page: OnboardingPage,
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {

        // ── Triangle backdrop ────────────────────────────────────────────────
        val triangleShape = object : Shape {
            override fun createOutline(
                size: Size,
                layoutDirection: LayoutDirection,
                density: Density,
            ): Outline {
                val shiftLeft = with(density) { 5.dp.toPx() }
                return Outline.Generic(Path().apply {
                    moveTo(-shiftLeft, size.height)                  // desplazado 60dp a la izquierda
                    lineTo(size.width, size.height * (260f / 481f))  // actual — llega hasta ~30% desde arriba
                    lineTo(size.width, size.height)                  // (393, 481) bottom-right
                    close()
                })
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(triangleShape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(IllustrationGradientStart, IllustrationGradientEnd),
                    ),
                ),
        )

        // ── Hero image ───────────────────────────────────────────────────────
        val (imgOffsetX, imgOffsetY, imgW, imgH) = Quad(91.dp, 133.dp, 409.dp, 409.dp)

        val heroColorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
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
                .then(
                    if (pageIndex == 0)
                        Modifier.graphicsLayer { scaleX = -1f }
                    else Modifier
                ),
        )

        // ── Floating decorative elements ─────────────────────────────────────
        FloatingDecorations(page = page, pageIndex = pageIndex)

        // ── Green badge icon ─────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .offset(x = page.badgeOffset.x, y = page.badgeOffset.y)
                .size(40.dp)
                .background(color = FigmaNeonGreen, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = page.badgeIcon,
                contentDescription = null,
                tint = OnPrimaryGreen,
                modifier = Modifier.size(24.dp),
            )
        }

        // ── Status bar + Logo overlay ───────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(116.dp)
                .padding(top = 76.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
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
        if (page.emojiCards.isNotEmpty() && page.emojiOffsets.isNotEmpty()) {
            EmojiBubble(
                emoji = page.emojiCards[1],
                modifier = Modifier.offset(x = page.emojiOffsets[0].x, y = page.emojiOffsets[0].y),
            )
        }
        if (page.emojiCards.size >= 2 && page.emojiOffsets.size >= 2) {
            EmojiBubble(
                emoji = page.emojiCards[0],
                modifier = Modifier.offset(x = page.emojiOffsets[1].x, y = page.emojiOffsets[1].y),
            )
        }

        // ── Page-specific floating card decorations ────────────────────────
        when (pageIndex) {
            0 -> {
                Column(
                    modifier = Modifier.offset(x = 35.dp, y = 180.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LoanSuccessCard()
                    LoanSuccessCard()
                }
            }
            1 -> {
                ProductCardRow(
                    modifier = Modifier.offset(x = 44.dp, y = 159.dp),
                )
            }
            2 -> {
                Column(
                    modifier = Modifier.offset(x = 20.dp, y = 247.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ){ // Nike transaction card — center area
                    TransactionCard(
                        modifier = Modifier
                            .padding(start = 2.dp),
                    )
                }
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
    Box(
        modifier = modifier
            .size(38.dp)
            .background(
                color = Color.White.copy(alpha = 0.27f),
                shape = RoundedCornerShape(100.dp),
            )
            .border(
                width = 0.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(100.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = emoji,
            fontSize = 18.sp,
        )
    }
}

@Composable
private fun LoanSuccessCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(157.dp)
            .height(24.dp)
            .background(
                color = Color.White.copy(alpha = 0.27f),
                shape = RoundedCornerShape(6.dp),
            )
            .border(
                width = 0.1.dp,
                color = Color.White.copy(alpha = 0.51f),
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Loan Successful",
            fontSize = 8.sp,
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold,
            color = FigmaMintSplash,
        )
        Text(
            text = "+ ₱ 2000.00",
            fontSize = 8.sp,
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold,
            color = FigmaNeonGreen,
        )
    }
}

@Composable
private fun ProductCardRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ProductCard(imageAsset = "product_1.png")
        ProductCard(imageAsset = "product_2.png")
    }
}

@Composable
private fun ProductCard(imageAsset: String) {
    Column(
        modifier = Modifier
            .size(width = 90.dp, height = 78.dp)
            .background(Color.White.copy(alpha = 0.27f), RoundedCornerShape(4.dp))
            .border(0.1.dp, Color.White.copy(alpha = 0.51f), RoundedCornerShape(4.dp))
            .padding(horizontal = 4.dp, vertical = 3.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/$imageAsset")
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth().height(54.dp),
        )
        Text(text = "iPhone 12 Pro...", fontSize = 9.sp, fontFamily = InterFamily, color = FigmaMintSplash, maxLines = 1)
        Text(text = "₱1,200 x 24 mo", fontSize = 9.sp, fontFamily = InterFamily, color = FigmaNeonGreen, maxLines = 1)
    }
}

@Composable
private fun TransactionCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(197.dp)
            .height(47.dp)
            .background(Color.White.copy(alpha = 0.27f), RoundedCornerShape(6.dp))
            .border(0.1.dp, Color.White.copy(alpha = 0.51f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left — logo circular + nombre
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/avatar.png")
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape),
            )
            Text(
                text = "Nike Inc.",
                fontSize = 11.sp,
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                color = FigmaMintSplash,
            )
        }
        // Right — precio + subtítulo
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "₱400.00",
                fontSize = 10.sp,
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                color = FigmaNeonGreen,
            )
            Text(
                text = "Fees of February",
                fontSize = 9.sp,
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                color = FigmaMintSplash,
            )
        }
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

                }
            }
        }

        // ── Page 2 : double CTA ──────────────────────────────────────────────
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

                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Reusable UI Components
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
fun OnboardingTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        color = OnboardingTitleGreen,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth(),
    )
}

// ─── Onboarding Subtitle ──────────────────────────────────────────────────────
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
