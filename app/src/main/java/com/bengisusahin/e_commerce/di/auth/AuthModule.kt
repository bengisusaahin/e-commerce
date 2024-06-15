package com.bengisusahin.e_commerce.di.auth

import com.bengisusahin.e_commerce.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

// SingletonComponent: all the dependencies inside this module will stay alive as long as the app is alive
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    //HomeViewModel.kt will use this dependency and inject it into its constructor
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService{
        return retrofit.create(AuthService::class.java)
    }
}