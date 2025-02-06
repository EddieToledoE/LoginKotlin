package com.ramirinter.practicando.network


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val message: String, val user:User)
data class CreateUserRequest(val email: String, val password: String, val username: String, val name: String)
data class CreateUserResponse(val message: String, val username: String)
data class Task(
    @SerializedName("_id") val id: String,
    val title: String,
    val description: String,
    val status: String,
)

data class CreateTaskRequest(
    val title: String,
    val description: String,
)

data class SearchFriendResponse(
    @SerializedName("_id") val id: String,
    val username: String
)

data class User(
    val username: String
)
data class UpdateTask(
    val title: String,
    val description: String,
    val status: String
)
interface ApiService {
    @POST("api/v1/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/v1/users/create")
    fun createUser(@Body request: CreateUserRequest): Call<CreateUserResponse>

    @GET("api/v1/users/friend/search")
    fun searchFriend(@Query("username") username: String): Call<List<SearchFriendResponse>>

    @GET("api/v1/tasks")
    fun getTasks(): Call<List<Task>>

    @DELETE("api/v1/tasks/{id}")
    fun deleteTask(@Path("id") id: String): Call<Unit>

    @PUT("api/v1/tasks/{id}")
    fun updateTask(@Path("id") id: String, @Body request: UpdateTask): Call<Task>

    @GET("api/v1/tasks/{id}")
    fun getTaskById(@Path("id") id: String): Call<Task>

    @POST("api/v1/tasks/create")
    fun createTask(@Body request: CreateTaskRequest): Call<Task>

}
