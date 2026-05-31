package com.example.lendlyapp.ui.screens.home

// ═══════════════════════════════════════════════════════════════════════════════
// HomeScreen — Dashboard principal
//
// Figma frame: 'Home screen'  pos=(0,0)  393×917dp  fill=#FFFFFF
//
// Secciones (posiciones absolutas del Figma):
//   App bar          y=44   h=56   (56dp after status bar)
//   "Account" title  y=132  h=36   28sp fw=600 #171D1E  x=16
//   Balance card     y=184  h=136  r=16  fill=#FCF8F8  x=16 w=361
//   "Unpaid Loans"   y=352  h=32   header + "See All"
//   Loan cards       y=400  h=76   r=12  fill=#FCF8F8  gap=12dp
//   "Recommended"    y=596  h=32   header + "See All"
//   Product cards    y=644  h=145  r=12  fill=#FCF8F8  w=132  gap=8dp
// ═══════════════════════════════════════════════════════════════════════════════

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lendlyapp.model.Loan
import com.example.lendlyapp.model.Product
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOliveSeed
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.ProductPriceGreen
import com.example.lendlyapp.ui.theme.SubtitleGray
import com.example.lendlyapp.viewmodel.HomeViewModel
import java.util.Locale

// ─── Entry point ───────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is HomeUiState.Loading -> HomeLoadingContent()
        is HomeUiState.Success -> HomeScreenContent(data = state.data)
        is HomeUiState.Error   -> HomeErrorContent(message = state.message)
    }
}

// ─── State screens ─────────────────────────────────────────────────────────────

@Composable
private fun HomeLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = FigmaNeonGreen)
    }
}

@Composable
private fun HomeErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = message, color = FigmaLightText)
    }
}

// ─── Main content ──────────────────────────────────────────────────────────────

@Composable
private fun HomeScreenContent(
    data: HomeData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        // ── App bar ───────────────────────────────────────────────────────────
        // Figma: 'App var'  pos=(0,44)  393×56dp
        HomeTopBar()

        // ── "Account" title ───────────────────────────────────────────────────
        // Figma: 'headline' pos=(0,100)  gap=32dp below app bar
        // TEXT='Account' 28sp fw=600 #171D1E  x=16
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = FigmaLightText,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        // ── Balance card ──────────────────────────────────────────────────────
        // Figma: 'Balance-card'  pos=(16,184)  361×136dp  r=16  fill=#FCF8F8
        Spacer(modifier = Modifier.height(16.dp))
        BalanceCard(
            balance = data.balance,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        // ── Unpaid Loans ──────────────────────────────────────────────────────
        // Figma: 'Frame 35'  pos=(0,352)  393×212dp
        Spacer(modifier = Modifier.height(32.dp))
        SectionHeader(
            title = "Unpaid Loans",
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            data.unpaidLoans.forEach { loan -> LoanCard(loan = loan) }
        }

        // ── Recommended For You ───────────────────────────────────────────────
        // Figma: 'Recommended-SC'  pos=(0,596)  393×193dp
        Spacer(modifier = Modifier.height(32.dp))
        SectionHeader(
            title = "Recommended For You",
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Figma: 'Frame 24'  pos=(0,644)  584×145dp (scrolls horizontally)
        // Cards start at x=16, gap=8dp, each card 132×145dp
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(data.recommendedProducts) { product ->
                ProductCard(product = product)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// App Bar
// ═══════════════════════════════════════════════════════════════════════════════
//
// Figma: 'App var'  pos=(0,44)  393×56dp  fill=#FFFFFF
//   leading-icon  pos=(4,52)   48×48  → person icon, tint=#171D1E
//   Frame 134     pos=(167,66) 58×20  → Lendly logo (centered)
//   trailing-icon pos=(341,52) 48×48  → notifications bell, tint=#1C1B1F

@Composable
private fun HomeTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Person / avatar icon
        IconButton(
            onClick = {},
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = FigmaLightText,
                modifier = Modifier.size(24.dp),
            )
        }

        // Logo — centered between the two icons
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            LendlyLogo(size = DpSize(width = 58.dp, height = 20.dp))
        }

        // Notifications bell
        IconButton(
            onClick = {},
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = FigmaLightText,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Balance Card
// ═══════════════════════════════════════════════════════════════════════════════
//
// Figma: 'Balance-card'  pos=(16,184)  361×136dp  r=16  fill=#FCF8F8
//   card-content at (32,200) → padding 16dp
//   Frame 12: 'AVAILABLE BALANCE' label + '+ Cash In' button  h=48dp
//   '₱ 2,500.00'  32sp fw=600  #102000  below Frame 12 (gap=16dp)

@Composable
private fun BalanceCard(
    balance: Double,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.height(136.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaLightBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Frame 12: label + Cash In button (h=48dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // 'AVAILABLE BALANCE'  12sp fw=500 #454745
                Text(
                    text = "AVAILABLE BALANCE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = FormLabel,
                )
                CashInButton(onClick = {})
            }

            // Balance amount — 32sp fw=600 #102000
            Text(
                text = "₱ " + String.format(Locale.US, "%,.2f", balance),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = OnPrimaryGreen,
            )
        }
    }
}

// ─── Cash In button ───────────────────────────────────────────────────────────
// Figma: 'Cash-in BT'  pos=(241,200)  120×48dp  r=100  fill=#7BF179
//   icon 18×18 + "Cash In" 14sp fw=600 #102000

@Composable
private fun CashInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(48.dp),
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FigmaNeonGreen,
            contentColor = OnPrimaryGreen,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Cash In",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Section Header  (Unpaid Loans / Recommended For You)
// ═══════════════════════════════════════════════════════════════════════════════
//
// Figma: 'headline' row  393×32dp
//   Left:  title text  22sp fw=600 #171D1E  x=16
//   Right: 'Input chip' = "See All" + chevron  14sp fw=600 #122300  r=1000

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = FigmaLightText,
        )
        SeeAllChip()
    }
}

