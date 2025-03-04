package edu.quinnipiac.ser210.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.quinnipiac.ser210.weatherapp.api.Weather
import edu.quinnipiac.ser210.weatherapp.api.WeatherInterface
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel,
    cityName: String?
) {
    val weatherResults = weatherViewModel.weatherResult.observeAsState()
    val weatherData = weatherResults.value?.get(cityName)?.body()

    DetailColumn(
        navController = navController,
        weather = weatherData
    )
}

@Composable
fun DetailColumn(navController: NavController, weather: ArrayList<WeatherInterface>?, modifier: Modifier = Modifier) {
    //TODO: Improve UI
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
    ) {

    }
}