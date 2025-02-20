package com.bengisusahin.e_commerce.di.usecase.profile

import com.bengisusahin.e_commerce.data.dataProfile.Profile
import com.bengisusahin.e_commerce.repository.ProfileRepository
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// This class is a use case class that is used to update the profile of a user
class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(userId: Long, profile: Profile) : Flow<ResourceResponseState<Profile>> {
        return profileRepository.updateProfile(userId, profile)
    }
}