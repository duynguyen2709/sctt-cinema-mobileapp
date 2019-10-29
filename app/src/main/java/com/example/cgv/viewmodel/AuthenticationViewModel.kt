package com.example.cgv.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cgv.model.LoginParam
import com.example.cgv.model.Resource
import com.example.cgv.model.User
import com.example.cgv.repository.AuthenticationRepository

class AuthenticationViewModel :ViewModel(){
    private val repository = AuthenticationRepository()
    val userInfoLiveData = repository.userInfoLiveData
    fun postSignUp(param: User){
        userInfoLiveData.value = Resource.loading(null, null)
        repository.postSignUp(param)
    }

    fun postLogIn(param: LoginParam){
        userInfoLiveData.value = Resource.loading(null, null)
        repository.postLogIn(param)
    }
}