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
    lateinit var dataForGoogleMap:WeatherData
    var latLocation=""
    var longLocation=""
    const val API_KEY = "0410c38c3e464360806201234220510"
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
    fun getData2(q:String,onDone: (WeatherData?)->Unit){
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
                    onDone(response.body()!!)
                }
            }


            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                t.printStackTrace()
            }


        })
    }

}
