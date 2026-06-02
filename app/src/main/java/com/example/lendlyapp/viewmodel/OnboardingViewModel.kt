package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    fun onNavigateAway() {
        viewModelScope.launch { userPreferences.setHasSeenOnboarding(seen = true) }
    }
}
