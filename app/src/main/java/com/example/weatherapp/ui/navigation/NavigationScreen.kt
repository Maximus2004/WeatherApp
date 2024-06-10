package com.example.weatherapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapp.ui.home.HomeScreen
import com.example.weatherapp.ui.weather.WeatherScreen

@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        modifier = Modifier.fillMaxSize(),
        startDestination = HomeScreen.route
    ) {
        composable(route = HomeScreen.route) {
            HomeScreen(navigate = navController::navigate)
        }
        composable(
            route = WeatherScreen.routeWithArgs,
            arguments = listOf(navArgument(name = WeatherScreen.coord) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val coord = backStackEntry.arguments?.getString(WeatherScreen.coord)!!.split(',')
            WeatherScreen(longitude = coord[0], latitude = coord[1], name = coord[2])
        }
    }
}