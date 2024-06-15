package com.bengisusahin.e_commerce.util

// API response state
sealed class ResourceResponseState<T> (val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): ResourceResponseState<T>(data)
    class Error<T>(message: String): ResourceResponseState<T>(message = message)
    class Loading<T>: ResourceResponseState<T>()
}