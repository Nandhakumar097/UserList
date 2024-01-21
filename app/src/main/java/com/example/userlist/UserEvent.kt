package com.example.userlist

import com.example.userlist.model.SortType

sealed interface UserEvent {

    object saveUser : UserEvent

    data class setUserName(val userName : String) : UserEvent
    data class setEmail(val email : String) : UserEvent
    data class setMobile(val mobile : String) : UserEvent
    data class setGender(val gender : String) : UserEvent
    data class SortContacts(val sortType: SortType): UserEvent

    object showDialog : UserEvent
    object hideDialog : UserEvent
    object callUserList : UserEvent

}