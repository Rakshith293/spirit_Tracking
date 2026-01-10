package com.prathik.smartworkforce.auth

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID = "user_id"
        private const val USER_TYPE = "user_type"
    }

    fun saveSession(userId: String, userType: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, userId)
        editor.putString(USER_TYPE, userType)
        editor.apply()
    }

    fun getUserId(): String? = prefs.getString(USER_ID, null)

    fun getUserType(): String? = prefs.getString(USER_TYPE, null)

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
