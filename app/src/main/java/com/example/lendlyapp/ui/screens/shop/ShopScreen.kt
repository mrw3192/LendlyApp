package com.example.lendlyapp.ui.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.zIndex
import com.example.lendlyapp.viewmodel.ShopViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lendlyapp.model.Product
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOliveSeed
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.MontserratFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.ShopPriceGreen
import com.example.lendlyapp.ui.theme.SubtitleGray

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun ShopScreen(
    onProductClick: (Product) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    viewModel: ShopViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val s = uiState as? ShopUiState.Success
    val featured    = s?.featured    ?: emptyList()
    val bestSellers = s?.bestSellers ?: emptyList()
    val categories  = s?.categories  ?: emptyList()
    val brands      = s?.brands      ?: emptyList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            Column {
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ShopSearchBar(onBarClick = onSearchClick, onFilterClick = onFilterClick)
                    PromotionalCard()
                }
            }
        }
        if (categories.isNotEmpty()) item { CategorySection(categories) }
        if (brands.isNotEmpty()) item { BrandsSection(brands) }
        if (featured.isNotEmpty()) {
            item { ProductSection(title = "Recommended For You", products = featured, onProductClick = onProductClick) }
        }
        if (bestSellers.isNotEmpty()) {
            item { ProductSection(title = "Best Sellers", products = bestSellers, onProductClick = onProductClick) }
        }
        if (uiState is ShopUiState.Loading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = FigmaNeonGreen)
                }
            }
        }
        if (uiState is ShopUiState.Error) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "No se pudo cargar la tienda",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = InterFamily,
                        color = FigmaLightText,
                    )
                    Text(
                        text = (uiState as ShopUiState.Error).message,
                        fontSize = 13.sp,
                        fontFamily = InterFamily,
                        color = SubtitleGray,
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(FigmaNeonGreen)
                            .clickable { viewModel.retry() }
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                    ) {
                        Text(
                            text = "Reintentar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = InterFamily,
                            color = OnPrimaryGreen,
                        )
                    }
                }
            }
        }
    }
}

// ─── Search Bar ───────────────────────────────────────────────────────────────

@Composable
private fun ShopSearchBar(onBarClick: () -> Unit = {}, onFilterClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE5E2E1), RoundedCornerShape(8.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onBarClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = SubtitleGray,
                modifier = Modifier.padding(start = 16.dp),
            )
            Text(
                text = "Search for product",
                color = SubtitleGray,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp),
            )
        }
        Box(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(FigmaNeonGreen)
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = Icons.Default.FilterList, contentDescription = null, tint = OnPrimaryGreen)
        }
    }
}

// ─── Promotional Card ─────────────────────────────────────────────────────────
@Composable
private fun PromotionalCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(OnPrimaryGreen)
    ) {

        // ─────────────────────────────
        // RECTÁNGULO VERDE + ELIPSE (MISMA CAPA)
        // ─────────────────────────────
        Box(
            modifier = Modifier
                .size(width = 120.dp, height = 95.dp)
                .align(Alignment.BottomEnd)
        ) {

            // Rectángulo verde
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = FigmaNeonGreen,
                        shape = RoundedCornerShape(
                            topStart = 19.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )
        }

        // ─────────────────────────────
        // CONTENIDO TEXTO
        // ─────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(260.dp)
                .padding(start = 22.dp, top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "The New Shoes",
                style = TextStyle(
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp
                ),
                color = Color.White
            )

            Text(
                text = "Shop this season's Top Silhouette",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(118.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FigmaNeonGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Shop Now",
                    color = OnPrimaryGreen,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily
                )
            }
        }

        // ─────────────────────────────
        // SHOES (ENCIMA DE TODO)
        // ─────────────────────────────
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/img_87cc7e6ae36438af.png")
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(360.dp)
                .graphicsLayer {
                    scaleX = -1.1f
                    scaleY = 1.1f
                }
                .align(Alignment.BottomEnd)
                .offset(x = (-45).dp, y = 40.dp)
                .zIndex(2f)
        )

        // ─────────────────────────────
        // INDICADORES
        // ─────────────────────────────
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 22.dp, bottom = 18.dp)
                .zIndex(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color.White, CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color.White.copy(alpha = 0.45f), CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color.White.copy(alpha = 0.45f), CircleShape)
            )
        }
    }
}
// ─── Category Section ─────────────────────────────────────────────────────────

@Composable
private fun CategorySection(categories: List<com.example.lendlyapp.model.ShopCategory>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(title = "Shop By Category")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(categories) { category ->
                Column(
                    modifier = Modifier
                        .width(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 96.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(FigmaLightBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = category.icon, fontSize = 36.sp)
                    }
                    Text(
                        text = category.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = InterFamily,
                        color = FormLabel,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        }
    }
}

// ─── Brands Section ───────────────────────────────────────────────────────────

@Composable
private fun BrandsSection(brands: List<com.example.lendlyapp.model.ShopBrand>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(title = "Popular Brands")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(brands) { brand ->
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FigmaLightBg)
                        .clickable { },
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(brand.logoUrl)
                            .build(),
                        contentDescription = brand.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = brand.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = FormLabel)
                    }
                }
            }
        }
    }
}

// ─── Product Section ──────────────────────────────────────────────────────────

@Composable
private fun ProductSection(
    title: String,
    products: List<Product>,
    onProductClick: (Product) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(title = title)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(products) { product ->
                Column(
                    modifier = Modifier
                        .size(width = 132.dp, height = 145.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FigmaLightBg)
                        .clickable { onProductClick(product) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.imageAsset)
                            .build(),
                        contentDescription = product.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                    Text(
                        text = product.shortName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = InterFamily,
                        color = FigmaLightText,
                        maxLines = 1,
                    )
                    Text(
                        text = "${product.currency}%,.0f × ${product.installmentMonths} mo".format(product.monthlyPayment),
                        fontSize = 11.sp,
                        fontFamily = InterFamily,
                        color = SubtitleGray,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

// ─── Shared ───────────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = MontserratFamily,
            color = FigmaLightText,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { },
        ) {
            Text(
                text = "See All",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = FigmaOliveSeed,
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = FigmaOliveSeed,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
