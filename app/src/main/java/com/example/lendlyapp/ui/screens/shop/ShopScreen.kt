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
import androidx.compose.ui.Alignment
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

// ─── Static editorial data ────────────────────────────────────────────────────

private data class ShopCategory(val label: String, val imageAsset: String)
private data class Brand(val name: String, val imageAsset: String)

private val shopCategories = listOf(
    ShopCategory("Phone",      "img_1d9e70f017321361.png"),
    ShopCategory("Headphones", "img_093dff6624fbadb3.png"),
    ShopCategory("Apparel",    "img_b7610e68381ed2f1.png"),
)

private val popularBrands = listOf(
    Brand("Apple", "img_c6d61f7f62e4010f.png"),
    Brand("Jordan", "img_a2fef0980c1cf4a7.png"),
    Brand("Adidas", "img_34bbdb89fdac37a8.png"),
)

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun ShopScreen(
    onProductClick: (Product) -> Unit = {},
    viewModel: ShopViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val allProducts = when (val s = uiState) {
        is ShopUiState.Success -> s.products
        else -> emptyList()
    }
    var searchQuery by remember { mutableStateOf("") }
    val products = if (searchQuery.isBlank()) allProducts
    else allProducts.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
            it.brand.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            Column {
                ShopAppBar()
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ShopSearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
                    PromotionalCard()
                }
            }
        }
        item { CategorySection() }
        item { BrandsSection() }
        if (searchQuery.isNotBlank() && products.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "No products found for \"$searchQuery\"", color = SubtitleGray, fontSize = 14.sp)
                }
            }
        } else if (products.isNotEmpty()) {
            item {
                ProductSection(
                    title = if (searchQuery.isBlank()) "Recommended For You" else "Results",
                    products = products.take(3),
                    onProductClick = onProductClick,
                )
            }
            if (searchQuery.isBlank()) {
                item {
                    ProductSection(
                        title = "Best Sellers",
                        products = products.drop(3),
                        onProductClick = onProductClick,
                    )
                }
            }
        }
        if (uiState is ShopUiState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = FigmaNeonGreen)
                }
            }
        }
    }
}

// ─── AppBar ───────────────────────────────────────────────────────────────────

@Composable
private fun ShopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { },
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = null, tint = FigmaLightText)
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            LendlyLogo(size = DpSize(58.dp, 20.dp))
        }
        listOf(Icons.Default.Search, Icons.Default.Favorite, Icons.Default.ShoppingCart).forEach { icon ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = FigmaLightText)
            }
        }
    }
}

// ─── Search Bar ───────────────────────────────────────────────────────────────

@Composable
private fun ShopSearchBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE5E2E1), RoundedCornerShape(8.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = SubtitleGray,
            modifier = Modifier.padding(start = 16.dp),
        )
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(color = FigmaLightText, fontSize = 16.sp),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(text = "Search for product", color = SubtitleGray, fontSize = 16.sp)
                }
                innerTextField()
            },
        )
        Box(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(FigmaNeonGreen),
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
            .background(OnPrimaryGreen),
    ) {

        // Green block
        Box(
            modifier = Modifier
                .size(105.dp)
                .align(Alignment.BottomEnd)
                .drawBehind {
                    drawRect(
                        color = FigmaNeonGreen.copy(alpha = 0.15f)
                    )
                }
                .background(FigmaNeonGreen)
        )

        // Top shoe
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/img_87cc7e6ae36438af.png")
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(
                    width = 120.dp,
                    height = 90.dp
                )
                .align(Alignment.CenterEnd)
                .offset(
                    x = (-10).dp,
                    y = (-18).dp
                )
                .graphicsLayer {
                    rotationZ = 8f
                }
        )

        // Bottom shoe
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/img_87cc7e6ae36438af.png")
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(
                    width = 140.dp,
                    height = 100.dp
                )
                .align(Alignment.BottomCenter)
                .offset(
                    x = 10.dp,
                    y = 26.dp
                )
                .graphicsLayer {
                    rotationZ = -18f
                }
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = 22.dp,
                    top = 24.dp,
                    end = 165.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {

            Text(
                text = "The New Shoes",
                color = Color.White,
                fontSize = 30.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFamily,
            )

            Text(
                text = "Shop this season's Top Silhouette",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(FigmaNeonGreen)
                    .padding(
                        horizontal = 22.dp,
                        vertical = 10.dp
                    )
            ) {
                Text(
                    text = "Shop Now",
                    color = OnPrimaryGreen,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = 22.dp,
                    bottom = 18.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        Color.White,
                        CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        Color.White.copy(alpha = 0.45f),
                        CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        Color.White.copy(alpha = 0.45f),
                        CircleShape
                    )
            )
        }
    }
}

// ─── Category Section ─────────────────────────────────────────────────────────

@Composable
private fun CategorySection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(title = "Shop By Category")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(shopCategories) { category ->
                Column(
                    modifier = Modifier.width(100.dp),
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
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("file:///android_asset/${category.imageAsset}")
                                .build(),
                            contentDescription = category.label,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    Text(
                        text = category.label,
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
private fun BrandsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(title = "Popular Brands")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(popularBrands) { brand ->
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FigmaLightBg),
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("file:///android_asset/${brand.imageAsset}")
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
                            .data("file:///android_asset/${product.imageAsset}")
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
                        text = "₱%,.0f × 24 mo".format(product.monthlyPayment),
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
