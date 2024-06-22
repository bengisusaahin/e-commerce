package com.bengisusahin.e_commerce.di.usecase.profile

import com.bengisusahin.e_commerce.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
   private val profileRepository: ProfileRepository
){
    suspend operator fun invoke(userId: Long) = profileRepository.getProfile(userId)

}