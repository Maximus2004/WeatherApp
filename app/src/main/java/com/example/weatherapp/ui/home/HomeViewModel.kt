package com.example.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val citiesRepository: CitiesRepository): ViewModel() {
    private val _currentLoadingState: MutableStateFlow<CurrentLoadingState> = MutableStateFlow(CurrentLoadingState.Loading)
    val currentLoadingState: StateFlow<CurrentLoadingState> = _currentLoadingState.asStateFlow()

    fun onHomeEvent(event: HomeEvent) = viewModelScope.launch {
        when(event) {
            is HomeEvent.OnLaunchHomeScreen -> {
                try {
                    _currentLoadingState.update { CurrentLoadingState.Loading }
                    val listOfCities = citiesRepository.getCitiesList()
                    if (listOfCities.isEmpty()) throw IllegalArgumentException()
                    _currentLoadingState.update { CurrentLoadingState.Success(HomeUiState(listOfCities = listOfCities)) }
                } catch (e: Exception) {
                    _currentLoadingState.update { CurrentLoadingState.Fail }
                }
            }
        }
    }
}

sealed interface CurrentLoadingState {
    data class Success(val homeUiState: HomeUiState) : CurrentLoadingState
    data object Loading : CurrentLoadingState
    data object Fail : CurrentLoadingState
}