package com.example.lendlyapp.ui.screens.cashin

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.FigmaDarkForest
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOliveGreen
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import com.example.lendlyapp.ui.shared.LendlyTopBar
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.SectionDividerGray
import com.example.lendlyapp.ui.theme.SubtitleGray

@Composable
fun SuccessfulTransactionScreen(
    partnerName: String,
    amount: String,
    onClose: () -> Unit = {},
    onDone: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        LendlyTopBar(
            onNavigationClick = onClose,
            navigationIcon = Icons.Default.Close,
            trailingContent = {
                IconButton(onClick = {}, modifier = Modifier.size(48.dp)) {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = null, tint = FigmaLightText, modifier = Modifier.size(24.dp))
                }
                IconButton(onClick = {}, modifier = Modifier.size(48.dp)) {
                    Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null, tint = FigmaLightText, modifier = Modifier.size(24.dp))
                }
            },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            HeroSection(partnerName = partnerName, amount = amount)

            HorizontalDivider(color = SectionDividerGray, thickness = 1.dp)

            TransactionDetailsSection()

            HorizontalDivider(color = SectionDividerGray, thickness = 1.dp)

            HelpSection()

            Spacer(modifier = Modifier.height(24.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen),
            ) {
                Text(
                    text = "Done",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = OnPrimaryGreen,
                )
            }
        }
    }
}

@Composable
private fun HeroSection(partnerName: String, amount: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(FigmaNeonGreen),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = FigmaDarkForest,
                modifier = Modifier.size(32.dp),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Added to your account",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = SubtitleGray,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "${if (amount.isNotEmpty()) amount else "0.00"} PHP",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "From $partnerName",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = SubtitleGray,
                    textAlign = TextAlign.Center,
                )
            }

            Box(
                modifier = Modifier
                    .border(1.dp, SubtitleGray, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
            ) {
                Text(
                    text = "Cash-In",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = SubtitleGray,
                )
            }
        }
    }
}

@Composable
private fun TransactionDetailsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Transaction Details",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = FigmaLightText,
        )

        DetailRow(label = "Transfer Fee", value = "-₱15.00", valueColor = SubtitleGray)
        DetailRow(label = "Date & Time", value = "Jul 15, 2024 9:12 AM", valueColor = SubtitleGray)
        DetailRow(label = "Transaction Number", value = "#200412312551", valueColor = FigmaOliveGreen)
    }
}

@Composable
private fun DetailRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = InterFamily,
            color = SubtitleGray,
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = InterFamily,
            color = valueColor,
        )
    }
}

@Composable
private fun HelpSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "Need help?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = InterFamily,
            color = SubtitleGray,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Go to Help Center",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = InterFamily,
            color = FigmaOliveGreen,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun SuccessfulTransactionScreenPreview() {
    LendlyAppTheme(dynamicColor = false) {
        SuccessfulTransactionScreen(partnerName = "GCash", amount = "2500.00")
    }
}
