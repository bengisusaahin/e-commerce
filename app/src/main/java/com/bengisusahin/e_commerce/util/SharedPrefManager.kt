package com.bengisusahin.e_commerce.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject

class SharedPrefManager@Inject constructor(
    context: Context) {
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
}