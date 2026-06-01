package com.example.lendlyapp.ui.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText

@Composable
fun ShopScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg),
        contentAlignment = Alignment.Center,
    ) {
        Text("Shop", color = FigmaLightText)
    }
}
