package com.example.cgv.model


data class CreateTicketParam(
    var email: String? = "",
    var seatCodes: List<String>,
    var showtimeID: Int

)