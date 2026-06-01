package com.example.lendlyapp.ui.screens.cashin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lendlyapp.ui.theme.CashInArrowGreen
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaOliveGreen
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import com.example.lendlyapp.ui.theme.SearchBorderGray
import com.example.lendlyapp.ui.theme.SectionDividerGray
import com.example.lendlyapp.ui.theme.SubtitleGray

private data class PaymentOption(val name: String, val assetFileName: String)

private val banks = listOf(
    PaymentOption("BPI",       "img_6daf468833128e42.png"),
    PaymentOption("Chinabank", "img_b4b5e58a8be91ecc.png"),
    PaymentOption("RCBC",      "img_cd083b9412eebeed.png"),
    PaymentOption("Unionbank", "img_7b998516048dbe0f.png"),
)

private val eWallets = listOf(
    PaymentOption("GCash",    "img_3ff9a800f9908816.png"),
    PaymentOption("Pay Maya", "img_11be8ebf4a1194f5.png"),
    PaymentOption("PayPal",   "img_f0d1f49c975175d1.png"),
)

@Composable
fun CashInOnlineScreen(onBack: () -> Unit = {}) {
    var query by remember { mutableStateOf("") }

    val filteredBanks   = banks.filter   { it.name.contains(query, ignoreCase = true) }
    val filteredWallets = eWallets.filter { it.name.contains(query, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        OnlineTopBar(onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Online Cash-In Options",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))
            OnlineSearchBar(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))
            OptionsCard(
                banks = filteredBanks,
                wallets = filteredWallets,
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OnlineTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = FigmaLightText,
                modifier = Modifier.size(24.dp),
            )
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(
                text = "Cash-In",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = FigmaLightText,
            )
        }
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun OnlineSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        placeholder = {
            Text(text = "Search...", fontSize = 14.sp, fontFamily = InterFamily, color = SubtitleGray)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SubtitleGray)
        },
        textStyle = TextStyle(fontSize = 14.sp, fontFamily = InterFamily, color = FigmaLightText),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = SearchBorderGray,
            focusedBorderColor = FigmaOliveGreen,
            unfocusedContainerColor = FigmaLightSurface,
            focusedContainerColor = FigmaLightSurface,
        ),
    )
}

@Composable
private fun OptionsCard(
    banks: List<PaymentOption>,
    wallets: List<PaymentOption>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaLightSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            if (banks.isNotEmpty()) {
                SectionLabel(text = "BANKS")
                banks.forEach { option ->
                    Spacer(modifier = Modifier.height(16.dp))
                    PaymentOptionRow(option = option)
                }
            }

            if (banks.isNotEmpty() && wallets.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = SectionDividerGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (wallets.isNotEmpty()) {
                SectionLabel(text = "E-WALLETS")
                wallets.forEach { option ->
                    Spacer(modifier = Modifier.height(16.dp))
                    PaymentOptionRow(option = option)
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = InterFamily,
        color = Color.Black,
    )
}

@Composable
private fun PaymentOptionRow(option: PaymentOption) {
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/${option.assetFileName}")
                .crossfade(true)
                .build(),
            contentDescription = option.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp).clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = option.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = Color.Black,
            modifier = Modifier.weight(1f),
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = CashInArrowGreen,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun CashInOnlineScreenPreview() {
    LendlyAppTheme(dynamicColor = false) {
        CashInOnlineScreen(onBack = {})
    }
}
