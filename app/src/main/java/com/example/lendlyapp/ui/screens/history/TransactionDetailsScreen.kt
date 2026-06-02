package com.example.lendlyapp.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.model.Transaction
import com.example.lendlyapp.ui.shared.LendlyTopBar
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOrangeAccent
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.OtpLinkTeal
import com.example.lendlyapp.ui.theme.SectionDividerGray
import com.example.lendlyapp.ui.theme.SubtitleGray
import com.example.lendlyapp.viewmodel.TransactionDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun TransactionDetailsScreen(
    onBack: () -> Unit = {},
    viewModel: TransactionDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        LendlyTopBar(
            onNavigationClick = onBack,
            trailingContent = {
                IconButton(onClick = {}, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = FigmaLightText, modifier = Modifier.size(24.dp))
                }
                IconButton(onClick = {}, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = FigmaLightText, modifier = Modifier.size(24.dp))
                }
            },
        )
        when (val state = uiState) {
            is TransactionDetailsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = FigmaNeonGreen)
                }
            }
            is TransactionDetailsUiState.Success -> {
                TransactionDetailsContent(transaction = state.transaction)
            }
            is TransactionDetailsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = FigmaLightText)
                }
            }
        }
    }
}

@Composable
private fun TransactionDetailsContent(transaction: Transaction) {
    val isPositive = transaction.amount > 0
    val icon = if (isPositive) Icons.Default.Add else Icons.Default.ArrowUpward
    val iconBg = if (isPositive) FigmaNeonGreen else FigmaOrangeAccent
    val partner = transaction.title.extractPartner()
    val directionLabel = if (isPositive) "From" else "To"
    val typeLabel = transaction.type.toDetailsLabel()
    val chipLabel = transaction.type.toTypeChipLabel()
    val amount = String.format(Locale.US, "%,.2f PHP", abs(transaction.amount))
    val formattedDate = transaction.date.toFullDateTime()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = typeLabel, fontSize = 14.sp, fontFamily = InterFamily, color = SubtitleGray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amount,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = InterFamily,
            color = FigmaLightText,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "$directionLabel $partner", fontSize = 14.sp, fontFamily = InterFamily, color = SubtitleGray)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .border(1.dp, SubtitleGray, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
        ) {
            Text(text = chipLabel, fontSize = 12.sp, fontFamily = InterFamily, color = FigmaLightText)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "Transaction Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = FigmaLightText,
                )
                Spacer(modifier = Modifier.height(16.dp))
                DetailRow(label = "Date & Time", value = formattedDate)
                HorizontalDivider(color = SectionDividerGray, modifier = Modifier.padding(vertical = 12.dp))
                DetailRow(
                    label = "Transaction Number",
                    value = transaction.referenceNumber,
                    valueColor = OtpLinkTeal,
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Didn't find what you were looking for?",
            fontSize = 13.sp,
            fontFamily = InterFamily,
            color = SubtitleGray,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Go to Help Center",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = OtpLinkTeal,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = FigmaLightText,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, fontSize = 14.sp, fontFamily = InterFamily, color = SubtitleGray)
        Text(text = value, fontSize = 14.sp, fontFamily = InterFamily, color = valueColor)
    }
}

private fun String.extractPartner(): String {
    val parts = split(" — ", " — ", " - ")
    return if (parts.size >= 2) parts.last().trim() else this
}

private fun String.toFullDateTime(): String = try {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    sdf.isLenient = false
    val parsed = sdf.parse(this) ?: return this
    SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US).format(parsed)
} catch (e: Exception) { this }

private fun String.toDetailsLabel(): String = when (this) {
    "LOAN_PAYMENT"      -> "Paid this month"
    "CASH_IN"           -> "Cash In"
    "LOAN_DISBURSEMENT" -> "Loan received"
    else                -> replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}

private fun String.toTypeChipLabel(): String = when (this) {
    "LOAN_PAYMENT"      -> "Paid Bills"
    "CASH_IN"           -> "Cash In"
    "LOAN_DISBURSEMENT" -> "Loan"
    else                -> replace("_", " ")
}
