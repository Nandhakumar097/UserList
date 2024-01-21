package com.example.userlist.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userlist.UserEvent
import com.example.userlist.model.SortType
import com.example.userlist.model.UserData
import com.example.userlist.model.UserState
import com.example.userlist.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private var myRepository : MyRepository, @ApplicationContext private var context: Context) : ViewModel(){

    private val _state = MutableStateFlow(UserState())

    private val _sortType = MutableStateFlow(SortType.NAME)

    private var _users = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.NAME -> myRepository.getUserList()
                else -> flow { myRepository.getUserList() }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sortType, _users) { state, sortType, users ->
        state.copy(
            userList = users,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), UserState())


    fun onEvent(event: UserEvent){
        when(event){

            UserEvent.hideDialog -> {
                _state.update { it.copy(
                    isAddingUser = false
                ) }
            }
            UserEvent.saveUser -> {
                val userName = _state.value.userName
                val email = _state.value.email
                val mobile = _state.value.mobile
                val gender = _state.value.gender

                if(userName.isBlank() || email.isBlank() || mobile.isBlank() || gender.isBlank()) {
                    Toast.makeText(context, "Please Fill all Fields", Toast.LENGTH_SHORT).show()
                    return
                }
                if (!email.endsWith("@gmail.com")){
                    Toast.makeText(context, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show()
                    return
                }
                if (mobile.length < 10){
                    Toast.makeText(context, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show()
                    return
                }

                val userData = UserData(
                    name = userName,
                    email = email,
                    mobile = mobile,
                    gender = gender
                )
                viewModelScope.launch {
                    myRepository.addUser(userData)
                }
                _state.update { it.copy(
                    isAddingUser = false,
                    userName = "",
                    email = "",
                    mobile = "",
                    gender = ""
                ) }
                viewModelScope.launch {
                    Log.d("aisfdgciud9","called - On")

                    myRepository.getUserList().collect{
                        Log.d("aisfdgciud5","called - On")

                        state.value.userList = it
                    }
                }

            }
            is UserEvent.setEmail -> {
                _state.update { it.copy(
                    email = event.email
                ) }
            }
            is UserEvent.setGender -> {
                _state.update { it.copy(
                    gender = event.gender
                ) }
            }
            is UserEvent.setMobile -> {
                _state.update { it.copy(
                    mobile = event.mobile
                ) }
            }
            is UserEvent.setUserName -> {
                _state.update { it.copy(
                    userName = event.userName
                ) }
            }
            UserEvent.showDialog -> {
                _state.update { it.copy(
                    isAddingUser = true
                ) }
            }
            else -> {}
        }
    }

}