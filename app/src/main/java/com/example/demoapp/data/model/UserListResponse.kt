package com.example.demoapp.data.model

data class UserListResponse(
    val users: List<User>,
    val total: Int,
    val skip: Int,
    val limit: Int
)