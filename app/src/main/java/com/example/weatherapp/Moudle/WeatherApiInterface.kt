package com.example.weatherapp.Moudle

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {
    @GET("forecast.json")
    fun getWeatherData(@Query("key") key: String, @Query("q") q: String): Call<WeatherData>
}