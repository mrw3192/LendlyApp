package com.example.lendlyapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.theme.FigmaMintSplash
import com.example.lendlyapp.theme.InterFamily
import com.example.lendlyapp.theme.LendlyAppTheme
import com.example.lendlyapp.theme.OnPrimaryGreen
import com.example.lendlyapp.theme.SubtitleGray

// ─── Bottom Navigation Bar ─────────────────────────────────────────────────────
// Figma node: 'Navigation bar' id=71:1884
// pos=(+0,+819) 393×80dp — fill=#FFFFFF — layout=HORIZONTAL spacing=8dp
//
// Active tab  → icon-container fill=#E5F5EA r=16dp; icon+label color=#102000
// Inactive tab → icon-container transparent;       icon+label color=#6A6C6A

enum class BottomNavTab(
    val label: String,
    val icon: ImageVector,
) {
    Home("Home", Icons.Default.Home),
    Loan("Loan", Icons.Default.CreditCard),
    Shop("Shop", Icons.Default.ShoppingBag),
    History("History", Icons.Default.History),
    Manage("Manage", Icons.Default.GridView),
}

@Composable
fun BottomNavBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BottomNavTab.entries.forEach { tab ->
            BottomNavItem(
                tab = tab,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    tab: BottomNavTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (isSelected) OnPrimaryGreen else SubtitleGray
    val containerBg  = if (isSelected) FigmaMintSplash else Color.Transparent

    Column(
        modifier = modifier
            .height(80.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // icon-container: active → mint pill, inactive → transparent
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(color = containerBg, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = contentColor,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = tab.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            color = contentColor,
        )
    }
}

@Preview(showBackground = true, widthDp = 393)
@Composable
private fun BottomNavBarPreview() {
    LendlyAppTheme(dynamicColor = false) {
        BottomNavBar(
            selectedTab = BottomNavTab.Home,
            onTabSelected = {},
        )
    }
}
