package com.example.lendlyapp.presentation.navigation

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
import com.example.lendlyapp.presentation.dashboard.HomeScreen
import com.example.lendlyapp.presentation.history.HistoryScreen
import com.example.lendlyapp.presentation.loans.LoanScreen
import com.example.lendlyapp.presentation.profile.ProfileScreen
import com.example.lendlyapp.presentation.shop.ShopScreen

@Composable
fun MainScaffold() {
    var selectedTab by remember { mutableStateOf(BottomNavTab.Home) }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Tab content ────────────────────────────────────────────────────────
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                BottomNavTab.Home    -> HomeScreen()
                BottomNavTab.Loan    -> LoanScreen()
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
