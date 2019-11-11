package com.example.cgv.api

import com.example.cgv.model.HomeInfo
import com.example.cgv.model.Response
import com.example.cgv.model.ShowTimes
import com.example.cgv.model.Theater
import com.example.cgv.model.*
import com.example.cgv.util.HashUtils
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Path

interface Service {
    @GET("client/public/movies")
    fun getHomeInfo(): Observable<Response<HomeInfo>>

    @GET("client/public/theaters")
    fun getListTheater(): Observable<Response<Map<String, List<Theater>>>>

    @GET("client/public/showtimes")
    fun getListShowTimes(
        @Query("type") type: Int,
        @Query("id") id: String,
        @Query("date") date: String
    ): Observable<Response<ShowTimes>>

    @POST("client/public/signup")
    fun postSignUp(@Body param: User): Observable<Response<AuthenticationResponse>>

    @POST("login")
    fun postLogin(@Body param: LoginParam): Observable<Response<AuthenticationResponse>>

    @GET("client/private/history/{email}")
    fun getHistoryTransaction(@Path("email") email: String): Observable<Response<List<Ticket>>>

    @GET("client/public/showtimes/status/{showtimeID}")
    fun getSeatMap(
        @Path("showtimeID") showTimeID: String, @Query("clientID") clientID: String = "1", @Query(
            "reqdate"
        ) reqdate: String = System.currentTimeMillis().toString(), @Query("sig") sig: String = HashUtils.sha256(
            "" + 1 + "|" + System.currentTimeMillis() + "|" + "SCTT"
        )
    ): Observable<Response<SeatMapResponse>>

    @POST("client/private/createticket")
    fun createTicket(@Body param:CreateTicketParam):Observable<Response<Ticket>>
}