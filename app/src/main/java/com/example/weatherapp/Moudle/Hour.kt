package com.example.weatherapp.Moudle

import com.google.gson.annotations.SerializedName


data class Hour(


    @SerializedName("time") var time: String? = null,
    @SerializedName("temp_c") var tempC: Double? = null,
    @SerializedName("temp_f") var tempF: Double? = null,
    @SerializedName("is_day") var isDay: Int? = null,
    @SerializedName("condition") var condition: Condition? = Condition(),
    @SerializedName("wind_mph") var windMph: Double? = null,
    @SerializedName("wind_kph") var windKph: Double? = null,

)