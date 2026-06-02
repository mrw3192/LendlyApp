package com.example.lendlyapp.ui.screens.cashin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.shared.LendlyTopBar
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.SectionDividerGray
import com.example.lendlyapp.ui.theme.SubtitleGray

private const val MAX_LIMIT = 10_000.0

@Composable
fun CashInAmountScreen(
    bankName: String,
    onBack: () -> Unit = {},
    onNext: (amount: String) -> Unit = {},
) {
    var amount by remember { mutableStateOf("") }
    val numericValue = amount.toDoubleOrNull()
    val isOverLimit = numericValue != null && numericValue > MAX_LIMIT
    val isButtonEnabled = amount.isNotEmpty() && numericValue != null && !isOverLimit

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        LendlyTopBar(onNavigationClick = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cash-In Amount",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Balance: ₱0.00",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = SubtitleGray,
            )

            Spacer(modifier = Modifier.height(16.dp))

            AmountInputField(
                amount = amount,
                onAmountChange = { newValue ->
                    if (isValidDecimalInput(newValue)) amount = newValue
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isOverLimit) {
                Text(
                    text = "Amount exceeds the ₱10,000.00 daily limit",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$bankName's max limit is ₱10,000.00 per day",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = SubtitleGray,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Button(
                onClick = { onNext(amount) },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen),
            ) {
                Text(
                    text = "Next",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFamily,
                    color = OnPrimaryGreen,
                )
            }
        }
    }
}

private fun isValidDecimalInput(input: String): Boolean {
    if (input.isEmpty()) return true
    return input.matches(Regex("^\\d+(\\.\\d{0,2})?\$"))
}

@Composable
private fun AmountInputField(
    amount: String,
    onAmountChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            value = amount,
            onValueChange = { onAmountChange(it) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            cursorBrush = SolidColor(FigmaLightText),
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFamily,
                color = Color.Black,
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                ) {
                    Text(
                        text = "₱",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = InterFamily,
                        color = if (amount.isEmpty()) SubtitleGray else Color.Black,
                    )
                    Box {
                        if (amount.isEmpty()) {
                            Text(
                                text = "0.00",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = InterFamily,
                                color = SubtitleGray,
                            )
                        }
                        innerTextField()
                    }
                }
            },
        )

        HorizontalDivider(
            modifier = Modifier.align(Alignment.BottomCenter),
            color = SectionDividerGray,
            thickness = 1.dp,
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun CashInAmountScreenPreview() {
    LendlyAppTheme(dynamicColor = false) {
        CashInAmountScreen(bankName = "BPI", onBack = {})
    }
}
