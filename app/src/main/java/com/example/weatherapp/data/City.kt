package com.example.weatherapp.data

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class City(
    val id: String,
    val city: String,
    val latitude: String,
    val longitude: String
)
