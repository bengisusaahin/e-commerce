package com.bengisusahin.e_commerce.util

sealed class SignUpValidation() {
    object Success: SignUpValidation()
    data class Error(val message: String): SignUpValidation()
}

data class SignUpFormState(
    val emailError: SignUpValidation,
    val passwordError: SignUpValidation
)