package com.example.demoapp.data.repository.impl

import com.example.demoapp.api.ApiService
import com.example.demoapp.data.model.User
import com.example.demoapp.data.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: ApiService
) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = userService.getUsers()
            Result.success(response.users)
        } catch (e: Exception) {
            // Handle network or parsing errors gracefully
            Result.failure(e)
        }
    }

    override suspend fun addUser(user: User): Result<User> {
        return try {
            val response = userService.addUser(user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}