package com.bengisusahin.e_commerce.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// SingletonComponent: all the dependencies inside this module will stay alive as long as the app is alive
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    //SignUpViewModel.kt will use this dependency and inject it into its constructor
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()
}