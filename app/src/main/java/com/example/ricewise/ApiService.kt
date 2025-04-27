package com.example.ricewise
import retrofit2.http.*
import retrofit2.Call

interface ApiService {
    @POST("login")
    fun login(@Body credentials: Map<String, String>): Call<LoginResponse>
}