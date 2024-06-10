package com.example.weatherapp.ui.weather

sealed interface WeatherEvent {
    data class OnGetWeather(val longitude: String, val latitude: String) : WeatherEvent
}