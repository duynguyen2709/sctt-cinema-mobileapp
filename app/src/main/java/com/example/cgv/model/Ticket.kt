package com.example.cgv.model

data class Ticket(
    val date: String,
    val extraInfo: String,
    val movieName: String,
    val roomNumber: String,
    val seatCodes: String,
    val theaterName: String,
    val ticketID: Int,
    val time: String,
    val totalPrice: Int
)