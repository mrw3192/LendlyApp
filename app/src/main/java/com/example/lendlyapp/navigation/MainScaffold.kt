package com.example.lendlyapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.lendlyapp.ui.screens.home.HomeScreen
import com.example.lendlyapp.ui.screens.history.HistoryScreen
import com.example.lendlyapp.ui.screens.loans.LoanInfoScreen
import com.example.lendlyapp.ui.screens.profile.ProfileScreen
import com.example.lendlyapp.ui.screens.shop.ShopScreen
import com.example.lendlyapp.ui.shared.BottomNavBar
import com.example.lendlyapp.ui.shared.BottomNavTab

@Composable
fun MainScaffold(
    onNavigateToLoanForm: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(BottomNavTab.Home) }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Tab content ────────────────────────────────────────────────────────
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                BottomNavTab.Home    -> HomeScreen()
                BottomNavTab.Loan    -> LoanInfoScreen(onNavigateToForm = onNavigateToLoanForm)
                BottomNavTab.Shop    -> ShopScreen()
                BottomNavTab.History -> HistoryScreen()
                BottomNavTab.Manage  -> ProfileScreen()
            }
        }

      Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding(),
        ) {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )
        }
    }
}
