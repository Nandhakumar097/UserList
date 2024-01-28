package com.example.userlist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.userlist.model.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertUser(userData: UserData)

    @Query("SELECT * From userdata")
    fun getAllUserData() : Flow<List<UserData>>

}