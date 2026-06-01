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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.Info
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
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaMintSplash
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOliveSeed
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.MontserratFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.ShopPriceGreen
import com.example.lendlyapp.ui.theme.SubtitleGray

private val merchants = listOf(
    Pair("Power Max Center", "img_d8472cb9ce98068d.png"),
    Pair("The Loop", "img_0125681952ba44a5.png"),
    Pair("I-Mac Center", "img_adec3785fe24bfc2.png"),
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    FeatureItem(
                        icon = Icons.Default.CheckCircle,
                        title = "How To Apply For A Loan",
                        lines = listOf(
                            "(1) Only 1 ID needed for the loan approval and,",
                            "(2) Click on Continue to check if you are qualified",
                        ),
                    )
                    FeatureItem(
                        icon = Icons.Default.Security,
                        title = "Disclaimer",
                        lines = listOf(
                            "Estimated calculation only. Down Payment and other loan terms may vary.",
                        ),
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    SpecDetail(
                        label = "Chip",
                        lines = listOf(
                            "A16 Bionic chip",
                            "6‑core CPU with 2 performance and 4 efficiency cores",
                            "5‑core GPU",
                            "16‑core Neural Engine",
                        ),
                    )
                    SpecDetail(
                        label = "Camera",
                        lines = listOf(
                            "12MP camera",
                            "ƒ/1.9 aperture",
                            "Autofocus with Focus Pixels",
                            "Retina Flash",
                        ),
                    )
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
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FigmaLightText)
        }
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            color = Color(0xFF1D1B20),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            maxLines = 1,
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = null, tint = FigmaLightText)
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
            FeatureChip(label = "Low interest")
            FeatureChip(label = "0% Installment")
            FeatureChip(label = "Easy pick-up")
        }
    }
}

@Composable
private fun FeatureChip(label: String) {
    Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Medium, fontFamily = InterFamily, color = Color(0xFF1D192B))
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
                .data(product.imageAsset)
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
            Text(text = "1/4", fontSize = 11.sp, fontWeight = FontWeight.Medium, fontFamily = InterFamily, color = SubtitleGray)
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
        Text(
            text = "From as low as",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            color = FigmaDarkForest,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "${product.currency}${product.monthlyPayment.toInt()}",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = MontserratFamily,
                color = ShopPriceGreen,
            )
            Text(
                text = "per month",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = SubtitleGray,
            )
        }
        Text(
            text = product.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = Color.Black,
        )
        if (product.description.isNotBlank()) {
            Text(
                text = product.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = InterFamily,
                color = SubtitleGray,
            )
        }
        if (product.reviewCount > 0) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "⭐ ${product.rating}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = ShopPriceGreen,
                )
                Text(
                    text = "(${product.reviewCount} reviews)",
                    fontSize = 12.sp,
                    fontFamily = InterFamily,
                    color = SubtitleGray,
                )
            }
        }
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
            Text(
                text = "WHERE DO YOU WANT TO SHOP?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = Color.Black,
            )
            StepTag()
        }
        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE5E2E1), RoundedCornerShape(8.dp))
                    .clickable { }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = FigmaOliveSeed)
                Text(
                    text = "Davao City, Davao del Sur",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = OnPrimaryGreen,
                    modifier = Modifier.weight(1f),
                )
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
            Text(
                text = "MARKETPLACE PARTNER MERCHANTS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = Color.Black,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StepTag()
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = FigmaLightText,
                )
            }
        }
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                merchants.forEach { (name, imageAsset) ->
                    MerchantRow(name = name, imageAsset = imageAsset, product = product)
                }
            }
        }
    }
}

@Composable
private fun MerchantRow(name: String, imageAsset: String, product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE5E2E1), RoundedCornerShape(8.dp))
            .clickable { }
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
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = OnPrimaryGreen,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(FigmaMintSplash)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(text = if (product.isAvailable) "Available" else "Not Available", fontSize = 11.sp, fontWeight = FontWeight.Medium, fontFamily = InterFamily, color = OnPrimaryGreen)
            }
            Text(
                text = "From ${product.currency}${product.monthlyPayment.toInt()} x ${product.installmentMonths} months",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = OnPrimaryGreen,
            )
            Text(
                text = "${product.currency}${product.totalPrice.toInt()} total price",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = SubtitleGray,
            )
            Text(
                text = "65% Downpayment",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = SubtitleGray,
            )
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
            Text(text = header, fontSize = 12.sp, fontWeight = FontWeight.Medium, fontFamily = InterFamily, color = Color.Black)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StepTag()
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = FigmaLightText,
                )
            }
        }
        AnimatedVisibility(visible = expanded) {
            content()
        }
    }
}

// ─── Feature Item (inside Features section) ───────────────────────────────────

@Composable
private fun FeatureItem(icon: ImageVector, title: String, lines: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(FigmaLightBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = SubtitleGray, modifier = Modifier.size(24.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = InterFamily, color = OnPrimaryGreen)
            lines.forEach { line ->
                Text(text = line, fontSize = 12.sp, fontWeight = FontWeight.Medium, fontFamily = InterFamily, color = SubtitleGray)
            }
        }
    }
}

// ─── Spec Detail (inside Product Specifications section) ──────────────────────

@Composable
private fun SpecDetail(label: String, lines: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = InterFamily, color = OnPrimaryGreen)
        lines.forEach { line ->
            Text(text = line, fontSize = 12.sp, fontWeight = FontWeight.Medium, fontFamily = InterFamily, color = SubtitleGray)
        }
    }
}

// ─── Step Tag ─────────────────────────────────────────────────────────────────

@Composable
private fun StepTag() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FigmaMintSplash)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(text = "Step 3", fontSize = 12.sp, fontWeight = FontWeight.Normal, fontFamily = InterFamily, color = SubtitleGray)
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
            Text(
                text = "From as low as",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                color = FigmaDarkForest,
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${product.currency}${product.monthlyPayment.toInt()}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MontserratFamily,
                    color = ShopPriceGreen,
                )
                Text(
                    text = "per month",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = SubtitleGray,
                )
            }
        }
        Button(
            onClick = { },
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen, contentColor = OnPrimaryGreen),
            modifier = Modifier.size(width = 111.dp, height = 48.dp),
        ) {
            Text(text = "Continue", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = InterFamily)
        }
    }
}
