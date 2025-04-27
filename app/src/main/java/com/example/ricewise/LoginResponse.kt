package com.example.ricewise

data class LoginResponse(
    val message: String,
    val user: User
)

data class User(
    val id: String,
    val email: String
)