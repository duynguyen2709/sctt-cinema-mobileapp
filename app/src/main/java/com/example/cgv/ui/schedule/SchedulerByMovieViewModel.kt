package com.example.cgv.ui.schedule

import androidx.lifecycle.ViewModel
import com.example.cgv.model.Resource
import com.example.cgv.repository.HomeRepository

class SchedulerByMovieViewModel : ViewModel() {
    private val repository = HomeRepository()

    val homeInfoLiveData = repository.homeInfoLiveData

    fun getHomeInfo() {
        homeInfoLiveData.value = Resource.loading(null, null)
        repository.getHomeInfo()
    }
}