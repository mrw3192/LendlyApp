package com.example.lendlyapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.shared.LendlyTopBar
import com.example.lendlyapp.ui.theme.*

@Composable
fun ProfileSuccessScreen(
    onDone: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            LendlyTopBar(
                onNavigationClick = onDone,
                navigationIcon = Icons.Default.Close,
                centerContent = { LendlyLogo(size = DpSize(58.dp, 20.dp)) }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = onDone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen)
                ) {
                    Text("Done", color = OnPrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Checkmark verde estilo Figma
            Text(
                text = "✓",
                fontSize = 120.sp,
                fontWeight = FontWeight.ExtraLight,
                color = FigmaNeonGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ALL DONE!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFamily,
                color = FigmaLightText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your info was saved",
                fontSize = 16.sp,
                color = SubtitleGray,
                fontFamily = InterFamily
            )
        }
    }
}

