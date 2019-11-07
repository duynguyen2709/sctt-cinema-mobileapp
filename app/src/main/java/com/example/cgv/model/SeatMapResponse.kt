package com.example.cgv.model

data class SeatMapResponse(
    val roomID: Int,
    val seats: List<List<Seat>>	,
    val showtimeID: Int,
    val basePrice: Long,
    val VIPPrice: Long
)