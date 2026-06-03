package com.example.lendlyapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lendlyapp.viewmodel.MainViewModel
import com.example.lendlyapp.model.Product
import com.example.lendlyapp.ui.screens.history.HistoryScreen
import com.example.lendlyapp.ui.screens.loans.LoanInfoScreen
import com.example.lendlyapp.ui.screens.home.HomeScreen
import com.example.lendlyapp.ui.screens.loans.LoanScreen
import com.example.lendlyapp.ui.screens.profile.ProfileScreen
import com.example.lendlyapp.ui.screens.shop.ProductDetailScreen
import com.example.lendlyapp.ui.screens.shop.SearchScreen
import com.example.lendlyapp.ui.screens.shop.ShopScreen
import com.example.lendlyapp.ui.screens.shop.ShopFilterScreen
import com.example.lendlyapp.ui.shared.BottomNavBar
import com.example.lendlyapp.ui.shared.BottomNavTab
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.shared.LendlyTopBar
import com.example.lendlyapp.ui.theme.FigmaLightText

@Composable
fun MainScaffold(
    onNavigateToCashIn: () -> Unit = {},
    onNavigateToLoanForm: () -> Unit = {},
  viewModel: MainViewModel = hiltViewModel()
) {
    
    val avatarUrl by viewModel.avatarUrl.collectAsState()
    var selectedTab by remember { mutableStateOf(BottomNavTab.Home) }
    var shopSelectedProduct by remember { mutableStateOf<Product?>(null) }
    var shopShowSearch by remember { mutableStateOf(false) }
    var shopShowFilter by remember { mutableStateOf(false) }

    val showMainTopBar = shopSelectedProduct == null && !shopShowSearch && !shopShowFilter

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {

        if (showMainTopBar) {
            LendlyTopBar(
                onNavigationClick = {},
                navigationIcon = Icons.Outlined.Person,
                navigationIconContent = if (avatarUrl != null) {
                    {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(28.dp).clip(CircleShape),
                        )
                    }
                } else null,
                centerContent = { LendlyLogo(size = DpSize(58.dp, 20.dp)) },
                trailingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = FigmaLightText,
                    )
                },
            )
        }

        // ── Tab content ────────────────────────────────────────────────────────
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                BottomNavTab.Home    -> HomeScreen(onNavigateToCashIn = onNavigateToCashIn)
                BottomNavTab.Loan    -> LoanInfoScreen(onNavigateToForm = onNavigateToLoanForm)              
                BottomNavTab.Shop    -> {
                    val selected = shopSelectedProduct
                    when {
                        selected != null -> ProductDetailScreen(
                            product = selected,
                            onBack = { shopSelectedProduct = null },
                        )
                        shopShowFilter -> ShopFilterScreen(
                            onBack = { shopShowFilter = false },
                            onApply = { shopShowFilter = false }
                        )
                        shopShowSearch -> SearchScreen(
                            onBack = { shopShowSearch = false },
                            onFilterClick = { shopShowFilter = true },
                        )
                        else -> ShopScreen(
                            onProductClick = { shopSelectedProduct = it },
                            onSearchClick = { shopShowSearch = true },
                            onFilterClick = { shopShowFilter = true }
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
                        shopShowFilter = false
                        shopSelectedProduct = null
                    }
                    selectedTab = tab
                },
            )
        }
    }
}
