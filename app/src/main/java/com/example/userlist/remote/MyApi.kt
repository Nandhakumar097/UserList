package com.example.userlist.remote

import com.example.userlist.model.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface MyApi {

    @GET("user")
    suspend fun getList() : Response<List<UserData>>

    @POST("user")
    suspend fun addUser(@Body userData: UserData) : Response<UserData>

}