package com.example.userlist

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.userlist.dao.UserDao
import com.example.userlist.model.UserData

@Database(
    entities = [UserData::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase(){

    abstract val userDao : UserDao

}