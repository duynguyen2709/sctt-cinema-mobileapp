package com.example.cgv.ui.ticket

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cgv.model.Resource
import com.example.cgv.model.ShowTimes
import com.example.cgv.repository.TicketRepository

class TicketViewModel : ViewModel() {
    private val repository = TicketRepository()

    val listShowTimes: MutableLiveData<Resource<ShowTimes>> =
        repository.showtimeLiveData

    fun getListShowTimes(type: Int, id: String, date: String) {
        listShowTimes.value = Resource.loading(null, null)
        repository.getShowTimes(type, id, date)
    }
}