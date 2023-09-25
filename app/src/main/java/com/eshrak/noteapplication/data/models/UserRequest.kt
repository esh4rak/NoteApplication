package com.eshrak.noteapplication.data.models

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)