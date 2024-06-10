package com.example.weatherapp.data

import com.example.weatherapp.network.CitiesApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitiesRepository @Inject constructor(private val citiesApiService: CitiesApiService) {
    suspend fun getCitiesList(): List<City> {
        return citiesApiService.getCitiesList()
    }
}