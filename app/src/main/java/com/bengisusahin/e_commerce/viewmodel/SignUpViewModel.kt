package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import com.bengisusahin.e_commerce.data.User
import com.bengisusahin.e_commerce.util.Resource
import com.bengisusahin.e_commerce.util.SignUpFormState
import com.bengisusahin.e_commerce.util.SignUpValidation
import com.bengisusahin.e_commerce.util.validateEmail
import com.bengisusahin.e_commerce.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

// ViewModel for handling sign up operations
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _signUpState = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val signUpState : Flow<Resource<FirebaseUser>> = _signUpState

    private val _signUpFormState = Channel<SignUpFormState>()
    val signUpFormState = _signUpFormState.receiveAsFlow()
    fun createUserWithEmailAndPassword(user: User, password: String) {
        if(checkValidation(user, password)) {
            runBlocking {
                _signUpState.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let { user ->
                        _signUpState.value = Resource.Success(user)
                    }
                }.addOnFailureListener {
                    _signUpState.value = Resource.Error(it.message.toString())
                }
        }else{
            val signUpFormState = SignUpFormState(
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _signUpFormState.send(signUpFormState)
            }
        }
    }

    private fun checkValidation(user: User, password: String) : Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidation is SignUpValidation.Success &&
                passwordValidation is SignUpValidation.Success
        return shouldRegister
    }
}