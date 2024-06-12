package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel(){
    private val _loginState = MutableSharedFlow<Resource<FirebaseUser>>()
    val loginState = _loginState.asSharedFlow()

    fun loginWithEmailAndPassword(email: String, password: String){
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
    }
}