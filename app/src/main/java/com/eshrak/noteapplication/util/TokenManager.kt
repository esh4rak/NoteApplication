package com.eshrak.noteapplication.util

import android.content.Context
import com.eshrak.noteapplication.util.Constants.PREFS_TOKEN_FILE
import com.eshrak.noteapplication.util.Constants.USER_EMAIL
import com.eshrak.noteapplication.util.Constants.USER_NAME
import com.eshrak.noteapplication.util.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {


    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String, username: String, email: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_NAME, username)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }


    fun getUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)

    }

}