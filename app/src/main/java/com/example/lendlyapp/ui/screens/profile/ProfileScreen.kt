package com.example.lendlyapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.ui.shared.LendlyLogo
import com.example.lendlyapp.ui.shared.LendlyTopBar
import com.example.lendlyapp.ui.theme.*
import com.example.lendlyapp.viewmodel.ProfileUiState
import com.example.lendlyapp.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToCreditScore: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Manage",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = MontserratFamily,
            color = FigmaLightText,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Currently using as",
            fontSize = 14.sp,
            color = SubtitleGray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card de Usuario reactiva
        UserAccountCard(uiState, onNavigateToEditProfile)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "General",
            fontSize = 14.sp,
            color = SubtitleGray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = FigmaLightBg, modifier = Modifier.padding(horizontal = 16.dp))

        // Lista de opciones según Figma
        ProfileMenuItem(Icons.Default.Person, "Account details", onClick = onNavigateToEditProfile)
        ProfileMenuItem(Icons.Default.Email, "Receiving by email or phone")
        ProfileMenuItem(Icons.Default.CalendarMonth, "Scheduled pay")
        ProfileMenuItem(Icons.Default.Speed, "Credit score", onClick = onNavigateToCreditScore)
        ProfileMenuItem(Icons.Default.Settings, "Settings")
        ProfileMenuItem(Icons.Default.Description, "Terms and Conditions")
        ProfileMenuItem(Icons.Default.HelpOutline, "Help")

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = FigmaLightBg)
        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuItem(
            icon = Icons.Default.Logout,
            title = "Log Out",
            onClick = onLogout,
            tint = FigmaLightText
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun UserAccountCard(uiState: ProfileUiState, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaLightBg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                val initials = if (uiState is ProfileUiState.Success) {
                    uiState.user.fullName.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("")
                } else "..."
                Text(initials, fontWeight = FontWeight.Bold, color = FigmaLightText)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                val name = if (uiState is ProfileUiState.Success) uiState.user.fullName else "Cargando..."
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Your personal Account", fontSize = 12.sp, color = SubtitleGray)
            }

            Button(
                onClick = onEdit,
                colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Edit", color = OnPrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {},
    tint: Color = FigmaLightText
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = tint)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = SubtitleGray)
    }
}
