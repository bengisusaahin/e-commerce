package com.bengisusahin.e_commerce.util

// This class is used to represent the state of the network response
sealed class NetworkResponseState<out T : Any> {
    object Loading : NetworkResponseState<Nothing>()
    data class Success<out T : Any>(val result: T) : NetworkResponseState<T>()
    data class Error(val exception: Exception) : NetworkResponseState<Nothing>()
}
