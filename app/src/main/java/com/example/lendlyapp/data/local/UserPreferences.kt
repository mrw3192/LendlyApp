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
import dagger.hilt.android.qualifiers.ApplicationContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "lendly_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        val AUTH_TOKEN          = stringPreferencesKey("auth_token")
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
        val USER_ID             = stringPreferencesKey("user_id")
    }

    val authToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[AUTH_TOKEN]
    }

    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[HAS_SEEN_ONBOARDING] ?: false
    }

    val userId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_ID]
    }

    suspend fun setHasSeenOnboarding(seen: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[HAS_SEEN_ONBOARDING] = seen
        }
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[AUTH_TOKEN] = token
        }
    }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN)
            prefs.remove(USER_ID)
        }
    }

    suspend fun clearAuthToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN)
        }
    }
}
