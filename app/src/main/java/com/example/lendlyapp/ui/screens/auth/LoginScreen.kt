package com.example.lendlyapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.ui.shared.HomeIndicatorBar
import com.example.lendlyapp.ui.shared.LendlyBottomBar
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.shared.LendlyTextField
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaOliveGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.SubtitleGray
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.viewmodel.LoginViewModel
import com.example.lendlyapp.viewmodel.LoginUiState

// ─── Login Screen ──────────────────────────────────────────────────────────────
// Figma node: 196:2165 "Login Page" — 393×1013dp
// Background: #FFFFFF (FigmaLightSurface)
//
// Layout:
//   Centered Lendly Logo → email field → password field → "Log In" button
//
// API: POST /auth/login { email, password } → { token, userId, email }
// Currently mocked — any valid email + non-empty password → success.

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    // Navigate on success
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onNavigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightSurface)
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // ── Logo area ──────────────────────────────────────────────────
            // Figma: centered at y≈327, Logo + shield decoration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center,
            ) {
                LendlyLogo(
                    size = DpSize(width = 174.dp, height = 60.dp),
                )
            }

            // ── Form area ──────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            ) {
                // Email field
                LendlyTextField(
                    label = "Email",
                    value = email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    placeholder = "Enter your email",
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                LendlyTextField(
                    label = "Password",
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    placeholder = "Enter your password",
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

                // "Forgot your password?" link
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Forgot your password?",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = FigmaOliveGreen,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { /* TODO: forgot password flow */ },
                )

                Spacer(modifier = Modifier.weight(1f))

                // Register link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Don't have an account? ",
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = SubtitleGray,
                    )
                    Text(
                        text = "Sign up",
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = FigmaOliveGreen,
                        modifier = Modifier.clickable { onNavigateToRegister() },
                    )
                }
            }
        }

        // ── Bottom bar with "Log In" button ────────────────────────────────
        LendlyBottomBar(
            buttonText = if (uiState is LoginUiState.Loading) "Logging in…" else "Log In",
            onClick = { viewModel.login() },
            enabled = uiState !is LoginUiState.Loading,
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        // ── Loading overlay ────────────────────────────────────────────────
        if (uiState is LoginUiState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = FigmaNeonGreen,
                )
            }
        }

        // ── Error snackbar ─────────────────────────────────────────────────
        if (uiState is LoginUiState.Error) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(12.dp),
                dismissAction = {
                    Text(
                        text = "Dismiss",
                        color = FigmaNeonGreen,
                        modifier = Modifier
                            .clickable { viewModel.clearError() }
                            .padding(8.dp),
                    )
                },
            ) {
                Text(text = (uiState as LoginUiState.Error).message)
            }
        }
    }
}
