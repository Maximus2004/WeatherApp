package com.example.weatherapp.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _currentLoadingState: MutableStateFlow<CurrentLoadingState> = MutableStateFlow(CurrentLoadingState.Loading)
    val currentLoadingState: StateFlow<CurrentLoadingState> = _currentLoadingState.asStateFlow()

    fun onWeatherEvent(event: WeatherEvent) = viewModelScope.launch {
        when (event) {
            is WeatherEvent.OnGetWeather -> {
                try {
                    _currentLoadingState.update { CurrentLoadingState.Loading }
                    val weather = weatherRepository.getWeatherMain(longitude = event.longitude, latitude = event.latitude)
                    _currentLoadingState.update { CurrentLoadingState.Success(WeatherUiState(main = weather.main)) }
                } catch (e: Exception) {
                    _currentLoadingState.update { CurrentLoadingState.Fail }
                }
            }
        }
    }
}

sealed interface CurrentLoadingState {
    data class Success(val weatherUiState: WeatherUiState) : CurrentLoadingState
    data object Loading : CurrentLoadingState
    data object Fail : CurrentLoadingState
}