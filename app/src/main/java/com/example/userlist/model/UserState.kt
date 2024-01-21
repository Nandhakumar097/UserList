package com.example.userlist.model

data class UserState(
        var userList: List<UserData> = emptyList(),
        val userName: String = "",
        val email: String = "",
        val mobile: String = "",
        val gender: String = "",
        val isAddingUser: Boolean = false,
        val sortType: SortType = SortType.NAME
)
