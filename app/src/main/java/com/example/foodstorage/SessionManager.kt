package com.example.foodstorage

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)

    fun createSession(email: String) {
        prefs.edit().apply {
            putBoolean("IS_LOGGED_IN", true)
            putString("USER_EMAIL", email)
            apply()
        }
    }

    fun logoutUser() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("IS_LOGGED_IN", false)
    fun getCurrentEmail(): String = prefs.getString("USER_EMAIL", "") ?: ""
}