package com.example.userlist.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.userlist.model.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    fun upSertUser(userData: UserData)

    @Query("SELECT * From userdata")
    fun getAllUserData() : Flow<List<UserData>>

}