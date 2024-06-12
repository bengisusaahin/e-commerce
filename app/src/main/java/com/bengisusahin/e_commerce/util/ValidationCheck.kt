package com.bengisusahin.e_commerce.util

fun validateEmail(email: String): SignUpValidation {
    return if (email.isEmpty()) {
        SignUpValidation.Error("Email cannot be empty")
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        SignUpValidation.Error("Invalid email format")
    } else {
        SignUpValidation.Success
    }
}

fun validatePassword(password: String): SignUpValidation {
    return if (password.isEmpty()) {
        SignUpValidation.Error("Password cannot be empty")
    } else if (password.length < 6) {
        SignUpValidation.Error("Password must be at least 6 characters")
    } else {
        SignUpValidation.Success
    }
}