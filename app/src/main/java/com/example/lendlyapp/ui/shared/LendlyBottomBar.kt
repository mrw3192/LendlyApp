package com.example.lendlyapp.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen

@Composable
fun LendlyBottomBar(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    indicatorColor: Color = Color.Black,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FigmaNeonGreen,
                contentColor = OnPrimaryGreen,
                disabledContainerColor = FigmaNeonGreen.copy(alpha = 0.4f),
                disabledContentColor = OnPrimaryGreen.copy(alpha = 0.4f),
            ),
        ) {
            Text(
                text = buttonText,
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
        }

    }
}
