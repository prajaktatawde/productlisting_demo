package com.example.demolisting.network

import com.example.demolisting.models.RecyclerList
import retrofit2.http.GET

interface RetroService {

    @GET("products")
    suspend fun getDataFromApi(): RecyclerList

}