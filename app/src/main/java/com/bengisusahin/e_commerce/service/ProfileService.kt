package com.bengisusahin.e_commerce.service

import com.bengisusahin.e_commerce.data.dataProfile.Profile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// This interface is used to define the API calls for the user profile
interface ProfileService {
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: Long): Profile
    @PUT("users/{id}")
    suspend fun updateUserProfile(@Path("id") id: Long, @Body profile: Profile): Profile
}