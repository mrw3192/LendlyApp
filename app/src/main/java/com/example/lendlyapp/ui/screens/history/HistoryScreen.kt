package com.example.lendlyapp.ui.screens.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.model.Loan
import com.example.lendlyapp.model.Transaction
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.theme.FigmaDarkForest
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaMintSplash
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOrangeAccent
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.ProductPriceGreen
import com.example.lendlyapp.ui.theme.SearchBorderGray
import com.example.lendlyapp.ui.theme.SubtitleGray
import com.example.lendlyapp.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun HistoryScreen(
    onNavigateToTransactionDetails: (String) -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is HistoryUiState.Loading -> HistoryLoadingContent()
        is HistoryUiState.Success -> HistoryContent(
            data = state.data,
            onNavigateToTransactionDetails = onNavigateToTransactionDetails,
        )
        is HistoryUiState.Error -> HistoryErrorContent(message = state.message)
    }
}

@Composable
private fun HistoryLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = FigmaNeonGreen)
    }
}

@Composable
private fun HistoryErrorContent(message: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = message, color = FigmaLightText)
    }
}

@Composable
private fun HistoryContent(
    data: HistoryData,
    onNavigateToTransactionDetails: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Type", "Balance", "Paid Bills")

    val filteredTransactions = data.transactions
        .filter { tx ->
            searchQuery.isEmpty() || tx.title.contains(searchQuery, ignoreCase = true)
        }
        .filter { tx ->
            when (selectedFilter) {
                "Balance"    -> tx.amount > 0
                "Paid Bills" -> tx.type == "LOAN_PAYMENT"
                else         -> true
            }
        }

    val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val yesterdayKey = Calendar.getInstance()
        .apply { add(Calendar.DAY_OF_YEAR, -1) }
        .let { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(it.time) }

    val groupedTransactions = filteredTransactions
        .mapNotNull { tx -> tx.date.toDateKey()?.let { key -> key to tx } }
        .groupBy { it.first }
        .entries
        .sortedByDescending { it.key }
        .map { (key, pairs) ->
            val label = when (key) {
                todayKey     -> "Today"
                yesterdayKey -> "Yesterday"
                else         -> key.toDisplayDate()
            }
            label to pairs.map { it.second }
        }

    val paidLoans = data.loans.filter { it.status == "PAID" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
    ) {
        HistoryTopBar()
        LazyColumn {
            item {
                Text(
                    text = "History",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = FigmaLightText,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                )
            }
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    filters.forEach { filter ->
                        HistoryFilterChip(
                            label = filter,
                            isSelected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            groupedTransactions.forEach { (dateLabel, transactions) ->
                item {
                    Text(
                        text = dateLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = InterFamily,
                        color = SubtitleGray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
                items(transactions) { tx ->
                    TransactionRow(
                        transaction = tx,
                        onClick = { onNavigateToTransactionDetails(tx.id) },
                    )
                }
            }
            if (paidLoans.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Recent Loans",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = InterFamily,
                        color = SubtitleGray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
                items(paidLoans) { loan ->
                    LoanRow(loan = loan)
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun HistoryTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {}, modifier = Modifier.size(48.dp)) {
            Icon(Icons.Default.Person, contentDescription = null, tint = FigmaLightText, modifier = Modifier.size(24.dp))
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            LendlyLogo(size = DpSize(width = 58.dp, height = 20.dp))
        }
        IconButton(onClick = {}, modifier = Modifier.size(48.dp)) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = FigmaLightText, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, SearchBorderGray, RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = SubtitleGray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                fontFamily = InterFamily,
                color = FigmaLightText,
            ),
            decorationBox = { inner ->
                if (query.isEmpty()) {
                    Text(text = "Search...", fontSize = 14.sp, fontFamily = InterFamily, color = SubtitleGray)
                }
                inner()
            },
        )
    }
}

@Composable
private fun HistoryFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (isSelected) FigmaNeonGreen else Color.White
    val textColor = if (isSelected) OnPrimaryGreen else FigmaLightText
    val borderColor = if (isSelected) Color.Transparent else Color.LightGray

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = bg,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.height(32.dp),
        interactionSource = remember { MutableInteractionSource() },
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = textColor,
            )
        }
    }
}

@Composable
private fun TransactionRow(
    transaction: Transaction,
    onClick: () -> Unit,
) {
    val isPositive = transaction.amount > 0
    val icon = if (isPositive) Icons.Default.Add else Icons.Default.ArrowUpward
    val iconBg = if (isPositive) FigmaMintSplash else Color(0xFFFFE5D3)
    val iconTint = if (isPositive) FigmaDarkForest else FigmaOrangeAccent
    val time = transaction.date.toFormattedTime()
    val company = transaction.title.extractPartner()
    val label = transaction.type.toHistoryLabel()
    val amount = String.format(Locale.US, "%,.2f PHP", abs(transaction.amount))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = time,
            fontSize = 11.sp,
            fontFamily = InterFamily,
            color = SubtitleGray,
            modifier = Modifier.width(52.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            color = FigmaLightText,
            modifier = Modifier.weight(1f),
        )
        Column(horizontalAlignment = Alignment.End) {
            Text(text = company, fontSize = 12.sp, fontFamily = InterFamily, color = SubtitleGray)
            Text(
                text = amount,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = if (isPositive) ProductPriceGreen else FigmaLightText,
            )
        }
    }
}

@Composable
private fun LoanRow(loan: Loan) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(FigmaMintSplash),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = FigmaDarkForest, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = loan.purpose.ifEmpty { loan.companyName },
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = FigmaLightText,
            )
            Text(text = loan.companyName, fontSize = 12.sp, fontFamily = InterFamily, color = SubtitleGray)
        }
        Text(
            text = loan.status.lowercase().replaceFirstChar { it.uppercase() },
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = ProductPriceGreen,
        )
    }
}

private fun String.toDateKey(): String? = try {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    sdf.isLenient = false
    val parsed = sdf.parse(this) ?: return null
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(parsed)
} catch (e: Exception) { null }

private fun String.toFormattedTime(): String = try {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    sdf.isLenient = false
    val parsed = sdf.parse(this) ?: return ""
    SimpleDateFormat("h:mm a", Locale.US).format(parsed)
} catch (e: Exception) { "" }

private fun String.toDisplayDate(): String = try {
    val inSdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val outSdf = SimpleDateFormat("MMMM d, yyyy", Locale.US)
    outSdf.format(inSdf.parse(this)!!)
} catch (e: Exception) { this }

private fun String.extractPartner(): String {
    val parts = split(" — ", " — ", " - ")
    return if (parts.size >= 2) parts.last().trim() else this
}

private fun String.toHistoryLabel(): String = when (this) {
    "LOAN_PAYMENT"      -> "Paid this month"
    "CASH_IN"           -> "Added"
    "LOAN_DISBURSEMENT" -> "Loan received"
    else                -> replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}
