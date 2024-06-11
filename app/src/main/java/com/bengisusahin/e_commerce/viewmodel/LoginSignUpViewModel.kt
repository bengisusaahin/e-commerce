package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import com.bengisusahin.e_commerce.data.User
import com.bengisusahin.e_commerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

// ViewModel for handling login and sign up operations
@HiltViewModel
class LoginSignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _signUpState = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val signUpState : Flow<Resource<FirebaseUser>> = _signUpState
    fun createUserWithEmailAndPassword(user: User, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener {
                it.user?.let { user ->
                    _signUpState.value = Resource.Success(user)
                }
            }.addOnFailureListener {
                _signUpState.value = Resource.Error(it.message.toString())
            }
    }
}