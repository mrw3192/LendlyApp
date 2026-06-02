package com.example.lendlyapp.ui.shared

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaOliveGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily

@Composable
fun LendlyAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontFamily = InterFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = FigmaLightText,
            )
        },
        text = {
            Text(
                text = message,
                fontFamily = InterFamily,
                fontSize = 14.sp,
                color = FormLabel,
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "OK",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = FigmaOliveGreen,
                    fontSize = 14.sp,
                )
            }
        },
        containerColor = FigmaLightSurface,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
    )
}
