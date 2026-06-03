package com.example.lendlyapp.ui.screens.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.theme.*
import com.example.lendlyapp.viewmodel.ProfileUiState
import com.example.lendlyapp.viewmodel.ProfileViewModel

@Composable
fun CreditScoreScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            LendlyTopAppBar(onBackClick = onBack, showInfoIcon = false)
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Credit Score",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFamily,
                color = FigmaLightText,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = FigmaLightBg),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                        val score = if (uiState is ProfileUiState.Success) (uiState as ProfileUiState.Success).user.creditScore else 0
                        ScoreArc(score = score)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = score.toString(),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = FigmaLightText
                            )
                            Text(
                                text = "Your Score is Good",
                                fontSize = 16.sp,
                                color = SubtitleGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "What is Credit Score?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = SubtitleGray,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This is your trust score, used as a base to determine the various activities you do on Credit Score.",
                        fontSize = 12.sp,
                        color = SubtitleGray,
                        lineHeight = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ScoreArc(score: Int) {
    Canvas(modifier = Modifier.size(200.dp)) {
        val sweepAngle = 180f
        val startAngle = 180f
        drawArc(
            color = Color.LightGray.copy(alpha = 0.3f),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
        )
        val progress = (score.toFloat() / 850f) * sweepAngle
        drawArc(
            brush = Brush.horizontalGradient(listOf(Color.Red, Color.Yellow, Color.Green)),
            startAngle = startAngle,
            sweepAngle = progress,
            useCenter = false,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

