package com.example.weatherapp.data

import com.example.weatherapp.network.WeatherApiService
import javax.inject.Inject
import javax.inject.Singleton

const val apiKey = "04c26517589352f1928a3c985a28ea13"

@Singleton
class WeatherRepository @Inject constructor(private val weatherApiService: WeatherApiService) {
    suspend fun getWeatherMain(longitude: String, latitude: String): WeatherResponse {
        return weatherApiService.getWeather(
            longitude = longitude.toDouble(),
            latitude = latitude.toDouble(),
            apiKey = apiKey,
            exclude = "minutely,hourly,daily,alerts",
            units = "metric"
        )
    }
}