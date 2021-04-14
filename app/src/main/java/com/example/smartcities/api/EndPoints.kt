package com.example.smartcities.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("api/utls")
    fun getUsers(): Call<List<User>>

    @GET("api/utl/{id}")
    fun getUserById(@Path("id") id: Int): Call<List<User>>

    @FormUrlEncoded
    @POST("api/utl")
    fun postUtl(@Field("email") first: String?, @Field ("pass") second: String?): Call<List<OutputPost>>
}