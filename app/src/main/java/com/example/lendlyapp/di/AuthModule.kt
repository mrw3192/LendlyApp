package com.example.lendlyapp.di

import android.content.Context
import com.example.lendlyapp.auth.AuthRepository
import com.example.lendlyapp.auth.AuthRepositoryImpl
import com.example.lendlyapp.data.local.UserPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context,
    ): UserPreferences = UserPreferences(context)
}
