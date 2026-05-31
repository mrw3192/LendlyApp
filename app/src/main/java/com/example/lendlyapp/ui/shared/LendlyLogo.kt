package com.example.lendlyapp.ui.shared

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.lendlyapp.ui.theme.FigmaNeonGreen


@Composable
fun LendlyLogo(
    modifier: Modifier = Modifier,
    size: DpSize = DpSize(width = 243.dp, height = 83.dp),
    @Suppress("UNUSED_PARAMETER") color: Color = FigmaNeonGreen,
) {
    val context = LocalContext.current

    val svgImageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(Uri.parse("file:///android_asset/logo_lendly.svg"))
            .build(),
        imageLoader = svgImageLoader,
        contentDescription = "Lendly",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size),
    )
}

@Composable
fun HomeIndicatorBar(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
) {
    Box(
        modifier = modifier
            .size(width = 393.dp, height = 34.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = 134.dp, height = 5.dp)
                .background(color = color, shape = RoundedCornerShape(100.dp)),
        )
    }
}
