package com.example.cgv.ui.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cgv.model.*
import com.example.cgv.repository.RoomTheaterRepository

class RoomTheaterViewModel : ViewModel() {
    private val repository = RoomTheaterRepository()

    val listSeat: MutableLiveData<Resource<SeatMapResponse>> =
        repository.seatLiveData
    val createTicketLiveData : MutableLiveData<Resource<Ticket>> = repository.createTicketLiveData


    fun getSeatMap(showTimeID:String) {
        listSeat.value = Resource.loading(null, null)
        repository.getSeatMap(showTimeID)
    }

    fun createTicket(param:CreateTicketParam){
        createTicketLiveData.value= Resource.loading(null,null)
        repository.createTicket(param)
    }


}