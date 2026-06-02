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

        enableEdgeToEdge()

        setContent {
            LendlyAppTheme(dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
