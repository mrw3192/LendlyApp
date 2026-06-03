package com.example.lendlyapp.ui.screens.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.theme.*
import com.example.lendlyapp.viewmodel.ProfileUiState
import com.example.lendlyapp.viewmodel.ProfileViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CreditScoreScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {}
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
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Credit Score",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFamily,
                color = FigmaLightText,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card del Arco del Score
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = FigmaLightBg),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val score = if (uiState is ProfileUiState.Success) (uiState as ProfileUiState.Success).user.creditScore else 720

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                        ScoreArc(score = score)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 40.dp)
                        ) {
                            Text(text = score.toString(), fontSize = 48.sp, fontWeight = FontWeight.Bold, color = FigmaLightText)
                            Text(text = "Your Score is Good", fontSize = 16.sp, color = SubtitleGray, fontWeight = FontWeight.Medium)
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

            // Sección General
            Text(
                text = "General",
                fontSize = 14.sp,
                color = SubtitleGray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = FigmaLightBg, modifier = Modifier.padding(horizontal = 16.dp))

            ProfileMenuItem(Icons.Default.Person, "Account details", onClick = onNavigateToEditProfile)
            ProfileMenuItem(Icons.Default.Email, "Receiving by email or phone")
            ProfileMenuItem(Icons.Default.CalendarMonth, "Scheduled pay")
            ProfileMenuItem(Icons.Default.Settings, "Settings")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ScoreArc(score: Int) {
    Canvas(modifier = Modifier.size(200.dp)) {
        val sweepAngle = 180f
        val startAngle = 180f

        // Arco de fondo
        drawArc(
            color = Color.LightGray.copy(alpha = 0.2f),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
        )

        // Arco de progreso con gradiente
        val progress = (score.toFloat() / 850f) * sweepAngle
        drawArc(
            brush = Brush.horizontalGradient(listOf(Color.Red, Color.Yellow, Color.Green)),
            startAngle = startAngle,
            sweepAngle = progress,
            useCenter = false,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
        )

        // Aguja (Gauge needle)
        val angleInRadians = (startAngle + progress) * (Math.PI / 180f).toFloat()
        val radius = size.width / 2
        val lineLength = radius - 20.dp.toPx()
        val endX = center.x + lineLength * cos(angleInRadians).toFloat()
        val endY = center.y + lineLength * sin(angleInRadians).toFloat()

        drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = center)
        drawLine(color = Color.Black, start = center, end = Offset(endX, endY), strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = FigmaLightText, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = FigmaLightText)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = SubtitleGray)
    }
}

