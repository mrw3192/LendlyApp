package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lendlyapp.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    fun onNavigateAway() {
        userPreferences.setHasSeenOnboarding(seen = true)
    }
}
