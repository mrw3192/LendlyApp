package com.example.lendlyapp.presentation.loans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.lendlyapp.theme.FigmaLightBg
import com.example.lendlyapp.theme.FigmaLightText

@Composable
fun LoanScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg),
        contentAlignment = Alignment.Center,
    ) {
        Text("Loan", color = FigmaLightText)
    }
}
