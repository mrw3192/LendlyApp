package com.example.lendlyapp.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.shared.LendlyAlertDialog
import com.example.lendlyapp.ui.shared.LendlyBottomBar
import com.example.lendlyapp.ui.shared.LendlyPhoneInput
import com.example.lendlyapp.ui.shared.LendlyTextField
import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightSurface
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.SubtitleGray
import com.example.lendlyapp.viewmodel.RegisterViewModel

// ─── Profile Detail Form Screen ────────────────────────────────────────────────
// Figma node: 180:1407 "Profile-detail-form page" — 393×1068dp
// Background: #FCF8F8 (FigmaLightBg)
//
// Layout:
//   Top App Bar → headline → scrollable form (name, DOB, address, city, postal, phone)
//   → "Next" button

@Composable
fun ProfileDetailScreen(
    viewModel: RegisterViewModel,
    onNavigateToPassword: () -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    if (state.error != null) {
        LendlyAlertDialog(
            title = "Form Validation",
            message = state.error!!,
            onDismiss = { viewModel.clearError() }
        )
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        // ── Top App Bar ────────────────────────────────────────────────────
        LendlyTopAppBar(onBackClick = onBackClick)

        // ── Scrollable Content ─────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
        ) {
            // Headline — 28sp SemiBold
            Text(
                text = "Enter your personal\ndetails",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                color = FigmaLightText,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Full legal first and middle name(s)
            LendlyTextField(
                label = "Full legal first and middle name(s)",
                value = state.firstName,
                onValueChange = { viewModel.onFirstNameChange(it) },
                placeholder = "John D.",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Full legal last name
            LendlyTextField(
                label = "Full legal last name",
                value = state.lastName,
                onValueChange = { viewModel.onLastNameChange(it) },
                placeholder = "Doe",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date of birth — 3-column layout (Day / Month / Year)
            Text(
                text = "Date of birth",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = FormLabel,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Day
                DatePartField(
                    label = "Day",
                    value = state.dobDay,
                    onValueChange = { viewModel.onDobDayChange(it) },
                    placeholder = "08",
                    modifier = Modifier.weight(1f),
                )
                // Month
                DatePartField(
                    label = "Month",
                    value = state.dobMonth,
                    onValueChange = { viewModel.onDobMonthChange(it) },
                    placeholder = "12",
                    modifier = Modifier.weight(1f),
                )
                // Year
                DatePartField(
                    label = "Year",
                    value = state.dobYear,
                    onValueChange = { viewModel.onDobYearChange(it) },
                    placeholder = "1997",
                    modifier = Modifier.weight(1.5f),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Address
            LendlyTextField(
                label = "Address",
                value = state.address,
                onValueChange = { viewModel.onAddressChange(it) },
                placeholder = "Somewhere IN BLOCK 12",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // City
            LendlyTextField(
                label = "City",
                value = state.city,
                onValueChange = { viewModel.onCityChange(it) },
                placeholder = "Davao City",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Postal Code
            LendlyTextField(
                label = "Postal Code",
                value = state.postalCode,
                onValueChange = { viewModel.onPostalCodeChange(it) },
                placeholder = "8000",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number
            LendlyPhoneInput(
                countryCode = state.countryCode,
                onCountryCodeChange = { viewModel.onCountryCodeChange(it) },
                phoneNumber = state.phone,
                onPhoneNumberChange = { viewModel.onPhoneChange(it) },
                label = "Phone Number",
            )

            // Bottom spacing for scroll
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ── Bottom bar ─────────────────────────────────────────────────────
        LendlyBottomBar(
            buttonText = "Next",
            onClick = {
                if (viewModel.validateProfile()) {
                    onNavigateToPassword()
                }
            },
            enabled = !state.isLoading,
        )
    }
}

// ─── Date Part Field (Day / Month / Year) ──────────────────────────────────────

@Composable
private fun DatePartField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = SubtitleGray,
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
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
