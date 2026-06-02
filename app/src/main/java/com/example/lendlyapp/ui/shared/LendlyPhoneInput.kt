package com.example.lendlyapp.ui.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.SubtitleGray

// ─── Phone Input ───────────────────────────────────────────────────────────────
// Figma: Split field — country code (80dp) + phone number (remaining width)
// Used in: Verify Phone Number, Profile Detail Form

@Composable
fun LendlyPhoneInput(
    countryCode: String,
    onCountryCodeChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Your Phone Number",
    isError: Boolean = false,
    errorMessage: String? = null,
    onFocusLost: () -> Unit = {},
) {
    var hasFocus by remember { mutableStateOf(false) }

    val textStyle = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = FigmaLightText,
    )

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = FigmaNeonGreen,
        unfocusedBorderColor = SubtitleGray,
        focusedContainerColor = FigmaLightSurface,
        unfocusedContainerColor = FigmaLightSurface,
    )

    val fieldShape = RoundedCornerShape(12.dp)

    androidx.compose.foundation.layout.Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = FormLabel,
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            // Country code field (80dp wide)
            OutlinedTextField(
                value = countryCode,
                onValueChange = onCountryCodeChange,
                modifier = Modifier
                    .width(80.dp)
                    .height(56.dp),
                textStyle = textStyle,
                singleLine = true,
                shape = fieldShape,
                colors = fieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Phone number field (remaining width)
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .onFocusChanged { state ->
                        if (state.isFocused) {
                            hasFocus = true
                        } else if (hasFocus) {
                            onFocusLost()
                            hasFocus = false
                        }
                    },
                textStyle = textStyle,
                singleLine = true,
                shape = fieldShape,
                colors = fieldColors,
                placeholder = {
                    Text(
                        text = "Phone number",
                        fontFamily = InterFamily,
                        fontSize = 16.sp,
                        color = SubtitleGray,
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = isError,
            )
        }
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = InterFamily,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp)
            )
        }
    }
}
