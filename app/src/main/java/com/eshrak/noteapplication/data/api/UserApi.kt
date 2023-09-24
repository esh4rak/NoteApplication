package com.eshrak.noteapplication.data.api

import com.eshrak.noteapplication.data.models.UserRequest
import com.eshrak.noteapplication.data.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("/users/signup")
    fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("/users/signin")
    fun signIn(@Body userRequest: UserRequest): Response<UserResponse>

}