package com.example.cgv.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Theater(
    @SerializedName("theaterID") val theaterId :Int,
    @SerializedName("theaterName") val theaterName: String
): Serializable