package com.example.weatherapp.ui.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.Main
import com.example.weatherapp.ui.navigation.NavigationDestination

object WeatherScreen : NavigationDestination {
    override val route = "WeatherScreen"
    const val coord = "coord"
    val routeWithArgs: String = "${route}/{$coord}"
}

@Composable
fun WeatherScreen(latitude: String, longitude: String, name: String) {
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    val currentLoadingState by weatherViewModel.currentLoadingState.collectAsState()

    WeatherScreenContent(
        name = name,
        latitude = latitude,
        longitude = longitude,
        currentLoadingState = currentLoadingState,
        onWeatherEvent = weatherViewModel::onWeatherEvent
    )
}

@Composable
fun WeatherScreenContent(
    name: String,
    latitude: String,
    longitude: String,
    currentLoadingState: CurrentLoadingState,
    onWeatherEvent: (WeatherEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onWeatherEvent(WeatherEvent.OnGetWeather(latitude = latitude, longitude = longitude))
    }


    when (currentLoadingState) {
        is CurrentLoadingState.Success -> {
            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                Column(modifier = Modifier.padding(top = 40.dp).align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${currentLoadingState.weatherUiState.main.temp.toInt()}°C",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { onWeatherEvent(WeatherEvent.OnGetWeather(latitude = latitude, longitude = longitude)) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(bottom = 36.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.update),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        is CurrentLoadingState.Loading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }

        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.error),
                        modifier = Modifier.padding(bottom = 42.dp),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Button(
                        onClick = { onWeatherEvent(WeatherEvent.OnGetWeather(latitude = latitude, longitude = longitude)) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.update),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeatherScreenPreview() {
    WeatherScreenContent(
        latitude = "44.5",
        longitude = "32.4",
        currentLoadingState = CurrentLoadingState.Success(WeatherUiState(Main())),
        onWeatherEvent = {},
        name = "Москва"
    )
}