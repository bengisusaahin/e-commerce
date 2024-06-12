package com.bengisusahin.e_commerce.util

sealed class FieldValidation() {
    object Success: FieldValidation()
    data class Error(val message: String): FieldValidation()
}

data class FormState(
    val emailError: FieldValidation,
    val passwordError: FieldValidation
)