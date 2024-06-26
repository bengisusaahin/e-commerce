package com.bengisusahin.e_commerce.service

import com.bengisusahin.e_commerce.data.dataAuth.LoginRequest
import com.bengisusahin.e_commerce.data.dataAuth.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Service interface for the authentication operations
interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): User

    @GET("auth/me")
    suspend fun getCurrentUser(): User
}