package com.example.lendlyapp.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.SubtitleGray
import com.example.lendlyapp.ui.theme.InterFamily

@Composable
fun LendlyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    onFocusLost: () -> Unit = {},
) {
    var hasFocus by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = FormLabel,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        hasFocus = true
                    } else if (hasFocus) {
                        onFocusLost()
                        hasFocus = false
                    }
                },
            textStyle = TextStyle(
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = FigmaLightText,
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    fontFamily = InterFamily,
                    fontSize = 16.sp,
                    color = SubtitleGray,
                )
            },
            trailingIcon = trailingIcon,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = singleLine,
            readOnly = readOnly,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FigmaNeonGreen,
                unfocusedBorderColor = SubtitleGray,
                focusedContainerColor = FigmaLightSurface,
                unfocusedContainerColor = FigmaLightSurface,
            ),
            isError = isError,
        )
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
