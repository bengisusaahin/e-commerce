package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataAuth.LoginRequest
import com.bengisusahin.e_commerce.data.dataAuth.User
import com.bengisusahin.e_commerce.di.usecase.auth.LoginUseCase
import com.bengisusahin.e_commerce.util.FieldValidation
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.FormState
import com.bengisusahin.e_commerce.util.SharedPrefManager
import com.bengisusahin.e_commerce.util.validateUsername
import com.bengisusahin.e_commerce.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {
    private val _loginState = MutableSharedFlow<ResourceResponseState<User>>()
    val loginState = _loginState.asSharedFlow()

    private val _loginFormState = MutableSharedFlow<FormState>()
    val loginFormState = _loginFormState.asSharedFlow()

    fun login(username: String, password: String, rememberMe: Boolean) {
        val usernameValidation = validateUsername(username)
        val passwordValidation = validatePassword(password)

        if (usernameValidation is FieldValidation.Success &&
            passwordValidation is FieldValidation.Success) {
            viewModelScope.launch {
                _loginState.emit(ResourceResponseState.Loading())
                try {
                    loginUseCase(LoginRequest(username, password)).collect { response ->
                        _loginState.emit(response)
                        if (response is ResourceResponseState.Success) {
                            if (rememberMe) {
                                sharedPrefManager.saveRememberMe(true)
                                sharedPrefManager.saveUsername(username)
                                sharedPrefManager.savePassword(password)
                            } else {
                                sharedPrefManager.saveRememberMe(false)
                                sharedPrefManager.saveUsername("")
                                sharedPrefManager.savePassword("")
                            }
                        }
                    }
                } catch (e: HttpException) {
                    val errorMessage = when (e.code()) {
                        404 -> "No found user"
                        401,403 -> "Wrong password or username"
                        else -> "An error occurred"
                    }
                    _loginState.emit(ResourceResponseState.Error(errorMessage))
                } catch (e: Exception) {
                    _loginState.emit(ResourceResponseState.Error(e.message.toString()))
                }
            }
        } else {
            val loginFormState = FormState(usernameValidation, passwordValidation)
            viewModelScope.launch {
                _loginFormState.emit(loginFormState)
            }
        }
    }
}
