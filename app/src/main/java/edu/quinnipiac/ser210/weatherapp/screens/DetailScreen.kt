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
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel,
    locationName: String?
) {
    val weatherResult = weatherViewModel.weatherResult.observeAsState()
    val weatherList = weatherResult.value?.body()
    val weatherFiltered = weatherList?.filter { weather ->
        weather.name == locationName
    }
    weatherFiltered?.firstOrNull()?.let { weather ->
        DetailColumn(
            navController = navController,
            weather = weather
        )
    }
}

@Composable
fun DetailColumn(navController: NavController, weather: Weather, modifier: Modifier = Modifier) {
    //TODO: Improve UI
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Location: ${weather.name}",
            modifier = modifier
        )
        Text(
            text = "Temperature: ${weather.main.temprature}",
            modifier = modifier
        )
        Text(
            text = "Rain: ${weather.rain.amount}",
            modifier = modifier
        )
    }
}