package com.bengisusahin.e_commerce.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject

class SharedPrefManager@Inject constructor(
    context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPrefsFile", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("KEY_AUTH_TOKEN", token)
        editor.apply()

        Log.d("SharedPrefManager", "saveAuthToken: $token")
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString("KEY_AUTH_TOKEN", null)
    }

    fun saveRememberMe(rememberMe: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("KEY_REMEMBER_ME", rememberMe)
        editor.apply()

        Log.d("SharedPrefManager", "saveRememberMe: $rememberMe")
    }

    fun fetchRememberMe(): Boolean {
        return sharedPreferences.getBoolean("KEY_REMEMBER_ME", false)
    }

    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("KEY_USERNAME", username)
        editor.apply()
    }

    fun fetchUsername(): String? {
        return sharedPreferences.getString("KEY_USERNAME", "")
    }

    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("KEY_PASSWORD", password)
        editor.apply()
    }

    fun fetchPassword(): String? {
        return sharedPreferences.getString("KEY_PASSWORD", "")
    }

    fun saveUserImage(userImage: String) {
        val editor = sharedPreferences.edit()
        editor.putString("KEY_USER_IMAGE", userImage)
        editor.apply()
    }

    fun fetchUserImage(): String? {
        return sharedPreferences.getString("KEY_USER_IMAGE", "")
    }
}