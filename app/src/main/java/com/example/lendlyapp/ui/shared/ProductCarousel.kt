package com.example.lendlyapp.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lendlyapp.model.Product
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaOliveSeed
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.MontserratFamily
import com.example.lendlyapp.ui.theme.ProductPriceGreen
import com.example.lendlyapp.ui.theme.SubtitleGray
import java.util.Locale

data class ProductCarouselStyle(
    val titleFontFamily: FontFamily,
    val seeAllIcon: ImageVector,
    val nameSelector: (Product) -> String,
    val nameColor: Color,
    val nameFontWeight: FontWeight,
    val priceSelector: (Product) -> String,
    val priceColor: Color,
    val priceFontWeight: FontWeight,
    val fixedImage: Boolean,
) {
    companion object {
        val Home = ProductCarouselStyle(
            titleFontFamily = InterFamily,
            seeAllIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            nameSelector = { it.name },
            nameColor = FormLabel,
            nameFontWeight = FontWeight.Medium,
            priceSelector = { String.format(Locale.US, "₱%,.0f x %d mo", it.monthlyPayment, it.installmentMonths) },
            priceColor = ProductPriceGreen,
            priceFontWeight = FontWeight.Medium,
            fixedImage = true,
        )
        val Shop = ProductCarouselStyle(
            titleFontFamily = MontserratFamily,
            seeAllIcon = Icons.Default.ArrowForward,
            nameSelector = { it.shortName },
            nameColor = FigmaLightText,
            nameFontWeight = FontWeight.SemiBold,
            priceSelector = { "${it.currency}%,.0f × ${it.installmentMonths} mo".format(it.monthlyPayment) },
            priceColor = SubtitleGray,
            priceFontWeight = FontWeight.Normal,
            fixedImage = false,
        )
    }
}

@Composable
fun ProductCarousel(
    title: String,
    products: List<Product>,
    style: ProductCarouselStyle,
    modifier: Modifier = Modifier,
    onSeeAllClick: (() -> Unit)? = null,
    onProductClick: ((Product) -> Unit)? = null,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProductCarouselHeader(title = title, style = style, onSeeAllClick = onSeeAllClick)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(products) { product ->
                ProductCarouselCard(
                    product = product,
                    style = style,
                    onClick = onProductClick?.let { callback -> { callback(product) } },
                )
            }
        }
    }
}

@Composable
private fun ProductCarouselHeader(
    title: String,
    style: ProductCarouselStyle,
    onSeeAllClick: (() -> Unit)?,
) {
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
            fontFamily = style.titleFontFamily,
            color = FigmaLightText,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = if (onSeeAllClick != null) Modifier.clickable { onSeeAllClick() } else Modifier,
        ) {
            Text(
                text = "See All",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = FigmaOliveSeed,
            )
            Icon(
                imageVector = style.seeAllIcon,
                contentDescription = null,
                tint = FigmaOliveSeed,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun ProductCarouselCard(
    product: Product,
    style: ProductCarouselStyle,
    onClick: (() -> Unit)?,
) {
    val container = Modifier
        .size(width = 132.dp, height = 145.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(FigmaLightBg)
        .let { if (onClick != null) it.clickable { onClick() } else it }

    if (style.fixedImage) {
        Column(modifier = container) {
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageAsset)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 85.dp, height = 65.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProductCardName(product, style, Modifier.fillMaxWidth().padding(horizontal = 16.dp))
            ProductCardPrice(product, style, Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        }
    } else {
        Column(modifier = container.padding(horizontal = 12.dp, vertical = 10.dp)) {
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
            ProductCardName(product, style, Modifier)
            ProductCardPrice(product, style, Modifier)
        }
    }
}

@Composable
private fun ProductCardName(product: Product, style: ProductCarouselStyle, modifier: Modifier) {
    Text(
        text = style.nameSelector(product),
        fontSize = 12.sp,
        fontWeight = style.nameFontWeight,
        fontFamily = InterFamily,
        color = style.nameColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Composable
private fun ProductCardPrice(product: Product, style: ProductCarouselStyle, modifier: Modifier) {
    Text(
        text = style.priceSelector(product),
        fontSize = 11.sp,
        fontWeight = style.priceFontWeight,
        fontFamily = InterFamily,
        color = style.priceColor,
        maxLines = 1,
        modifier = modifier,
    )
}
