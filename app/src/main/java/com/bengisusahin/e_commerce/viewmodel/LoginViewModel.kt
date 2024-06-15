package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataAuth.LoginRequest
import com.bengisusahin.e_commerce.data.dataAuth.User
import com.bengisusahin.e_commerce.service.AuthService
import com.bengisusahin.e_commerce.util.FieldValidation
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.FormState
import com.bengisusahin.e_commerce.util.validateUsername
import com.bengisusahin.e_commerce.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel(){
    private val _loginState = MutableSharedFlow<ResourceResponseState<User>>()
    val loginState = _loginState.asSharedFlow()

    private val _loginFormState = Channel<FormState>()
    val loginFormState = _loginFormState.receiveAsFlow()

    fun login(username: String, password: String){
        val usernameValidation = validateUsername(username)
        val passwordValidation = validatePassword(password)

        if (usernameValidation is FieldValidation.Success &&
            passwordValidation is FieldValidation.Success) {
            viewModelScope.launch {
                _loginState.emit(ResourceResponseState.Loading())
                try {
                    val response = authService.login(LoginRequest(username, password))
                    viewModelScope.launch {
                        _loginState.emit(ResourceResponseState.Success(response))
                    }
                } catch (e: Exception) {
                    viewModelScope.launch {
                        _loginState.emit(ResourceResponseState.Error(e.message.toString()))
                    }
                }
            }
        }else{
            val loginFormState = FormState(usernameValidation, passwordValidation)
            runBlocking{
                _loginFormState.send(loginFormState)
            }
        }
    }
}