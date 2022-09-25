package com.example.weatherapp.Moudle

data class WeatherData(

    var location: Location? = Location(),
    var current: Current? = Current(),
    var forecast: Forecast? = Forecast()

)

