package com.example.lendlyapp.ui.screens.loans

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.*

data class TransactionDetails(
    val amount: String = "2,000.00 PHP",
    val source: String = "From Apple Inc.",
    val monthlyFee: String = "₱982.12",
    val interest: String = "2.99%",
    val installmentPlan: String = "6 Months",
    val dateTime: String = "Jul 15, 2024 9:12 AM",
    val transactionNumber: String = "#200412312551"
)

@Composable
fun LoanSuccessScreen(
    details: TransactionDetails = TransactionDetails(),
    onClose: () -> Unit = {},
    onDone: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(bottom = 16.dp)
            ) {
                Button(
                    onClick = onDone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen)
                ) {
                    Text("Done", color = OnPrimaryGreen, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Spacer(Modifier.height(8.dp))
                // Home indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .padding(horizontal = 130.dp)
                        .background(Color.Black, CircleShape)
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header con fondo suave
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FigmaLightBg)
            ) {
                // Top App Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón cerrar
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(FigmaMintSplash),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onClose) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    // Acciones derecha
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = FigmaLightText)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = FigmaLightText)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Ícono central verde con "+"
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(FigmaNeonGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Success",
                            tint = OnPrimaryGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Added to your account", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
                    Spacer(Modifier.height(4.dp))
                    Text(details.amount, fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = FigmaLightText)
                    Spacer(Modifier.height(4.dp))
                    Text(details.source, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
                    Spacer(Modifier.height(12.dp))

                    // Chip "Loan Amount"
                    Box(
                        modifier = Modifier
                            .border(1.dp, SubtitleGray, RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text("Loan Amount", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = SubtitleGray)
                    }
                    Spacer(Modifier.height(32.dp))
                }
            }

            // Transaction Details
            Spacer(Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Transaction Details", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = FigmaLightText)
                Spacer(Modifier.height(16.dp))

                TransactionDetailRow(label = "Monthly Fee", value = details.monthlyFee)
                Spacer(Modifier.height(12.dp))
                TransactionDetailRow(label = "Interest", value = details.interest)
                Spacer(Modifier.height(12.dp))
                TransactionDetailRow(label = "Installment plan", value = details.installmentPlan)
                Spacer(Modifier.height(12.dp))
                TransactionDetailRow(label = "Date & Time", value = details.dateTime)
                Spacer(Modifier.height(12.dp))

                // Número de transacción (link)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Transaction Number", fontSize = 16.sp, color = SubtitleGray)
                    Text(
                        text = details.transactionNumber,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FigmaOliveGreen,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(
                color = FigmaMintSplash,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))

            // "Need help?"
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Need help?", fontSize = 14.sp, color = SubtitleGray)
                Text(
                    text = "Go to Help Center",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = FigmaOliveGreen,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TransactionDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp, color = SubtitleGray)
        Text(value, fontSize = 16.sp, color = SubtitleGray)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoanSuccessScreenPreview() {
    LendlyAppTheme {
        LoanSuccessScreen()
    }
}
