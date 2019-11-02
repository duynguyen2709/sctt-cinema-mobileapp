package com.example.cgv.model

import java.io.Serializable

data class Ticket(
    val date: String,
    val extraInfo: String,
    val movieName: String,
    val roomNumber: String,
    val seatCodes: String,
    val theaterName: String,
    val ticketID: Long,
    val time: String,
    val totalPrice: Int
) :Serializable