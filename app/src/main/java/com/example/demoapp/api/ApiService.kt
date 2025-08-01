package com.example.demoapp.api

import com.example.demoapp.data.model.User
import com.example.demoapp.data.model.UserListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("users")
    suspend fun getUsers(): UserListResponse

    @POST("users/add")
    suspend fun addUser(@Body user: User): User
}