package edu.quinnipiac.ser210.weatherapp.screens

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import edu.quinnipiac.ser210.weatherapp.api.WeatherData
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel
import java.util.Date

@Composable
fun DetailScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel,
    cityName: String
) {
    val weatherResults = weatherViewModel.weatherResult.observeAsState()
    val weatherData = weatherResults.value?.get(cityName)?.body()

    Log.d("DetailScreen", "Initializing DetailColumn")
    DetailColumn(
        navController = navController,
        city = cityName,
        weather = weatherData
    )
}

@Composable
fun DetailColumn(
    navController: NavController,
    city: String,
    weather: WeatherData?,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val currentDateAndTime = sdf.format(Date())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (weather != null) {
            val currentCondition = weather.data.current_condition.firstOrNull()
            if (currentCondition != null) {
                currentCondition.weatherIconUrl.firstOrNull()?.let { WeatherImage(it.value, modifier = modifier) }
                currentCondition.weatherDesc.firstOrNull()?.let { WeatherInfo(
                    city = city,
                    temperature = currentCondition.temp_F,
                    date = currentDateAndTime,
                    description = it.value,
                    feelsLike = currentCondition.FeelsLikeF,
                    humidity = currentCondition.humidity,
                    modifier = modifier
                ) }
            }
        }
    }
}

@Composable
fun WeatherImage(weatherIconUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = weatherIconUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(156.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
fun WeatherInfo(
    city: String,
    date: String,
    temperature: String,
    description: String,
    feelsLike: String,
    humidity: String,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(12.dp)
    ) {
        Text(
            text = "City: $city",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = modifier
        )
        Text(
            text = date,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
        )
        Text(
            text = description,
            fontSize = 22.sp,
            modifier = modifier.padding(80.dp)
        )
        Text(
            text = "$temperature, feels like $feelsLike",
            fontSize = 22.sp,
            modifier = modifier
        )
        Text(
            text = "Humidity: $humidity",
            fontSize = 22.sp,
            modifier = modifier
        )
    }
}