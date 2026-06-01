package com.example.lendlyapp.ui.screens.loans

import androidx.compose.foundation.background
 import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.lendlyapp.ui.theme.*

// ─── Assets (Figma URLs) ──────────────────────────────────────────────────────
private const val IMG_BANNER = "https://www.figma.com/api/mcp/asset/5cf348c0-c623-49d6-93db-55ba16e06994"
private const val IMG_ALARM  = "https://www.figma.com/api/mcp/asset/0f8a1cfc-470e-4aa1-ab1d-4f674d716a39"
private const val IMG_HOW_1  = "https://www.figma.com/api/mcp/asset/5ad176fb-45ef-4c95-b1bc-ba9b176f5c98"
private const val IMG_HOW_2  = "https://www.figma.com/api/mcp/asset/5a2b0b87-936a-4c84-891b-db5bfba3d3f4"
private const val IMG_HOW_3  = "https://www.figma.com/api/mcp/asset/7c49b657-b290-4bd7-949e-ffc9b6bdb64f"
private const val IMG_HOW_4  = "https://www.figma.com/api/mcp/asset/58f67a5d-2968-487f-b230-eda17645074c"

@Composable
fun LoanInfoScreen(
    onNavigateToForm: () -> Unit
) {
    // Note: No Scaffold here if the TopBar and BottomBar are managed by MainScaffold
    // But since the design has a specific TopBar for this screen, we can use a Column.
    // If we want the button to stay fixed at the bottom, we use a Scaffold without a bottom bar (it will be in MainScaffold).
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // App Bar
        LoanTopAppBar()

        Spacer(modifier = Modifier.height(8.dp))

        // Banner principal verde
        LoanBannerCard(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(12.dp))

        // Info card (monto, tasa, plazo)
        LoanInfoCard(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(32.dp))

        // "How it works"
        Text(
            text = "How it works",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = FigmaLightText,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HowItWorksGrid(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // Botón "Get This Loan"
        Button(
            onClick = onNavigateToForm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen)
        ) {
            Text(
                text = "Get This Loan",
                color = OnPrimaryGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LoanTopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.Black
            )
        }
        // Logo placeholder
        Text(
            text = "Rayland",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = FigmaOliveGreen
        )
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun LoanBannerCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(196.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(FigmaNeonGreen)
    ) {
        // Imagen de fondo (persona)
        AsyncImage(
            model = IMG_BANNER,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        )
        // Texto superpuesto
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            // Chip "Limited Time Offer"
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(FigmaMintSplash)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(model = IMG_ALARM, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Limited Time Offer", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = OnPrimaryGreen)
            }
            Spacer(Modifier.height(12.dp))
            Text("Safe and\nsecure loans", fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = OnPrimaryGreen, lineHeight = 36.sp)
            Text("All here in Rayland", fontSize = 14.sp, color = OnPrimaryGreen.copy(alpha = 0.8f))
        }
    }
}

@Composable
private fun LoanInfoCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(FigmaLightBg)
            .padding(24.dp)
    ) {
        Text("You can borrow up to", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        Spacer(Modifier.height(4.dp))
        Text("₱ 30,000.00", fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = FigmaOliveGreen)
        Text("*Subject to evaluation", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = FigmaLightBg.copy(alpha = 0.8f)) // Or use a specific divider color if available
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Loan Details", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = FigmaOliveGreen)
            Text(
                text = "What is this?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = FigmaOliveGreen,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = FigmaLightBg.copy(alpha = 0.8f))
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LoanStatItem(label = "Payable in", value = "6 - 12", unit = "months")
            VerticalDivider(modifier = Modifier.height(60.dp), color = FigmaLightBg.copy(alpha = 0.8f))
            LoanStatItem(label = "Interest Rate", value = "1.99%", unit = "ave per mo.")
            VerticalDivider(modifier = Modifier.height(60.dp), color = FigmaLightBg.copy(alpha = 0.8f))
            LoanStatItem(label = "Process Fee", value = "3%", unit = "as low as")
        }
    }
}

@Composable
private fun LoanStatItem(label: String, value: String, unit: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Text(value, fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = FigmaOliveGreen)
        Text(unit, fontSize = 11.sp, color = SubtitleGray)
    }
}

@Composable
private fun HowItWorksGrid(modifier: Modifier = Modifier) {
    val items = listOf(
        Triple(IMG_HOW_1, "Keep your credit score high", "The offered loan amount is based on your credit score"),
        Triple(IMG_HOW_2, "Get instant approval", "Everything we need to process is already in the application"),
        Triple(IMG_HOW_3, "Easy payments option available", "Skip the queue and pay your due on the application"),
        Triple(IMG_HOW_4, "Safe and secure", "Rayland is working with trusted partners to provide this services")
    )
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in items.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                HowItWorksCard(image = items[i].first, title = items[i].second, description = items[i].third, modifier = Modifier.weight(1f))
                if (i + 1 < items.size) {
                    HowItWorksCard(image = items[i+1].first, title = items[i+1].second, description = items[i+1].third, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun HowItWorksCard(image: String, title: String, description: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(260.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFEAEAEA), RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = image, 
            contentDescription = null, 
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(12.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = FigmaLightText)
        Spacer(Modifier.height(4.dp))
        Text(description, fontSize = 12.sp, color = FormLabel.copy(alpha = 0.8f), lineHeight = 16.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoanInfoScreenPreview() {
    LendlyAppTheme {
        LoanInfoScreen(onNavigateToForm = {})
    }
}
