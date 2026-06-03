package com.example.lendlyapp.di

import com.example.lendlyapp.auth.AuthRepository
import com.example.lendlyapp.auth.AuthRepositoryImpl
import com.example.lendlyapp.shared.LendlyApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
    fun provideLendlyApiService(retrofit: Retrofit): LendlyApiService =
        retrofit.create(LendlyApiService::class.java)
}
