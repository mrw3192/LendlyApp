package com.example.lendlyapp.ui.screens.shop

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FigmaOliveGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.SubtitleGray
import com.example.lendlyapp.viewmodel.ShopViewModel

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    viewModel: ShopViewModel = hiltViewModel(),
) {
    BackHandler { onBack() }

    val recentSearches by viewModel.recentSearches.collectAsState()
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SearchTopAppBar(onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SearchBarInPage(
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    if (query.isNotBlank()) viewModel.addRecentSearch(query)
                },
                focusRequester = focusRequester,
            )
            if (recentSearches.isNotEmpty()) {
                RecentLabel(onClearAll = { viewModel.clearAllRecentSearches() })
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                RecentSearchList(
                    items = recentSearches,
                    onItemClick = {},
                    onItemRemove = { term -> viewModel.removeRecentSearch(term) },
                )
            }
        }
    }
}

// ─── Top App Bar ──────────────────────────────────────────────────────────────

@Composable
private fun SearchTopAppBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onBack() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = FigmaLightText,
            )
        }
        Text(
            text = "Search",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            color = Color(0xFF1D1B20),
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = FigmaLightText,
            )
        }
    }
}

// ─── Search Bar (in-page) ─────────────────────────────────────────────────────

@Composable
private fun SearchBarInPage(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    focusRequester: FocusRequester,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE5E2E1), RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = OnPrimaryGreen,
            )
        }
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = InterFamily,
                color = FigmaLightText,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search for product",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = InterFamily,
                            color = FormLabel,
                        )
                    }
                    innerTextField()
                }
            },
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(FigmaNeonGreen)
                .clickable { },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = OnPrimaryGreen,
            )
        }
    }
}

// ─── Recent Label ─────────────────────────────────────────────────────────────

@Composable
private fun RecentLabel(onClearAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Recent",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = Color.Black,
        )
        Text(
            text = "Clear All",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = InterFamily,
            color = FigmaOliveGreen,
            modifier = Modifier.clickable { onClearAll() },
        )
    }
}

// ─── Recent Search List ───────────────────────────────────────────────────────

@Composable
private fun RecentSearchList(
    items: List<String>,
    onItemClick: (String) -> Unit,
    onItemRemove: (String) -> Unit,
) {
    Column {
        items.forEach { term ->
            RecentSearchItem(
                term = term,
                onClick = { onItemClick(term) },
                onRemove = { onItemRemove(term) },
            )
        }
    }
}

@Composable
private fun RecentSearchItem(
    term: String,
    onClick: () -> Unit,
    onRemove: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = term,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = InterFamily,
                color = SubtitleGray,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove $term",
                tint = FormLabel,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onRemove() },
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE5E2E1))
    }
}
