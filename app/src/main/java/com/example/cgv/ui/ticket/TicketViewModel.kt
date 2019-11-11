package com.example.cgv.ui.ticket

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cgv.model.Resource
import com.example.cgv.model.ShowTimes
import com.example.cgv.repository.TicketRepository

class TicketViewModel : ViewModel() {
    private val repository = TicketRepository()

    private var _type: Int? = null

    private var _id: String? = null

    private var _date: String? = null

    val listShowTimes: MutableLiveData<Resource<ShowTimes>> =
        repository.showtimeLiveData

    fun getListShowTimes(type: Int, id: String, date: String) {
        _type = type
        _date = date
        _id = id
        listShowTimes.value = Resource.loading(null, null)
        repository.getShowTimes(type, id, date)
    }

    fun refreshListShowTimes() {
        if (_type != null && _id != null && _date != null) {
            listShowTimes.value = Resource.loading(null, null)
            repository.getShowTimes(_type!!, _id!!, _date!!)
        }
    }
}