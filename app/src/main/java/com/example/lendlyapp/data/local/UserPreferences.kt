package com.example.lendlyapp.data.local

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val context: Context,
) {
    private val prefs = context.getSharedPreferences("lendly_prefs", Context.MODE_PRIVATE)

    val authToken: String?         get() = prefs.getString("auth_token", null)
    val hasSeenOnboarding: Boolean get() = prefs.getBoolean("has_seen_onboarding", false)
    val userId: String?            get() = prefs.getString("user_id", null)

    fun setHasSeenOnboarding(seen: Boolean) {
        prefs.edit().putBoolean("has_seen_onboarding", seen).apply()
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun saveUserId(id: String) {
        prefs.edit().putString("user_id", id).apply()
    }

    fun clearSession() {
        prefs.edit().remove("auth_token").remove("user_id").apply()
    }

    fun clearAuthToken() {
        prefs.edit().remove("auth_token").apply()
    }
}
