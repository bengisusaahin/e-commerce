package com.bengisusahin.e_commerce.util

// This class is used to represent the state of the screen in the UI
sealed class ScreenState<out T : Any> {
    object Loading : ScreenState<Nothing>()
    data class Error(val message: String) : ScreenState<Nothing>()
    data class Success<out T : Any>(val uiData: T) : ScreenState<T>()
}