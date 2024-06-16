package com.bengisusahin.e_commerce.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPrefsFile", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("KEY_AUTH_TOKEN", token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString("KEY_AUTH_TOKEN", null)
    }
}