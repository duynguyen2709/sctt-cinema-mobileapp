package com.example.cgv.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cgv.model.LoginParam
import com.example.cgv.model.Resource
import com.example.cgv.model.User
import com.example.cgv.repository.AuthenticationRepository
import com.example.cgv.repository.UserRepository

class UserViewModel : ViewModel() {
    private val repository = UserRepository()
    val historyLiveData = repository.historyTransactionLiveData
    fun getListHistory(email: String) {
        historyLiveData.value= Resource.loading(null,null)
        repository.getListHistory(email)
    }
}