package com.bengisusahin.e_commerce.service

import com.bengisusahin.e_commerce.data.dataProfile.Profile
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: Long): Profile
}