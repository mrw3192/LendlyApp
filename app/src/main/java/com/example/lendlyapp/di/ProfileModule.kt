package com.example.lendlyapp.di

import com.example.lendlyapp.profile.ProfileRepository import com.example.lendlyapp.profile.ProfileRepositoryImpl import com.example.lendlyapp.shared.LendlyApiService import dagger.Module import dagger.Provides import dagger.hilt.InstallIn import dagger.hilt.components.SingletonComponent import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class) object ProfileModule {
    @Provides
    @Singleton
    fun provideProfileRepository(
        apiService: LendlyApiService
    ): ProfileRepository {
        return ProfileRepositoryImpl(apiService)
    }
}