package com.example.lendlyapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore instance is created once per application context (top-level extension property).
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "lendly_prefs")

/**
 * DataStore-backed preferences repository.
 *
 * Keys (SPEC_TECNICO §3):
 *  - [AUTH_TOKEN]           → Bearer token for API requests.
 *  - [HAS_SEEN_ONBOARDING]  → false on first install → shows Onboarding; true → skips to Login.
 *
 * Injected as a @Singleton via [com.example.lendlyapp.di.AppModule].
 */
@Singleton
class UserPreferences @Inject constructor(
    private val context: Context,
) {
    companion object {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
    }

    /** Emits the current auth token, or null if not set. */
    val authToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[AUTH_TOKEN]
    }

    /**
     * Emits whether the user has already seen the onboarding flow.
     * Defaults to false (= show onboarding) on fresh install.
     */
    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[HAS_SEEN_ONBOARDING] ?: false
    }

    /** Persists the has-seen-onboarding flag. Called before navigating away from Onboarding. */
    suspend fun setHasSeenOnboarding(seen: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[HAS_SEEN_ONBOARDING] = seen
        }
    }

    /** Saves the Bearer token returned by POST /auth/login. */
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[AUTH_TOKEN] = token
        }
    }

    /** Removes the auth token (used on logout). */
    suspend fun clearAuthToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN)
        }
    }
}
