package com.example.lendlyapp.ui.screens.loans

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.ui.theme.*
import com.example.lendlyapp.viewmodel.LoanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanFormScreen(
    viewModel: LoanViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val amount by viewModel.amountInput
    val installments by viewModel.installmentsInput
    val purpose by viewModel.purposeInput

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Loan", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Info, contentDescription = "Info")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Button(
                onClick = { 
                    viewModel.applyForLoan()
                    onSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Get This Loan", color = OnPrimaryGreen, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Please provide your details for your loan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                color = FigmaLightText
            )
            
            Text("Please provide your details for your loan", color = SubtitleGray, fontSize = 14.sp)

            // Step 1
            StepLabel(1, "Enter loan amount")
            Text(
                text = "₱${if(amount.isEmpty()) "0.00" else amount}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* Show keyboard/dialog */ }
            )
            HorizontalDivider(color = FigmaLightBg)

            // Step 2
            StepLabel(2, "Select an installment plan")
            InstallmentPlanCard("6 Months", "₱ 982.12/mo", "2.99% Interest", isSelected = installments == "6") {
                viewModel.onInstallmentsChange("6")
            }

            // Step 3
            StepLabel(3, "Select your loan purpose")
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SubtitleGray.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(purpose, fontWeight = FontWeight.Medium)
                    // Icon placeholder
                }
            }

            // Summary
            SummarySection(amount)
        }
    }
}

@Composable
fun StepLabel(step: Int, label: String) {
    Column {
        Surface(color = FigmaMintSplash, shape = RoundedCornerShape(4.dp)) {
            Text("Step $step", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 11.sp, color = FigmaOliveGreen, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun InstallmentPlanCard(title: String, price: String, interest: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if(isSelected) Color.White else FigmaLightBg),
        border = if(isSelected) BorderStroke(1.dp, FigmaNeonGreen) else null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(interest, color = SubtitleGray, fontSize = 12.sp)
            }
            Text(price, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SummarySection(amount: String) {
    Column(modifier = Modifier.fillMaxWidth().background(FigmaLightBg, RoundedCornerShape(16.dp)).padding(16.dp)) {
        Text("Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        SummaryRow("Loan Amount", "PHP ${amount.ifEmpty { "0.00" }}")
        SummaryRow("3% Processing Fee", "-150.00")
        Divider(modifier = Modifier.padding(vertical = 12.dp))
        SummaryRow("Total amount to Receive", "₱ ${amount.ifEmpty { "0.00" }}", isBold = true)
        SummaryRow("Lender", "null")
        Text("What is this?", color = FigmaOliveGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = SubtitleGray, fontSize = 14.sp)
        Text(value, fontWeight = if(isBold) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
    }
}
