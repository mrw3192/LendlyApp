package com.example.lendlyapp.ui.screens.home


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.example.lendlyapp.ui.shared.ProductCarousel
import com.example.lendlyapp.ui.shared.ProductCarouselStyle
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOliveSeed
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.SubtitleGray
import com.example.lendlyapp.viewmodel.HomeViewModel
import java.util.Locale


@Composable
fun HomeScreen(
    onNavigateToCashIn: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is HomeUiState.Loading -> HomeLoadingContent()
        is HomeUiState.Success -> HomeScreenContent(data = state.data, onNavigateToCashIn = onNavigateToCashIn)
        is HomeUiState.Error   -> HomeErrorContent(message = state.message)
    }
}


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

@Composable
private fun HomeScreenContent(
    data: HomeData,
    onNavigateToCashIn: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = FigmaLightText,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
        BalanceCard(
            balance = data.balance,
            onCashIn = onNavigateToCashIn,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

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

        Spacer(modifier = Modifier.height(32.dp))
        ProductCarousel(
            title = "Recommended For You",
            products = data.recommendedProducts,
            style = ProductCarouselStyle.Home,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun BalanceCard(
    balance: Double,
    onCashIn: () -> Unit = {},
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "AVAILABLE BALANCE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = FormLabel,
                )
                CashInButton(onClick = onCashIn)
            }

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
                    Product(id = "1", name = "iPhone 12 Pro",      shortName = "iPhone 12 Pro", imageAsset = "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero", monthlyPayment = 1200.0, totalPrice = 28800.0, category = "", brand = "", installmentMonths = 24, currency = "₱", isAvailable = true, description = "", rating = 0.0, reviewCount = 0),
                    Product(id = "2", name = "AirPods Pro",        shortName = "AirPods Pro",   imageAsset = "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MME73",                     monthlyPayment = 600.0,  totalPrice = 14400.0, category = "", brand = "", installmentMonths = 24, currency = "₱", isAvailable = true, description = "", rating = 0.0, reviewCount = 0),
                    Product(id = "3", name = "Samsung Galaxy S21", shortName = "Samsung S21",   imageAsset = "https://images.samsung.com/is/image/samsung/ph-feature-made-for-the-epic-in-everyday-418978951", monthlyPayment = 1000.0, totalPrice = 24000.0, category = "", brand = "", installmentMonths = 24, currency = "₱", isAvailable = true, description = "", rating = 0.0, reviewCount = 0),
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
