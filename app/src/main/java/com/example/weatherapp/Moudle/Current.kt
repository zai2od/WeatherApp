package com.example.weatherapp.Moudle

import com.google.gson.annotations.SerializedName

data class Current (

    @SerializedName("temp_c"             ) var tempC            : Double?       = null,
    @SerializedName("condition"          ) var condition        : Condition? = Condition(),
    @SerializedName("wind_mph"           ) var windMph          : Double?    = null,
    @SerializedName("wind_kph"           ) var windKph          : Double?    = null,


)