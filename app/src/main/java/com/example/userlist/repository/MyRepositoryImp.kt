package com.example.userlist.repository

import android.content.Context
import com.example.userlist.dao.UserDao
import com.example.userlist.model.NetworkConnectivity
import com.example.userlist.model.UserData
import com.example.userlist.remote.MyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyRepositoryImp  @Inject constructor(private val myApi: MyApi, private val dao: UserDao,private val context: Context ) : MyRepository{

    override suspend fun getUserList(): Flow<List<UserData>> {
        return if (NetworkConnectivity().isOnline(context = context)){
            flow {
                try {
                    myApi.getList().body()?.let { emit(it) }
                }catch (ex : Exception){
                    dao.getAllUserData()
                }
            }
        }else {
            dao.getAllUserData()
        }
    }

    override suspend fun addUser(userData: UserData) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.upSertUser(userData)
        }
        myApi.addUser(userData)
    }


}