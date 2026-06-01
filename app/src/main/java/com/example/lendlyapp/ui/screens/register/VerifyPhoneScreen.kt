package com.example.lendlyapp.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.shared.LendlyAlertDialog
import com.example.lendlyapp.ui.shared.LendlyBottomBar
import com.example.lendlyapp.ui.shared.LendlyPhoneInput
import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.viewmodel.RegisterViewModel

// ─── Verify Phone Number Screen ────────────────────────────────────────────────
// Figma node: 164:1750 "Verify phone number" — 393×1068dp
// Background: #FCF8F8 (FigmaLightBg)
//
// Layout:
//   Top App Bar → headline → subtitle → phone input → "Send Code" button

@Composable
fun VerifyPhoneScreen(
    viewModel: RegisterViewModel,
    onNavigateToSms: () -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    if (state.error != null) {
        LendlyAlertDialog(
            title = "Phone Validation",
            message = state.error!!,
            onDismiss = { viewModel.clearError() }
        )
    }

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
            // Headline — 28sp, SemiBold, FigmaLightText
            Text(
                text = "Verify your phone\nnumber with a code",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                color = FigmaLightText,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle — 16sp, Normal, FormLabel
            Text(
                text = "We will send you a One-Time-Password (OTP) to confirm you number.",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = FormLabel,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Phone input
            LendlyPhoneInput(
                countryCode = state.countryCode,
                onCountryCodeChange = { viewModel.onCountryCodeChange(it) },
                phoneNumber = state.phone,
                onPhoneNumberChange = { viewModel.onPhoneChange(it) },
                isError = state.phoneError != null,
                errorMessage = state.phoneError,
                onFocusLost = { viewModel.onPhoneFocusLost() }
            )
        }

        // ── Bottom bar ─────────────────────────────────────────────────────
        LendlyBottomBar(
            buttonText = "Send Code",
            onClick = {
                if (viewModel.validatePhone()) {
                    viewModel.sendOtp()
                    onNavigateToSms()
                }
            },
            enabled = !state.isLoading,
        )
    }
}
