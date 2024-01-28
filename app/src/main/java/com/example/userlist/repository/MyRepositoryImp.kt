package com.example.userlist.repository

import android.content.Context
import android.widget.Toast
import androidx.room.withTransaction
import com.example.userlist.UserDatabase
import com.example.userlist.model.UserData
import com.example.userlist.remote.MyApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyRepositoryImp  @Inject constructor(private val myApi: MyApi, private val userDatabase: UserDatabase,private val context: Context ) : MyRepository{

    override suspend fun getUserList(): Flow<List<UserData>> {
        return userDatabase.userDao.getAllUserData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun addUser(userData: UserData){
        userDatabase.withTransaction {
            try {
                val response =  myApi.addUser(userData)
                if (response.isSuccessful){
                    userDatabase.userDao.upSertUser(userData)
                }else {
                    Toast.makeText(context, "Failed to Add User", Toast.LENGTH_SHORT).show()
                }
            }catch (ex : Exception){
                Toast.makeText(context, "Failed to Add User", Toast.LENGTH_SHORT).show()
            }
        }
    }


}