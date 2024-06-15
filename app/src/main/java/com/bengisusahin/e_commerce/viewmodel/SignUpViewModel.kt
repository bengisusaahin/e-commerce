package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import com.bengisusahin.e_commerce.data.User
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.FormState
import com.bengisusahin.e_commerce.util.FieldValidation
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

    private val _signUpState = MutableStateFlow<ResourceResponseState<FirebaseUser>>(ResourceResponseState.Loading())
    val signUpState : Flow<ResourceResponseState<FirebaseUser>> = _signUpState

    private val _signUpFormState = Channel<FormState>()
    val signUpFormState = _signUpFormState.receiveAsFlow()
    fun createUserWithEmailAndPassword(user: User, password: String) {
        if(checkValidation(user, password)) {
            runBlocking {
                _signUpState.emit(ResourceResponseState.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let { user ->
                        _signUpState.value = ResourceResponseState.Success(user)
                    }
                }.addOnFailureListener {
                    _signUpState.value = ResourceResponseState.Error(it.message.toString())
                }
        }else{
            val signUpFormState = FormState(
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
        val shouldRegister = emailValidation is FieldValidation.Success &&
                passwordValidation is FieldValidation.Success
        return shouldRegister
    }
}