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
import com.example.lendlyapp.ui.screens.loans.LoanScreen
import com.example.lendlyapp.ui.screens.profile.ProfileScreen
import com.example.lendlyapp.model.Product
import com.example.lendlyapp.ui.screens.shop.ProductDetailScreen
import com.example.lendlyapp.ui.screens.shop.SearchScreen
import com.example.lendlyapp.ui.screens.shop.ShopScreen
import com.example.lendlyapp.ui.shared.BottomNavBar
import com.example.lendlyapp.ui.shared.BottomNavTab

@Composable
fun MainScaffold() {
    var selectedTab by remember { mutableStateOf(BottomNavTab.Home) }
    var shopSelectedProduct by remember { mutableStateOf<Product?>(null) }
    var shopShowSearch by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Tab content ────────────────────────────────────────────────────────
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                BottomNavTab.Home    -> HomeScreen()
                BottomNavTab.Loan    -> LoanScreen()
                BottomNavTab.Shop    -> {
                    val selected = shopSelectedProduct
                    when {
                        selected != null -> ProductDetailScreen(
                            product = selected,
                            onBack = { shopSelectedProduct = null },
                        )
                        shopShowSearch -> SearchScreen(
                            onBack = { shopShowSearch = false },
                        )
                        else -> ShopScreen(
                            onProductClick = { shopSelectedProduct = it },
                            onSearchClick = { shopShowSearch = true },
                        )
                    }
                }
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
                onTabSelected = { tab ->
                    if (tab != BottomNavTab.Shop) {
                        shopShowSearch = false
                        shopSelectedProduct = null
                    }
                    selectedTab = tab
                },
            )
        }
    }
}
