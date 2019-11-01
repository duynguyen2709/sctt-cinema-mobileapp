package com.example.cgv.model

import com.google.gson.annotations.SerializedName

data class ShowTimes(
    @SerializedName("showtimes") val showTimes: Map<String, Map<String, List<ShowTime>>>
)