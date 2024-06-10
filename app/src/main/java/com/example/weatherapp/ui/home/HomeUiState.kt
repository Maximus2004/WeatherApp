package com.example.weatherapp.ui.home

import com.example.weatherapp.data.City

data class HomeUiState(
    val listOfCities: List<City> = listOf()
)