package com.example.weatherforecastapp.network

import com.example.weatherforecastapp.model.Weather
import com.example.weatherforecastapp.model.WeatherObject
import com.example.weatherforecastapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherAPI {
    @GET(value = "")
    suspend fun getWeather(
        @Query("") query: String,
        @Query("") units: String = "imperial",
        @Query("") appid: String = Constants.API_KEY
    ): Weather
}