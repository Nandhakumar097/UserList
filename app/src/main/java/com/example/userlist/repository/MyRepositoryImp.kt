package com.example.userlist.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.userlist.dao.UserDao
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
        return if (isOnline(context = context)){
            flow {
                myApi.getList().body()?.let { emit(it) }
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


    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}