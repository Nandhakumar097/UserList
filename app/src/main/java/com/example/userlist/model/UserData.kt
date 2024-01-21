package com.example.userlist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var name: String,
        var email: String,
        var mobile: String,
        var gender: String
)

