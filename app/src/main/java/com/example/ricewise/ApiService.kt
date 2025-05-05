package com.example.ricewise
import retrofit2.http.*
import retrofit2.Call

interface ApiService {
    @POST("login")
    fun login(@Body credentials: Map<String, String>): Call<LoginResponse>

    @POST("users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("users/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @POST("users/reset-password")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<ResetPasswordResponse>
}