package com.bengisusahin.e_commerce.repository

import com.bengisusahin.e_commerce.data.dataAuth.LoginRequest
import com.bengisusahin.e_commerce.data.dataAuth.User
import com.bengisusahin.e_commerce.service.AuthService
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

// This class is responsible for handling the authentication operations
class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val sharedPrefManager: SharedPrefManager
){
    fun login(loginRequest: LoginRequest): Flow<ResourceResponseState<User>> = flow {
        emit(ResourceResponseState.Loading())
            val response = authService.login(loginRequest)
            sharedPrefManager.saveAuthToken(response.token)
            emit(ResourceResponseState.Success(response))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }
    fun getCurrentUser(): Flow<User> = flow {
        emit(authService.getCurrentUser())
    }.flowOn(Dispatchers.IO)
}