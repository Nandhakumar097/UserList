package com.example.userlist.repository

import com.example.userlist.model.UserData
import kotlinx.coroutines.flow.Flow

interface MyRepository {

    suspend fun getUserList(): Flow<List<UserData>>
    suspend fun addUser(userData: UserData)
}