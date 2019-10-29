package com.example.cgv.api

import com.example.cgv.CoreApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    const val TIME_OUT = 10L

    const val BASE_URL = "http://167.179.80.90:8000/"

    val youtubeInstance: Retrofit by lazy {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                val token = CoreApplication.instance.token
                if (token != null) {
                    builder.header("Authorization", "Bearer $token")
                }
                request = builder.build()
                chain.proceed(request)
            }
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}