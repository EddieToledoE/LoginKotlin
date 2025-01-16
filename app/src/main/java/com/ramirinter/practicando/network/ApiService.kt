package com.ramirinter.practicando.network


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val message: String, val user:User)
data class User(
    val username: String
)
interface ApiService {
    @POST("api/v1/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
