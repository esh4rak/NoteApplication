package com.eshrak.noteapplication.data.repository

import android.util.Log
import com.eshrak.noteapplication.data.api.UserAPI
import com.eshrak.noteapplication.data.models.UserRequest
import com.eshrak.noteapplication.util.Constants.TAG
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    suspend fun registerUser(userRequest: UserRequest) {
        val response = userAPI.signUp(userRequest)
        Log.d(TAG, response.body().toString())
    }


    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signIn(userRequest)
        Log.d(TAG, response.body().toString())
    }


}