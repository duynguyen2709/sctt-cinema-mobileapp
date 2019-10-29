package com.example.cgv.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cgv.model.Resource
import com.example.cgv.repository.HomeRepository

class HomeViewModel: ViewModel() {
    private val repository = HomeRepository()
    val homeInfoLiveData = repository.homeInfoLiveData
    fun getHomeInfo(){
        homeInfoLiveData.value = Resource.loading(null, null)
        repository.getHomeInfo()
    }
}