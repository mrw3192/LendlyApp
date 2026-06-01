package com.example.lendlyapp.ui.screens.shop

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lendlyapp.model.Product
import com.example.lendlyapp.ui.theme.FigmaDarkForest
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaMintSplash
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOliveSeed
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.ShopPriceGreen
import com.example.lendlyapp.ui.theme.SubtitleGray

private val merchants = listOf(
    Triple("Power Max Center", "img_d8472cb9ce98068d.png", "Available"),
    Triple("The Loop", "img_0125681952ba44a5.png", "Available"),
    Triple("I-Mac Center", "img_adec3785fe24bfc2.png", "Available"),
)

@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
) {
    BackHandler { onBack() }

    var locationExpanded by remember { mutableStateOf(true) }
    var merchantsExpanded by remember { mutableStateOf(true) }
    var featuresExpanded by remember { mutableStateOf(true) }
    var specsExpanded by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { ProductTopAppBar(title = product.name, onBack = onBack) },
        bottomBar = { CheckoutBottomBar(product = product) },
        containerColor = Color.White,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            GreenFeatureBanner()
            ProductImageArea(product = product)
            PriceSection(product = product)
            HorizontalDivider(thickness = 8.dp, color = FigmaMintSplash)
            LocationDropdown(
                expanded = locationExpanded,
                onToggle = { locationExpanded = !locationExpanded },
            )
            HorizontalDivider(thickness = 8.dp, color = FigmaMintSplash)
            MerchantsDropdown(
                product = product,
                expanded = merchantsExpanded,
                onToggle = { merchantsExpanded = !merchantsExpanded },
            )
            HorizontalDivider(thickness = 8.dp, color = FigmaMintSplash)
            ExpandableSection(
                header = "FEATURES",
                expanded = featuresExpanded,
                onToggle = { featuresExpanded = !featuresExpanded },
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(text = "How To Apply For A Loan", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = FigmaLightText)
                    Text(
                        text = "1. Select a product\n2. Choose your merchant\n3. Apply online in minutes\n4. Get approved and pick up in store",
                        fontSize = 14.sp,
                        color = SubtitleGray,
                    )
                    Text(text = "Disclaimer", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = FigmaLightText)
                    Text(
                        text = "Terms and conditions apply. Subject to credit approval.",
                        fontSize = 14.sp,
                        color = SubtitleGray,
                    )
                }
            }
            HorizontalDivider(thickness = 8.dp, color = FigmaMintSplash)
            ExpandableSection(
                header = "PRODUCT SPECIFICATIONS",
                expanded = specsExpanded,
                onToggle = { specsExpanded = !specsExpanded },
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SpecRow(label = "Chip", value = "A14 Bionic chip")
                    SpecRow(label = "Camera", value = "12MP Ultra Wide, Wide, Telephoto")
                }
            }
        }
    }
}

// ─── Top App Bar ──────────────────────────────────────────────────────────────

@Composable
private fun ProductTopAppBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.White)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onBack() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = FigmaLightText)
        }
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1D1B20),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            maxLines = 1,
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { },
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = FigmaLightText)
        }
    }
}

// ─── Green Feature Banner ─────────────────────────────────────────────────────

@Composable
private fun GreenFeatureBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(FigmaNeonGreen),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FeatureChip(icon = Icons.Default.Star, label = "Low interest")
            FeatureChip(icon = Icons.Default.LocalOffer, label = "0% Installment")
            FeatureChip(icon = Icons.Default.LocalShipping, label = "Easy pick-up")
        }
    }
}

@Composable
private fun FeatureChip(icon: ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF1D192B), modifier = Modifier.size(14.dp))
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1D192B))
    }
}

// ─── Product Image Area ───────────────────────────────────────────────────────

@Composable
private fun ProductImageArea(product: Product) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(219.dp),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/product_2.png")
                .build(),
            contentDescription = product.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(width = 178.dp, height = 225.dp),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 8.dp)
                .clip(RoundedCornerShape(1000.dp))
                .border(1.dp, SubtitleGray, RoundedCornerShape(1000.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            Text(text = "1/4", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
        }
    }
}

// ─── Price Section ────────────────────────────────────────────────────────────

@Composable
private fun PriceSection(product: Product) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = "From as low as", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = FigmaDarkForest)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "$${product.monthlyPayment.toInt()}", fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = ShopPriceGreen)
            Text(text = "per month", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = SubtitleGray)
        }
        Text(text = "Apple iPhone 15 Pro Max 256GB, Rose Gold", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
}

// ─── Location Dropdown ────────────────────────────────────────────────────────

@Composable
private fun LocationDropdown(expanded: Boolean, onToggle: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "WHERE DO YOU WANT TO SHOP?", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(FigmaMintSplash)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(text = "Step 3", fontSize = 12.sp, color = FigmaLightText)
            }
        }
        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE5E2E1), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = FigmaOliveSeed)
                Text(text = "Davao City, Davao del Sur", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = OnPrimaryGreen, modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = ShopPriceGreen)
            }
        }
    }
}

// ─── Merchants Dropdown ───────────────────────────────────────────────────────

@Composable
private fun MerchantsDropdown(
    product: Product,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "MARKETPLACE PARTNER MERCHANTS", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = FigmaLightText,
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                merchants.forEach { (name, imageAsset, availability) ->
                    MerchantRow(name = name, imageAsset = imageAsset, availability = availability, product = product)
                }
            }
        }
    }
}

@Composable
private fun MerchantRow(name: String, imageAsset: String, availability: String, product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE5E2E1), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/$imageAsset")
                .build(),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = FigmaLightText)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(FigmaMintSplash)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(text = availability, fontSize = 12.sp, color = FigmaLightText)
            }
            Text(text = "From \$${product.monthlyPayment.toInt()} x 12 months", fontSize = 12.sp, color = SubtitleGray)
            Text(text = "\$${product.totalPrice.toInt()} total price", fontSize = 12.sp, color = SubtitleGray)
        }
        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = FigmaLightText)
    }
}

// ─── Expandable Section ───────────────────────────────────────────────────────

@Composable
private fun ExpandableSection(
    header: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = header, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = FigmaLightText,
            )
        }
        AnimatedVisibility(visible = expanded) {
            content()
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = FigmaLightText)
        Text(text = value, fontSize = 14.sp, color = SubtitleGray)
    }
}

// ─── Checkout Bottom Bar ──────────────────────────────────────────────────────

@Composable
private fun CheckoutBottomBar(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E2E1))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(text = "From as low as", fontSize = 12.sp, color = SubtitleGray)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "\$${product.monthlyPayment.toInt()}", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = ShopPriceGreen)
                Text(text = "per month", fontSize = 12.sp, color = SubtitleGray)
            }
        }
        Button(
            onClick = { },
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen, contentColor = OnPrimaryGreen),
            modifier = Modifier.size(width = 111.dp, height = 48.dp),
        ) {
            Text(text = "Continue", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
