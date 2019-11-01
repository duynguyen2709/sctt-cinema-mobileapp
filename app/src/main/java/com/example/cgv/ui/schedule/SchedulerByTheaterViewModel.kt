package com.example.cgv.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cgv.model.Resource
import com.example.cgv.model.Theater
import com.example.cgv.repository.SchedulerRepository

class SchedulerByTheaterViewModel : ViewModel() {
    private val repository = SchedulerRepository()

    val listTheater: MutableLiveData<Resource<Map<String, List<Theater>>>> =
        repository.theaterLiveData

    init {
        getListTheater()
    }

    private fun getListTheater() {
        listTheater.value = Resource.loading(null, null)
        repository.getListTheater()
    }
}