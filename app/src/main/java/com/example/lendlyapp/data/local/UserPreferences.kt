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
        // Returning User keys
        val REMEMBERED_NAME = stringPreferencesKey("remembered_name")
        val REMEMBERED_PHONE = stringPreferencesKey("remembered_phone")
        val REMEMBERED_EMAIL = stringPreferencesKey("remembered_email")
        val REMEMBERED_AVATAR = stringPreferencesKey("remembered_avatar")
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

    // ── Remembered user flows ───────────────────────────────────────────
    val rememberedName: Flow<String?> = context.dataStore.data.map { it[REMEMBERED_NAME] }
    val rememberedPhone: Flow<String?> = context.dataStore.data.map { it[REMEMBERED_PHONE] }
    val rememberedEmail: Flow<String?> = context.dataStore.data.map { it[REMEMBERED_EMAIL] }
    val rememberedAvatar: Flow<String?> = context.dataStore.data.map { it[REMEMBERED_AVATAR] }

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

    /** Saves the last-logged-in user profile for the Returning User login variant. */
    suspend fun saveRememberedUser(name: String, phone: String, email: String, avatar: String?) {
        context.dataStore.edit { prefs ->
            prefs[REMEMBERED_NAME] = name
            prefs[REMEMBERED_PHONE] = phone
            prefs[REMEMBERED_EMAIL] = email
            if (avatar != null) prefs[REMEMBERED_AVATAR] = avatar
            else prefs.remove(REMEMBERED_AVATAR)
        }
    }

    /** Clears the remembered user data (e.g. on explicit logout). */
    suspend fun clearRememberedUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(REMEMBERED_NAME)
            prefs.remove(REMEMBERED_PHONE)
            prefs.remove(REMEMBERED_EMAIL)
            prefs.remove(REMEMBERED_AVATAR)
        }
    }
}
