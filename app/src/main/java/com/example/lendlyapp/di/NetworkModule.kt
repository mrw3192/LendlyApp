package com.example.lendlyapp.di

import android.content.Context
import com.example.lendlyapp.auth.AuthRepository
import com.example.lendlyapp.auth.AuthRepositoryImpl
import com.example.lendlyapp.core.ApiConfig
import com.example.lendlyapp.core.ApiKeyInterceptor
import com.example.lendlyapp.core.AuthInterceptor
import com.example.lendlyapp.data.local.UserPreferences
import com.example.lendlyapp.shared.AuthApi
import com.example.lendlyapp.shared.LendlyApiService
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

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor) // Use the object directly
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLendlyApiService(retrofit: Retrofit): LendlyApiService {
        return retrofit.create(LendlyApiService::class.java)
    }
}
