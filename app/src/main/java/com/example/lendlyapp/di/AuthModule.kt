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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.lendlyapp.core.ApiConfig
import com.example.lendlyapp.core.AuthInterceptor
import com.example.lendlyapp.shared.LendlyApiService


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
    fun provideLendlyApiService(retrofit: Retrofit): LendlyApiService =
        retrofit.create(LendlyApiService::class.java)
}
