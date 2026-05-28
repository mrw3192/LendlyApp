package com.example.lendlyapp.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.presentation.components.LendlyAlertDialog
import com.example.lendlyapp.presentation.components.LendlyBottomBar
import com.example.lendlyapp.presentation.components.LendlyTextField
import com.example.lendlyapp.presentation.components.LendlyTopAppBar
import com.example.lendlyapp.theme.FigmaLightBg
import com.example.lendlyapp.theme.FigmaLightText
import com.example.lendlyapp.theme.FormLabel
import com.example.lendlyapp.theme.InterFamily

// ─── Create Password Screen ───────────────────────────────────────────────────
// Figma node: 180:1946 "Create-password page" — 393×1068dp
// Background: #FCF8F8 (FigmaLightBg)
//
// Layout:
//   Top App Bar → headline → password input with visibility toggle
//   → validation hint → "Next" button

@Composable
fun CreatePasswordScreen(
    viewModel: RegisterViewModel,
    onNavigateToDone: () -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    if (state.error != null) {
        LendlyAlertDialog(
            title = "Password Security",
            message = state.error!!,
            onDismiss = { viewModel.clearError() }
        )
    }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        // ── Top App Bar ────────────────────────────────────────────────────
        LendlyTopAppBar(onBackClick = onBackClick)

        // ── Content ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            // Headline — 28sp SemiBold
            Text(
                text = "Create your password",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                color = FigmaLightText,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Password input with visibility toggle
            LendlyTextField(
                label = "Choose a password",
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = "********",
                isPassword = true,
                passwordVisible = passwordVisible,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = FormLabel,
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Validation hint with bold parts
            // Figma: "At least 9 characters, containing a letter and a number"
            // 14sp, fw=500, fill=#454745
            Text(
                text = buildAnnotatedString {
                    append("At least ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("9 characters")
                    }
                    append(", containing ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("a letter")
                    }
                    append(" and ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("a number")
                    }
                },
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = FormLabel,
            )
        }

        // ── Bottom bar ─────────────────────────────────────────────────────
        LendlyBottomBar(
            buttonText = "Next",
            onClick = {
                if (viewModel.validatePassword()) {
                    viewModel.completeRegistration()
                    onNavigateToDone()
                }
            },
            enabled = !state.isLoading,
        )
    }
}
