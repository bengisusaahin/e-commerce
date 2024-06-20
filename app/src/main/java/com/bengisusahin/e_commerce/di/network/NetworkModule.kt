package com.bengisusahin.e_commerce.di.network

import com.bengisusahin.e_commerce.service.AuthInterceptor
import com.bengisusahin.e_commerce.service.AuthService
import com.bengisusahin.e_commerce.service.CartService
import com.bengisusahin.e_commerce.service.ProductService
import com.bengisusahin.e_commerce.util.Constants
import com.bengisusahin.e_commerce.util.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPrefManager: SharedPrefManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sharedPrefManager))
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
    }
    // HomeViewModel.kt will use this dependency and inject it into its constructor
    // create retrofit instance and provide AuthService
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
    @Provides
    @Singleton
    fun provideProductService(retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }

    @Provides
    @Singleton
    fun provideCartService(retrofit: Retrofit): CartService {
        return retrofit.create(CartService::class.java)
    }
}