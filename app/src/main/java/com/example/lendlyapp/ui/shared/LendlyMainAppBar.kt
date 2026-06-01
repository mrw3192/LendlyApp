package com.example.lendlyapp.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText

@Composable
fun LendlyMainAppBar(
    onAvatarClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onAvatarClick() },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(FigmaLightBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = FigmaLightText,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            LendlyLogo(size = DpSize(58.dp, 20.dp))
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onNotificationsClick() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = FigmaLightText,
            )
        }
    }
}
