package com.example.cgv.api

import com.example.cgv.model.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Service {
    @GET("client/public/movies")
    fun getHomeInfo(): Observable<Response<HomeInfo>>

    @POST("client/public/signup")
    fun postSignUp(@Body param: User):Observable<Response<AuthenticationResponse>>

    @POST("login")
    fun postLogin(@Body param: LoginParam):Observable<Response<AuthenticationResponse>>

    @GET("client/private/history/{email}")
    fun getHistoryTransaction(@Path("email") email:String)
}