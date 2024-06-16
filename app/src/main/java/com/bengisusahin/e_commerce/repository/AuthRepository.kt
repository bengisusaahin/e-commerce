package com.bengisusahin.e_commerce.repository

import com.bengisusahin.e_commerce.data.dataAuth.LoginRequest
import com.bengisusahin.e_commerce.data.dataAuth.User
import com.bengisusahin.e_commerce.service.AuthService
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
){
    fun login(loginRequest: LoginRequest): Flow<ResourceResponseState<User>> = flow {
        emit(ResourceResponseState.Loading())
            val response = authService.login(loginRequest)
            emit(ResourceResponseState.Success(response))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }
}