package com.example.lendlyapp.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.InterFamily

@Composable
fun LendlyTopBar(
    onNavigationClick: () -> Unit,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    navigationIconContent: (@Composable () -> Unit)? = null,
    title: String = "",
    centerContent: (@Composable BoxScope.() -> Unit)? = null,
    trailingContent: @Composable RowScope.() -> Unit = { Spacer(Modifier.size(48.dp)) },
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onNavigationClick, modifier = Modifier.size(48.dp)) {
            if (navigationIconContent != null) {
                navigationIconContent()
            } else {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = null,
                    tint = FigmaLightText,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            if (centerContent != null) {
                centerContent()
            } else if (title.isNotEmpty()) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    color = FigmaLightText,
                )
            }
        }

        trailingContent()
    }
}
