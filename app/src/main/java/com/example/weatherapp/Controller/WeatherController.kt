package com.example.weatherapp.Controller

import com.example.weatherapp.Moudle.WeatherApiInterface
import com.example.weatherapp.Moudle.WeatherData
import com.example.weatherapp.View.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WeatherController {
    const val API_KEY = "fbff809070b8440d930104713222109"
    var q = ""
    var permissionDeniedOrNot=false
    fun getData(onDone: (WeatherData?)->Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(WeatherApiInterface::class.java)

        val retrofitAPI = retrofit.getWeatherData(
            API_KEY,
            q
        )

        retrofitAPI.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {

                if (response.isSuccessful) {
                    onDone(response.body())
                }
            }


            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                t.printStackTrace()
            }

        })


    }

}
