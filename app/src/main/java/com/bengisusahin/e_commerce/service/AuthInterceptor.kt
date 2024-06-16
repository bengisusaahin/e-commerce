package com.bengisusahin.e_commerce.service

import com.bengisusahin.e_commerce.util.SharedPrefManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sharedPrefManager: SharedPrefManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        sharedPrefManager.fetchAuthToken()?.let { token ->
            builder.addHeader("Authorization", "Bearer $token")
        }
        val request = builder.build()
        return chain.proceed(request)
    }
}