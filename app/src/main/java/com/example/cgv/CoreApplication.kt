package com.example.cgv

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.cgv.model.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder

const val USER_PROFILE = "USER_PROFILE"
const val TOKEN = "TOKEN"

open class CoreApplication : Application() {

    companion object {
        lateinit var instance: CoreApplication
            private set
    }
    private lateinit var prefsUtil :SharedPreferences
    private lateinit var gSon: Gson
    var user:User?=null
    var token:String?=null

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefsUtil=applicationContext.getSharedPreferences("CGV_APP", Context.MODE_PRIVATE)
        val gSonBuilder = GsonBuilder()
        gSonBuilder.setDateFormat("M/d/yy hh:mm a")
        gSon = gSonBuilder.create()
    }

    fun saveUser(user: User){
        prefsUtil.edit().putString(
            USER_PROFILE,
            gSon.toJson(user)
        ).apply()
        this.user=user
    }

    fun saveToken(token: String){
        prefsUtil.edit().putString(
            TOKEN,
            token
        ).apply()
        this.token=token
    }
}