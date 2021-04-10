package com.example.smartcities.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("myslim/api/utl")
    fun getUsers(): Call<List<User>>

    @GET("/myslim/api/utl/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

}