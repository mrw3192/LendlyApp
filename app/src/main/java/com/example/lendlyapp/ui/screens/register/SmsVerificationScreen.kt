package com.example.lendlyapp.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextDecoration
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
import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.shared.OtpInputRow
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.OtpLinkTeal
import com.example.lendlyapp.viewmodel.RegisterViewModel

// ─── SMS Verification Screen ───────────────────────────────────────────────────
// Figma node: 164:1876 "SMS-verification" — 393×1068dp
// Background: #FCF8F8 (FigmaLightBg)
//
// Layout:
//   Top App Bar → headline → subtitle → OTP 6-box input → resend link → "Next"

@Composable
fun SmsVerificationScreen(
    viewModel: RegisterViewModel,
    onNavigateToProfile: () -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    if (state.error != null) {
        LendlyAlertDialog(
            title = "Code Verification",
            message = state.error!!,
            onDismiss = { viewModel.clearError() }
        )
    }

    // Mask the phone number for display
    val maskedPhone = if (state.phone.length >= 3) {
        "******" + state.phone.takeLast(3)
    } else {
        state.phone
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
            // Headline — 28sp SemiBold
            Text(
                text = "Enter the code",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                color = FigmaLightText,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle with masked phone
            Text(
                text = "Enter the security code we sent to\n$maskedPhone",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = FormLabel,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Label
            Text(
                text = "Enter the code you received here",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = FormLabel,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // OTP input row
            OtpInputRow(
                otpValue = state.otpCode,
                onOtpChange = { viewModel.onOtpChange(it) },
                isError = state.otpError != null,
            )
            if (state.otpError != null) {
                Text(
                    text = state.otpError!!,
                    color = androidx.compose.ui.graphics.Color.Red,
                    fontSize = 12.sp,
                    fontFamily = InterFamily,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Didn't received a code?" link — teal color (#005046), centered + underlined
            Text(
                text = "Didn't receive a code?",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = OtpLinkTeal,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: resend OTP */ },
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }

        // ── Bottom bar ─────────────────────────────────────────────────────
        LendlyBottomBar(
            buttonText = "Next",
            onClick = {
                if (viewModel.validateOtp()) {
                    onNavigateToProfile()
                }
            },
            enabled = !state.isLoading,
        )
    }
}
