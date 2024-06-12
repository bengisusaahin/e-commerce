package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.util.FieldValidation
import com.bengisusahin.e_commerce.util.Resource
import com.bengisusahin.e_commerce.util.FormState
import com.bengisusahin.e_commerce.util.validateEmail
import com.bengisusahin.e_commerce.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    private val firebaseAuth: FirebaseAuth
): ViewModel(){
    private val _loginState = MutableSharedFlow<Resource<FirebaseUser>>()
    val loginState = _loginState.asSharedFlow()

    private val _loginFormState = Channel<FormState>()
    val loginFormState = _loginFormState.receiveAsFlow()

    private val _resetPasswordState = MutableSharedFlow<Resource<String>>()
    val resetPasswordState = _resetPasswordState.asSharedFlow()

    fun loginWithEmailAndPassword(email: String, password: String){
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        if (emailValidation is FieldValidation.Success &&
            passwordValidation is FieldValidation.Success) {
            viewModelScope.launch { _loginState.emit(Resource.Loading()) }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let { user ->
                            _loginState.emit(Resource.Success(user))
                        }
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _loginState.emit(Resource.Error(it.message.toString()))
                    }
                }
        }else{
            val loginFormState = FormState(emailValidation, passwordValidation)
            runBlocking {
                _loginFormState.send(loginFormState)
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPasswordState.emit(Resource.Loading())
        }
        firebaseAuth
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _resetPasswordState.emit(Resource.Success(email))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _resetPasswordState.emit(Resource.Error(it.message.toString()))
                }
            }
        }
    }