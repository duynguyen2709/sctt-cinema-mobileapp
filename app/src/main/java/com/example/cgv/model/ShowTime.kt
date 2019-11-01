package com.example.cgv.model

import com.google.gson.annotations.SerializedName

data class ShowTime(
    @SerializedName("showtimeID") val showtimeID: String,
    @SerializedName("startTime") val startTime: String
)