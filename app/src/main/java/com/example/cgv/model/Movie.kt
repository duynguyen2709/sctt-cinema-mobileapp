package com.example.cgv.model

import java.io.Serializable

data class Movie (
    val movieID : Int,
    val movieName : String,
    val timeInMinute : Int,
    val category : String,
    val imageURL : String,
    val trailerURL : String,
    val screenshots : List<String>,
    val description : String,
    val dateFrom : String,
    val dateTo : String,
    val status : Int,
    val baseTicketPrice : Int,
    val valid : Boolean
): Serializable