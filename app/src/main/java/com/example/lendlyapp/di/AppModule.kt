package com.example.lendlyapp.di

import android.content.Context
import com.example.lendlyapp.data.local.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the [UserPreferences] singleton backed by Preferences DataStore.
     * The ApplicationContext is injected by Hilt so the DataStore outlives any Activity.
     */
    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context,
    ): UserPreferences = UserPreferences(context)
}
