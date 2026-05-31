package com.example.lendlyapp.ui.screens.loans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.lendlyapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanInfoScreen(
    onNavigateToForm: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    // Logo placeholder
                    Box(modifier = Modifier.size(40.dp).background(FigmaNeonGreen, RoundedCornerShape(8.dp)))
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = FigmaLightText)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = FigmaLightText)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FigmaLightBg
                )
            )
        },
        bottomBar = {
            // Button at the bottom
            Button(
                onClick = onNavigateToForm,
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
        containerColor = FigmaLightBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Banner
            BannerSection()

            // Borrow section
            BorrowLimitSection()

            // Details
            LoanDetailsSection()

            // How it works
            Text(
                text = "How it works",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = FigmaLightText
            )
            
            HowItWorksGrid()
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(FigmaNeonGreen)
            .padding(24.dp)
    ) {
        Column {
            Surface(
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("⏰ Limited Time Offer", fontSize = 12.sp, color = FigmaLightText)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Safe and\nsecure loans",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                color = FigmaLightText
            )
            Text(text = "All here in Rayland", fontSize = 14.sp, color = FigmaLightText.copy(alpha = 0.7f))
        }
        // Image placeholder
    }
}

@Composable
fun BorrowLimitSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FigmaLightSurface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("You can borrow up to", color = SubtitleGray, fontSize = 14.sp)
            Text(
                text = "₱ 30,000.00",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FigmaLightText
            )
            Text("*Subject to evaluation", color = SubtitleGray, fontSize = 12.sp)
        }
    }
}

@Composable
fun LoanDetailsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FigmaLightSurface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Loan Details", fontWeight = FontWeight.Bold, color = FigmaLightText)
                Text("What is this?", color = FigmaOliveGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                DetailItem("Payable in", "6 - 12", "months")
                DetailItem("Interest Rate", "1.99%", "ave per mo.")
                DetailItem("Process Fee", "3%", "as low as")
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, sub: String) {
    Column {
        Text(label, color = SubtitleGray, fontSize = 11.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FigmaLightText)
        Text(sub, color = SubtitleGray, fontSize = 10.sp)
    }
}

@Composable
fun HowItWorksGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            HowItWorkCard(Modifier.weight(1f), "Keep your credit score high", "The offered loan amount is based on your credit score")
            HowItWorkCard(Modifier.weight(1f), "Get instant approval", "Everything we need to process si already in the application")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            HowItWorkCard(Modifier.weight(1f), "Easy payments option available", "Skip the queue and pay your due on the application")
            HowItWorkCard(Modifier.weight(1f), "Safe and secure", "Rayland is working with trusted partners to provide this services")
        }
    }
}

@Composable
fun HowItWorkCard(modifier: Modifier, title: String, desc: String) {
    Card(
        modifier = modifier.height(180.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaLightSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.size(40.dp).background(FigmaLightBg, RoundedCornerShape(8.dp)))
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 16.sp, color = FigmaLightText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, fontSize = 10.sp, color = SubtitleGray, lineHeight = 14.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoanInfoScreenPreview() {
    LendlyAppTheme {
        LoanInfoScreen(onNavigateToForm = {})
    }
}
