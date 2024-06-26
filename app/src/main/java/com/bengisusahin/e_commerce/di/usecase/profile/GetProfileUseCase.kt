package com.bengisusahin.e_commerce.di.usecase.profile

import com.bengisusahin.e_commerce.repository.ProfileRepository
import javax.inject.Inject

// This class is a use case class for getting a profile from the repository
class GetProfileUseCase @Inject constructor(
   private val profileRepository: ProfileRepository
){
    operator fun invoke(userId: Long) = profileRepository.getProfile(userId)
}