package com.example.lendlyapp.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.SubtitleGray

// ─── OTP Input Row ─────────────────────────────────────────────────────────────
// Figma: 6 boxes, each 53×56dp, single centered digit
// Used in: SMS Verification screen

private const val OTP_LENGTH = 6

@Composable
fun OtpInputRow(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequesters = remember { List(OTP_LENGTH) { FocusRequester() } }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        for (i in 0 until OTP_LENGTH) {
            val char = otpValue.getOrNull(i)?.toString() ?: ""

            OutlinedTextField(
                value = char,
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        val newOtp = buildString {
                            for (j in 0 until OTP_LENGTH) {
                                append(
                                    if (j == i) newValue
                                    else otpValue.getOrNull(j)?.toString() ?: ""
                                )
                            }
                        }
                        onOtpChange(newOtp)
                        // Auto-advance focus
                        if (newValue.isNotEmpty() && i < OTP_LENGTH - 1) {
                            focusRequesters[i + 1].requestFocus()
                        }
                    } else if (newValue.isEmpty()) {
                        // Handle backspace
                        val newOtp = buildString {
                            for (j in 0 until OTP_LENGTH) {
                                append(
                                    if (j == i) ""
                                    else otpValue.getOrNull(j)?.toString() ?: ""
                                )
                            }
                        }
                        onOtpChange(newOtp)
                        if (i > 0) {
                            focusRequesters[i - 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .width(53.dp)
                    .height(56.dp)
                    .focusRequester(focusRequesters[i]),
                textStyle = TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = FigmaLightText,
                    textAlign = TextAlign.Center,
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FigmaNeonGreen,
                    unfocusedBorderColor = SubtitleGray,
                    focusedContainerColor = FigmaLightSurface,
                    unfocusedContainerColor = FigmaLightSurface,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }

    // Request focus on first box when composable enters composition
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}
