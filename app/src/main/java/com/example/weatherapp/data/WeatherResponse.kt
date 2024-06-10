package com.example.weatherapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("name")
    val name: String,
    @SerialName("main")
    val main: Main
)

@Serializable
data class Main(
    @SerialName("temp")
    val temp: Float = 34.5f,
    @SerialName("feels_like")
    val feels_like: Float = 43.5f,
    @SerialName("temp_min")
    val temp_min: Float = 4f,
    @SerialName("temp_max")
    val temp_max: Float = 10f,
    @SerialName("pressure")
    val pressure: Int = 45,
    @SerialName("humidity")
    val humidity: Int = 32
)
