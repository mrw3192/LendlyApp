package com.example.lendlyapp.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.lendlyapp.ui.shared.LendlyMainAppBar
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText

@Composable
fun HistoryScreen() {
    Column(modifier = Modifier.fillMaxSize().background(FigmaLightBg)) {
        LendlyMainAppBar()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("History", color = FigmaLightText)
        }
    }
}
