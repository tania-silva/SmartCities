package com.example.smartcities.api


data class Marker(
    val id_anom: Int,
    val utilizador_id: Int,
    val descricao: String,
    val lat: Double,
    val lng: Double,
    val imagem: String,
    val titulo: String,
)