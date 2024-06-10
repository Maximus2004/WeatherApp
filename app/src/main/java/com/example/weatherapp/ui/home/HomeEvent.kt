package com.example.weatherapp.ui.home

sealed interface HomeEvent {
    data object OnLaunchHomeScreen : HomeEvent
}