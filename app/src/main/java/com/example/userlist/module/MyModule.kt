package com.example.userlist.module

import android.content.Context
import androidx.room.Room
import com.example.userlist.UserDatabase
import com.example.userlist.remote.MyApi
import com.example.userlist.repository.MyRepository
import com.example.userlist.repository.MyRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModule {

    @Singleton
    @Provides
    fun provideMyApi() : MyApi{
        return Retrofit.Builder()
            .baseUrl("https://crudcrud.com/api/57660137e39644889066017b054093a5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMyRepository(myApi: MyApi, userDatabase: UserDatabase,@ApplicationContext appContext: Context) : MyRepository{
        return MyRepositoryImp(myApi,userDatabase,appContext)
    }

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "userdatabase.db"
        ).build()
    }

}