package com.example.lendlyapp

import android.app.Application
import com.example.lendlyapp.core.ApiConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LendlyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiConfig.API_KEY = getString(R.string.api_key)
        ApiConfig.BASE_URL = getString(R.string.api_base_url)
    }
}
