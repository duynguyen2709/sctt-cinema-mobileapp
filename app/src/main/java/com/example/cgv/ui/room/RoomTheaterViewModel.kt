package com.example.cgv.ui.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cgv.model.Resource
import com.example.cgv.model.Seat
import com.example.cgv.model.SeatMapResponse
import com.example.cgv.repository.RoomTheaterRepository

class RoomTheaterViewModel : ViewModel() {
    private val repository = RoomTheaterRepository()

    val listSeat: MutableLiveData<Resource<SeatMapResponse>> =
        repository.seatLiveData


    fun getSeatMap(showTimeID:String) {
        listSeat.value = Resource.loading(null, null)
        repository.getSeatMap(showTimeID)
    }
}