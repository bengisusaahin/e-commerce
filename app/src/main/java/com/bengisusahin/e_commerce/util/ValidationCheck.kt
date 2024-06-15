package com.bengisusahin.e_commerce.util

fun validateEmail(email: String): FieldValidation {
    return if (email.isEmpty()) {
        FieldValidation.Error("Email cannot be empty")
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        FieldValidation.Error("Invalid email format")
    } else {
        FieldValidation.Success
    }
}

fun validatePassword(password: String): FieldValidation {
    return if (password.isEmpty()) {
        FieldValidation.Error("Password cannot be empty")
    } else if (password.length < 6) {
        FieldValidation.Error("Password must be at least 6 characters")
    } else {
        FieldValidation.Success
    }
}