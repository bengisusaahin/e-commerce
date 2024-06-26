package com.bengisusahin.e_commerce.util

// Field validation state for form fields
sealed class FieldValidation() {
    object Success: FieldValidation()
    data class Error(val message: String): FieldValidation()
}

data class FormState(
    val usernameError: FieldValidation,
    val passwordError: FieldValidation
)