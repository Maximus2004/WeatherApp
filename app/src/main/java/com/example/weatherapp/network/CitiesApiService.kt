package com.example.weatherapp.network

import com.example.weatherapp.data.City
import retrofit2.http.GET

interface CitiesApiService {
    @GET("764f9886a1e8392ddcae2521437d5a3b/raw/65164ea1af958c75c81a7f0221bead610590448e/cities.json")
    suspend fun getCitiesList(): List<City>
}