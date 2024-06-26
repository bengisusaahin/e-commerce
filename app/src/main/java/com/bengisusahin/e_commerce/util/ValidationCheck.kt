package com.bengisusahin.e_commerce.util

// this function is used to validate the email field
fun validateUsername(username: String): FieldValidation {
    return if (username.isEmpty()) {
        FieldValidation.Error("Username cannot be empty")
    }  else {
        FieldValidation.Success
    }
}

// this function is used to validate the password field
fun validatePassword(password: String): FieldValidation {
    return if (password.isEmpty()) {
        FieldValidation.Error("Password cannot be empty")
    } else if (password.length < 6) {
        FieldValidation.Error("Password must be at least 6 characters")
    } else {
        FieldValidation.Success
    }
}