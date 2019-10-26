package com.example.cgv.api

import com.example.cgv.model.HomeInfo
import com.example.cgv.model.Response
import io.reactivex.Observable
import retrofit2.http.GET

interface Service {
    @GET("public/movies")
    fun getHomeInfo(): Observable<Response<HomeInfo>>
}