@Composable
private fun SeeAllChip() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "See All",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = FigmaOliveSeed,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = FigmaOliveSeed,
            modifier = Modifier.size(18.dp),
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Loan Card
// ═══════════════════════════════════════════════════════════════════════════════
//
// Figma: 'Balance-card' (loan variant)  361×76dp  r=12  fill=#FCF8F8
//   Left:  avatar (40×40 circle) + company name  16sp fw=600 #171D1E
//   Right: amount 14sp fw=600 #171D1E + description 12sp fw=500 #6A6C6A

@Composable
private fun LoanCard(
    loan: Loan,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaLightBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left: avatar + company name
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(loan.lenderLogo)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
                Text(
                    text = loan.companyName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = FigmaLightText,
                )
            }

            // Right: amount + description
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = String.format(Locale.US, "₱%.2f", loan.amount),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = FigmaLightText,
                )
                Text(
                    text = loan.description ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = SubtitleGray,
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Product Card
// ═══════════════════════════════════════════════════════════════════════════════
//
// Figma: 'Product-card'  132×145dp  r=12  fill=#FCF8F8
//   Image  85×65dp  centered horizontally  padding-top=16dp
//   Name   12sp fw=500  #454745  x=16 (padding-start=16dp)
//   Price  11sp fw=500  #3C6839  x=16

@Composable
private fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(132.dp)
            .height(145.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaLightBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Product image — 85×65dp, top-padding=16dp, horizontally centered
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 85.dp, height = 65.dp)
                    .align(Alignment.CenterHorizontally),
            )

            // Text area — gap=8dp below image, padding-start=16dp
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = FormLabel,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Text(
                text = String.format(Locale.US, "₱%,.0f x %d mo", product.price, product.installments),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = ProductPriceGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════════════════════════

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun HomeScreenPreview() {
    LendlyAppTheme(dynamicColor = false) {
        HomeScreenContent(
            data = HomeData(
                balance = 2500.00,
                unpaidLoans = listOf(
                    Loan(id = "1", companyName = "Nike Inc.",  amount = 400.0,  description = "Fees of Febuary", lenderLogo = "https://logo.clearbit.com/nike.com"),
                    Loan(id = "2", companyName = "Apple Inc.", amount = 1500.0, description = "Fees of March",   lenderLogo = "https://logo.clearbit.com/apple.com"),
                ),
                recommendedProducts = listOf(
                    Product(id = "1", name = "iPhone 12 Pro",      price = 1200.0, installments = 24, imageUrl = "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero"),
                    Product(id = "2", name = "AirPods Pro",        price = 600.0,  installments = 24, imageUrl = "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MME73"),
                    Product(id = "3", name = "Samsung Galaxy S21", price = 1000.0, installments = 24, imageUrl = "https://images.samsung.com/is/image/samsung/ph-feature-made-for-the-epic-in-everyday-418978951"),
                ),
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 393)
@Composable
private fun BalanceCardPreview() {
    LendlyAppTheme(dynamicColor = false) {
        BalanceCard(
            balance = 2500.00,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 393)
@Composable
private fun LoanCardPreview() {
    LendlyAppTheme(dynamicColor = false) {
        LoanCard(
            loan = Loan(id = "1", companyName = "Nike Inc.", amount = 400.0, description = "Fees of Febuary", lenderLogo = "https://logo.clearbit.com/nike.com"),
            modifier = Modifier.padding(16.dp),
        )
    }
}
