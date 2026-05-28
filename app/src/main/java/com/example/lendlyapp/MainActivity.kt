package com.example.lendlyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.lendlyapp.navigation.AppNavigation
import com.example.lendlyapp.ui.theme.LendlyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enableEdgeToEdge draws content behind status bar and navigation bar.
        // Each screen manages its own insets / status bar icon colour.
        enableEdgeToEdge()

        setContent {
            // dynamicColor = false → enforces Figma colour tokens on all API levels.
            LendlyAppTheme(dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
