package com.example.lendlyapp.ui.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.MontserratFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.SubtitleGray

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShopFilterScreen(
    onBack: () -> Unit,
    onApply: () -> Unit,
) {
    var selectedBrand by remember { mutableStateOf("All") }
    var selectedGender by remember { mutableStateOf("All") }
    var selectedSortBy by remember { mutableStateOf("Most Recent") }
    var selectedPriceRange by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = FigmaLightText,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Text(
                text = "Filter",
                fontFamily = InterFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = FigmaLightText,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(24.dp)) // To balance the back arrow
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Brands
            FilterSection(
                title = "Brands",
                options = listOf("All", "Nike", "Adidas", "Puma", "Jordan"),
                selectedOption = selectedBrand,
                onSelect = { selectedBrand = it }
            )

            // Gender
            FilterSection(
                title = "Gender",
                options = listOf("All", "Men", "Women"),
                selectedOption = selectedGender,
                onSelect = { selectedGender = it }
            )

            // Sort by
            FilterSection(
                title = "Sort by",
                options = listOf("Most Recent", "Popular", "Low Interest"),
                selectedOption = selectedSortBy,
                onSelect = { selectedSortBy = it }
            )

            // Price Range
            FilterSection(
                title = "Price Range",
                options = listOf("All", "$500 - $1000", "$1000 - $5000"),
                selectedOption = selectedPriceRange,
                onSelect = { selectedPriceRange = it }
            )
        }

        // Bottom buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Reset Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, Color(0xFFADAAAA), RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .clickable {
                        selectedBrand = "All"
                        selectedGender = "All"
                        selectedSortBy = "Most Recent"
                        selectedPriceRange = "All"
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reset Filter",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = SubtitleGray
                )
            }

            // Apply Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                	.clip(RoundedCornerShape(24.dp))
                    .background(FigmaNeonGreen)
                    .clickable { onApply() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Apply",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = OnPrimaryGreen
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterSection(
    title: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = FigmaLightText
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                val borderModifier = if (isSelected) {
                    Modifier
                } else {
                    Modifier.border(1.dp, Color(0xFFE5E2E1), RoundedCornerShape(8.dp))
                }
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) FigmaNeonGreen else Color.White)
                        .then(borderModifier)
                        .clickable { onSelect(option) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontFamily = InterFamily,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        fontSize = 14.sp,
                        color = if (isSelected) OnPrimaryGreen else SubtitleGray
                    )
                }
            }
        }
    }
}
