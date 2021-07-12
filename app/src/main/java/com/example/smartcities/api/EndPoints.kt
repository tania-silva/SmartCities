package com.example.smartcities.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("api/utls")
    fun getUsers(): Call<List<User>>

    @GET("api/anomalias")
    fun getAnomalias(): Call<List<Marker>>

    @GET("api/utl/{id}")
    fun getUserById(@Path("id") id: Int): Call<List<User>>

    @FormUrlEncoded
    @POST("api/utl")
    fun postUtl(@Field("email") first: String?, @Field ("pass") second: String?): Call<List<OutputPost>>

    @FormUrlEncoded
    @POST("api/anomalia/editar") // pedido POST para editar anomalia
    fun editarAnom(@Field("id_anom") first: Int, @Field("titulo") second: String,
                   @Field("descricao") third: String, @Field("tipo_anom") fourth: String): Call<Marker>


    @FormUrlEncoded
    @POST("api/anomalia/delete") // pedido POST para editar anomalia
    fun eliminarAnom(@Field("id_anom") first: Int): Call<Marker>

    @FormUrlEncoded
    @POST("api/inserirAnom") // pedido POST para reportar anomalia
    fun addAnom(@Field("utilizador_id") first: Int, @Field("titulo") second: String,
                @Field("descricao") third: String, @Field("tipo_anom") fourth: String,
                @Field("imagem") fifth: String, @Field("lat") sixth: Float,
                @Field("lng") seventh: Float): Call<Marker>

    @GET("api/anomalia/{id}")
    fun getAnomById(@Path("id") id: Int): Call<List<Marker>>
}