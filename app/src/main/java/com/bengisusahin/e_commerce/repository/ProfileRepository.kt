package com.bengisusahin.e_commerce.repository

import com.bengisusahin.e_commerce.data.dataProfile.Profile
import com.bengisusahin.e_commerce.service.ProfileService
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileService: ProfileService
){
    fun getProfile(userId: Long) : Flow<ResourceResponseState<Profile>> = flow {
        emit(ResourceResponseState.Loading())
        val profile = profileService.getUserProfile(userId)
        emit(ResourceResponseState.Success(profile))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }

    fun updateProfile(userId: Long, profile: Profile) : Flow<ResourceResponseState<Profile>> = flow {
        emit(ResourceResponseState.Loading())
        val updatedProfile = profileService.updateUserProfile(userId, profile)
        emit(ResourceResponseState.Success(updatedProfile))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
    }
}