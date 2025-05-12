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

    @GET("variety/all")
    fun getAllVarieties(): Call<List<Variety>>

    @POST("variety")
    fun createVariety(@Body variety: Variety): Call<Variety>

    @GET("variety/{id}")
    fun getVarietyById(@Path("id") id: String): Call<Variety>

    @PUT("variety/{id}")
    fun updateVarietyById(@Path("id") id: String, @Body variety: Variety): Call<Variety>

    @DELETE("variety/{id}")
    fun deleteVarietyById(@Path("id") id: String): Call<Void>
}