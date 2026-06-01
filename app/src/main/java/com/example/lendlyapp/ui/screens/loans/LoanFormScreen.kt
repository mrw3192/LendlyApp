package com.example.lendlyapp.ui.screens.loans

 import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.ui.theme.*
import com.example.lendlyapp.viewmodel.LoanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanFormScreen(
    viewModel: LoanViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val amount by viewModel.amountInput
    val selectedPlan by viewModel.installmentsInput
    val selectedPurpose by viewModel.purposeInput
    var purposeExpanded by remember { mutableStateOf(false) }

    val purposes = listOf("Educational", "Medical", "Business", "Personal", "Emergency")
    val plans = listOf(
        Triple("6", "2.99% Interest", "₱ 982.12/mo"),
        Triple("12", "3.99% Interest", "₱ 510.00/mo")
    )

    Scaffold(
        topBar = {
            LoanFormTopBar(onBack = onBack)
        },
        bottomBar = {
            Column(modifier = Modifier.background(Color.White)) {
                Button(
                    onClick = {
                        viewModel.applyForLoan()
                        onSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen)
                ) {
                    Text("Get This Loan", color = OnPrimaryGreen, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .padding(horizontal = 130.dp)
                        .background(Color.Black, CircleShape)
                )
                Spacer(Modifier.height(8.dp))
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Título
            Text(
                text = "Please provide your details for your loan",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                lineHeight = 32.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Please provide your details for your loan",
                fontSize = 12.sp,
                color = SubtitleGray
            )

            Spacer(Modifier.height(32.dp))

            // Step 1 — Monto
            StepLabel(text = "Step 1")
            Spacer(Modifier.height(8.dp))
            Text("Enter loan amount", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { viewModel.onAmountChange(it) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnPrimaryGreen
                ),
                singleLine = true,
                prefix = { Text("₱", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = OnPrimaryGreen) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FigmaNeonGreen,
                    unfocusedBorderColor = FigmaMintSplash
                )
            )

            Spacer(Modifier.height(32.dp))

            // Step 2 — Plan de cuotas
            StepLabel(text = "Step 2")
            Spacer(Modifier.height(8.dp))
            Text("Select an installment plan", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(Modifier.height(8.dp))

            plans.forEach { (label, interest, monthly) ->
                val isSelected = selectedPlan == label
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (isSelected) FigmaNeonGreen else FigmaMintSplash,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable { viewModel.onInstallmentsChange(label) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("$label Months", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = OnPrimaryGreen)
                        Text(interest, fontSize = 12.sp, color = OnPrimaryGreen)
                    }
                    Text(monthly, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = OnPrimaryGreen)
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(24.dp))

            // Step 3 — Propósito
            StepLabel(text = "Step 3")
            Spacer(Modifier.height(8.dp))
            Text("Select your loan purpose", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = purposeExpanded,
                onExpandedChange = { purposeExpanded = !purposeExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPurpose,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FigmaMintSplash,
                        unfocusedBorderColor = FigmaMintSplash
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnPrimaryGreen
                    )
                )
                ExposedDropdownMenu(
                    expanded = purposeExpanded,
                    onDismissRequest = { purposeExpanded = false }
                ) {
                    purposes.forEach { purpose ->
                        DropdownMenuItem(
                            text = { Text(purpose) },
                            onClick = {
                                viewModel.onPurposeChange(purpose)
                                purposeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = FigmaLightBg, thickness = 8.dp)
            Spacer(Modifier.height(24.dp))

            // Resumen
            Text("Summary", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(Modifier.height(12.dp))

            SummaryRow(label = "Loan Amount", value = "PHP ${amount.ifEmpty { "0.00" }}")
            Spacer(Modifier.height(4.dp))
            SummaryRow(label = "3% Processing Fee", value = "-150.00")

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = FigmaMintSplash)
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Total amount to Receive", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = FormLabel)
                Text("₱ ${amount.ifEmpty { "0.00" }}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = OnPrimaryGreen)
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Lender", fontSize = 12.sp, color = FormLabel)
                    Text("What is this?", fontSize = 12.sp, color = OnPrimaryGreen, textDecoration = TextDecoration.Underline)
                }
                Text("null", fontSize = 12.sp, color = SubtitleGray)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StepLabel(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FigmaMintSplash)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, fontSize = 12.sp, color = SubtitleGray)
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = FormLabel)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoanFormTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Loan", fontWeight = FontWeight.Medium) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Info, contentDescription = "Info")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoanFormScreenPreview() {
    LendlyAppTheme {
        LoanFormScreen()
    }
}
