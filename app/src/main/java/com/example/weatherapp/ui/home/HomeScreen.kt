package com.example.weatherapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.City
import com.example.weatherapp.ui.navigation.NavigationDestination
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.weather.WeatherScreen

object HomeScreen: NavigationDestination {
    override val route = "HomeScreen"
}

@Composable
fun HomeScreen(navigate: (String) -> Unit) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val currentLoadingState by homeViewModel.currentLoadingState.collectAsState()

    CitiesList(onHomeEvent = homeViewModel::onHomeEvent, currentLoadingState = currentLoadingState, navigate = navigate)
}

@Composable
fun LetterHeader(char: String, modifier: Modifier = Modifier) {
    Text(
        text = char.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        textAlign = TextAlign.Center,
        modifier = modifier.wrapContentSize()
    )
}

@Composable
fun NameItem(
    city: City,
    showCharHeader: Boolean,
    navigate: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        if (showCharHeader) {
            Box(modifier = Modifier.size(40.dp)) {
                LetterHeader(
                    char = city.city.first().toString(),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Spacer(modifier = Modifier.size(40.dp))
        }
        TextButton(
            onClick = { navigate("${WeatherScreen.route}/${city.longitude},${city.latitude},${city.city}") },
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = city.city,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .wrapContentSize()
                        .align(Alignment.CenterStart)
                )
            }
        }
    }
}

fun getStartIndexes(cities: Set<Map.Entry<Char, List<City>>>): List<Int> {
    val startIndexes = mutableListOf<Int>()
    var sum = 0
    for (city in cities) {
        startIndexes.add(sum)
        sum += city.value.size
    }
    return startIndexes
}

fun getEndIndexes(cities: Set<Map.Entry<Char, List<City>>>): List<Int> {
    val endIndexes = mutableListOf<Int>()
    var sum = 0
    for (city in cities) {
        endIndexes.add(sum + city.value.size)
        sum += city.value.size
    }
    return endIndexes
}

@Composable
fun CitiesList(currentLoadingState: CurrentLoadingState, onHomeEvent: (HomeEvent) -> Unit, navigate: (String) -> Unit) {
    LaunchedEffect(Unit) {
        onHomeEvent(HomeEvent.OnLaunchHomeScreen)
    }

    when (currentLoadingState) {
        is CurrentLoadingState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        }

        is CurrentLoadingState.Fail -> {
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
                        onClick = { onHomeEvent(HomeEvent.OnLaunchHomeScreen) },
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

        is CurrentLoadingState.Success -> {
            val cities = remember {
                currentLoadingState.homeUiState.listOfCities
                    .filter { it.city.isNotBlank() }
                    .sortedBy { it.city }
            }
            val groupedNames = remember(cities) {
                cities.groupBy { it.city.first().uppercaseChar() }
            }
            val startIndexes = remember(cities) {
                getStartIndexes(groupedNames.entries)
            }
            val endIndexes = remember(cities) {
                getEndIndexes(groupedNames.entries)
            }
            val listState = rememberLazyListState()
            val moveStickyHeader by remember {
                derivedStateOf {
                    endIndexes.contains(listState.firstVisibleItemIndex + 1)
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(items = cities, key = { _, city -> city.id }) { index, city ->
                    NameItem(
                        city = city,
                        showCharHeader = startIndexes.contains(index) && listState.firstVisibleItemIndex != index,
                        navigate = navigate
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(40.dp)
                        .align(Alignment.TopStart)
                ) {
                    LetterHeader(
                        char = cities[listState.firstVisibleItemIndex].city.first().toString(),
                        modifier = Modifier
                            .width(40.dp)
                            .then(
                                if (moveStickyHeader) {
                                    Modifier
                                        .offset {
                                            IntOffset(0, -listState.firstVisibleItemScrollOffset)
                                        }
                                        .align(Alignment.Center)
                                } else {
                                    Modifier.align(Alignment.Center)
                                }
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        CitiesList(
            onHomeEvent = {},
            currentLoadingState = CurrentLoadingState.Success(HomeUiState(listOfCities = listOf(
                        City(
                            id = "1",
                            city = "адов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "2",
                            city = "мдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "3",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "4",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "5",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "6",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "7",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "123",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "8",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "9",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "10",
                            city = "бдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "11",
                            city = "мдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "12",
                            city = "мдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "13",
                            city = "мдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "14",
                            city = "мдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "15",
                            city = "мдов",
                            longitude = "",
                            latitude = ""
                        ),
                        City(
                            id = "16",
                            city = "мдов",
                            longitude = "",
                            latitude = ""
                        )
                    ))),
            navigate = {}
        )
    }
}