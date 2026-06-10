package com.example.lendlyapp.ui.screens.loans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lendlyapp.ui.shared.LendlyTopBar
import com.example.lendlyapp.ui.theme.*
import com.example.lendlyapp.viewmodel.LoanUiState
import com.example.lendlyapp.viewmodel.LoanViewModel

// ─── Assets ───────────────────────────────────────────────────────────────────
private const val IMG_APPLE_LOGO = "https://www.figma.com/api/mcp/asset/966f50b5-4544-4adf-a6be-c63c34e288a9"

// ─── Modelos de datos locales ─────────────────────────────────────────────────
data class ActiveLoanItem(
    val merchant: String,
    val feePeriod: String,
    val productName: String,
    val amount: String,
    val logoUrl: String = IMG_APPLE_LOGO
)

data class RecentLoanItem(
    val date: String,
    val merchant: String,
    val productName: String,
    val status: String
)

@Composable
fun ActiveLoanScreen(
    viewModel: LoanViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState

    val activeLoans: List<ActiveLoanItem>
    val recentLoans: List<RecentLoanItem>
    when (val state = uiState) {
        is LoanUiState.Success -> {
            activeLoans = state.loans
                .filter { it.status == "ACTIVE" }
                .map { loan ->
                    ActiveLoanItem(
                        merchant = loan.companyName,
                        feePeriod = loan.nextPaymentDate ?: "",
                        productName = loan.purpose,
                        amount = "₱ ${loan.amount}",
                        logoUrl = loan.lenderLogo ?: IMG_APPLE_LOGO
                    )
                }
            recentLoans = state.loans
                .filter { it.status != "ACTIVE" }
                .map { loan ->
                    RecentLoanItem(
                        date = loan.nextPaymentDate ?: "",
                        merchant = loan.companyName,
                        productName = loan.purpose,
                        status = loan.status
                    )
                }
        }
        else -> {
            activeLoans = emptyList()
            recentLoans = emptyList()
        }
    }

    Scaffold(
        topBar = {
            LendlyTopBar(
                onNavigationClick = onBack,
                title = "",
                trailingContent = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendar",
                            tint = FigmaLightText
                        )
                    }
                }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Título
            Text(
                text = "Active loans",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = FigmaLightText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            HorizontalDivider(color = FigmaMintSplash)
            Spacer(Modifier.height(16.dp))

            // Sección "Present"
            LoanSectionHeader(title = "Present")
            HorizontalDivider(color = FigmaMintSplash, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))

            activeLoans.forEach { item ->
                ActiveLoanRow(item = item)
            }

            Spacer(Modifier.height(24.dp))

            // Sección "Recent Loans"
            LoanSectionHeader(title = "Recent Loans")
            HorizontalDivider(color = FigmaMintSplash, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))

            recentLoans.forEach { item ->
                RecentLoanRow(item = item)
            }

            Spacer(Modifier.height(34.dp))
        }
    }
}

@Composable
private fun LoanSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = SubtitleGray,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun ActiveLoanRow(item: ActiveLoanItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.logoUrl,
            contentDescription = item.merchant,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.merchant, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
            Text(item.productName, fontSize = 16.sp, color = OnPrimaryGreen, fontWeight = FontWeight.Medium)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(item.feePeriod, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
            Text(item.amount, fontSize = 16.sp, color = OnPrimaryGreen, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun RecentLoanRow(item: RecentLoanItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(FigmaLightBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Paid",
                tint = FigmaLightText,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.date, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
            Text(item.productName, fontSize = 16.sp, color = OnPrimaryGreen, fontWeight = FontWeight.Medium)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(item.merchant, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
            Text(item.status, fontSize = 16.sp, color = OnPrimaryGreen, fontWeight = FontWeight.SemiBold)
        }
    }
}

private fun sampleActiveLoans() = listOf(
    ActiveLoanItem("Apple Inc.", "Fees of february", "iPhone 15 Pro Max", "1,2555 PHP"),
    ActiveLoanItem("Apple Inc.", "Fees of february", "iPhone 15 Pro Max", "1,2555 PHP"),
    ActiveLoanItem("Apple Inc.", "Fees of february", "iPhone 15 Pro Max", "1,2555 PHP")
)

private fun sampleRecentLoans() = listOf(
    RecentLoanItem("02/08/2024", "Apple Inc.", "iPhone 15 Pro Max", "Paid"),
    RecentLoanItem("02/08/2024", "Apple Inc.", "iPhone 15 Pro Max", "Paid"),
    RecentLoanItem("02/08/2024", "Apple Inc.", "iPhone 15 Pro Max", "Paid")
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ActiveLoanScreenPreview() {
    LendlyAppTheme {
        ActiveLoanScreen()
    }
}
