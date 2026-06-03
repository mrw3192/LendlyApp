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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
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

import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.theme.FigmaDarkForest
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaMintSplash
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.IllustrationGradientStart
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.MontserratFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.OnboardingDotInactive
import com.example.lendlyapp.ui.theme.OnboardingTitleGreen
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import com.example.lendlyapp.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch


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
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds(),
        ) {
            OnboardingIllustration(page = page, pageIndex = pageIndex)
        }

        OnboardingBottomContent(
            page = page,
            pageIndex = pageIndex,
            pagerState = pagerState,
            onGetStarted = onGetStarted,
            onLogin = onLogin,
            onSignUp = onSignUp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun OnboardingIllustration(
    page: OnboardingPage,
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .offset(x = 24.dp, y = 148.dp)
                .width(674.dp)
                .height(333.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(IllustrationGradientStart)
                .border(
                    width = 0.43.dp,
                    color = Color.White.copy(alpha = 0.41f),
                    shape = RoundedCornerShape(12.dp),
                ),
        )

        val (imgOffsetX, imgOffsetY, imgW, imgH) = when (pageIndex) {
            0    -> Quad(82.dp,  148.dp, 359.dp, 333.dp)
            1    -> Quad(104.dp, 148.dp, 299.dp, 333.dp)
            else -> Quad(91.dp,  133.dp, 409.dp, 409.dp)
        }

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
                .clip(RoundedCornerShape(8.dp)),
        )

        FloatingDecorations(page = page, pageIndex = pageIndex)

        val badgeIcon: ImageVector = when (pageIndex) {
            0    -> Icons.Default.Payments
            1    -> Icons.Default.LocalMall
            else -> Icons.Default.DateRange
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 16.dp)
                .size(40.dp)
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(116.dp)
                .padding(top = 44.dp, start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            LendlyLogo(
                size = DpSize(width = 116.dp, height = 40.dp),
                color = FigmaNeonGreen,
            )
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Suppress("UNCHECKED_CAST")
private operator fun <A, B, C, D> Quad<A, B, C, D>.component1() = first
@Suppress("UNCHECKED_CAST")
private operator fun <A, B, C, D> Quad<A, B, C, D>.component2() = second
@Suppress("UNCHECKED_CAST")
private operator fun <A, B, C, D> Quad<A, B, C, D>.component3() = third
@Suppress("UNCHECKED_CAST")
private operator fun <A, B, C, D> Quad<A, B, C, D>.component4() = fourth

@Composable
private fun FloatingDecorations(
    page: OnboardingPage,
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (page.emojiCards.isNotEmpty()) {
            EmojiBubble(
                emoji = page.emojiCards[0],
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 100.dp, end = 32.dp),
            )
        }
        if (page.emojiCards.size >= 2) {
            EmojiBubble(
                emoji = page.emojiCards[1],
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, top = 60.dp),
            )
        }

        when (pageIndex) {
            0 -> {
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
                ProductPriceCard(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, top = 48.dp),
                )
            }
            2 -> {
                TransactionCard(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = 16.dp),
                )
            }
        }
    }
}


@Composable
private fun EmojiBubble(
    emoji: String,
    modifier: Modifier = Modifier,
) {
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
            color = FigmaMintSplash,
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
            color = FigmaMintSplash,
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

        is OnboardingLayout.SingleCta -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(layout.outerGap),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OnboardingTitle(text = page.title)
                    OnboardingSubtitle(text = page.subtitle)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
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

        is OnboardingLayout.DoubleCta -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(layout.outerGap),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OnboardingTitle(text = page.title)

                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PageIndicatorDots(
                        currentPage = pageIndex,
                        pageCount = onboardingPages.size,
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            LendlySecondaryButton(
                                text = "Log In",
                                onClick = onLogin,
                                modifier = Modifier.fillMaxWidth(),
                            )
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
