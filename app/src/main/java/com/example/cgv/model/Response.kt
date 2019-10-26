package com.example.cgv.model

data class Response<T> (

    val returnCode : Int,
    val returnMessage : String,
    val data : T
)