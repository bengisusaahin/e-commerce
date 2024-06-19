package com.bengisusahin.e_commerce.di.usecase.auth

import com.bengisusahin.e_commerce.data.dataAuth.LoginRequest
import com.bengisusahin.e_commerce.data.dataAuth.User
import com.bengisusahin.e_commerce.repository.AuthRepository
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    operator fun invoke(loginRequest: LoginRequest): Flow<ResourceResponseState<User>> {
        return authRepository.login(loginRequest)
    }
}