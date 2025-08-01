package com.example.demoapp.data.repository

import com.example.demoapp.data.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun addUser(user: User): Result<User>
}