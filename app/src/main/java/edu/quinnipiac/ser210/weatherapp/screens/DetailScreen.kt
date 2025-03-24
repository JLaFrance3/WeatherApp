/**
 * DetailScreen listing current weather and forecasts for selected location
 */

package edu.quinnipiac.ser210.weatherapp.screens

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import edu.quinnipiac.ser210.weatherapp.api.WeatherData
import edu.quinnipiac.ser210.weatherapp.api.WeatherDesc
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel
import java.util.Date

@Composable
fun DetailScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel,
    cityName: String
) {
    // Get weather data from view model
    val weatherResults = weatherViewModel.weatherResult.observeAsState()
    val weatherData = weatherResults.value?.get(cityName)?.body()

    Log.d("DetailScreen", "Initializing DetailColumn")
    DetailColumn(
        navController = navController,
        city = cityName,
        weatherData = weatherData
    )
}

// Column of all weather information to be displayed for passed location
@Composable
fun DetailColumn(
    navController: NavController,
    city: String,
    weatherData: WeatherData?,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("EEE, MMM d, yyyy")
    val currentDateAndTime = sdf.format(Date())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (weatherData != null) {
            val currentCondition = weatherData.data.current_condition[0]
            ElevatedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                modifier = modifier
            ) {
                Column (
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    WeatherImage(
                        currentCondition.weatherIconUrl[0].value, modifier = modifier
                    )
                    CurrentWeatherInfo(
                        city = city,
                        temperature = currentCondition.temp_F,
                        date = currentDateAndTime,
                        description = currentCondition.weatherDesc[0].value,
                        feelsLike = currentCondition.FeelsLikeF,
                        humidity = currentCondition.humidity,
                        modifier = modifier
                    )
                }
            }

            val forecast = weatherData.data.weather
            PrecipitationInfoColumn(
                city = city,
                date = currentDateAndTime,
                maxTemperature = forecast[0].maxtempF,
                minTemperature = forecast[0].mintempF,
                weatherIconUrl = forecast[0].hourly[0].weatherIconUrl[0].value,
                weatherDesc = forecast[0].hourly[0].weatherDesc[0].value,
                chanceRain = forecast[0].hourly[0].chanceofrain,
                chanceSnow = forecast[0].hourly[0].chanceofsnow,
                wind = forecast[0].hourly[0].WindGustMiles,
                modifier = modifier
            )

            ForecastRow()
        }
    }
}

//Large current weather image
@Composable
fun WeatherImage(weatherIconUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = weatherIconUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(148.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
    )
}

//Information about current weather
@Composable
fun CurrentWeatherInfo(
    city: String,
    date: String,
    temperature: String,
    description: String,
    feelsLike: String,
    humidity: String,
    modifier: Modifier = Modifier
) {
    Box (
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Text(
            text = "City: $city",
            fontSize = 48.sp,
            lineHeight = 56.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(4.dp)
        )
    }
    Text(
        text = date,
        fontSize = 36.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
    HorizontalDivider(
        thickness = 2.dp,
        modifier = modifier.padding(12.dp)
    )
    Text(
        text = "Current Weather:",
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .padding(4.dp)
    )
    Text(
        text = description,
        fontSize = 22.sp,
        modifier = modifier
            .padding(4.dp)
    )
    Text(
        text = "$temperature°F, feels like $feelsLike°F",
        fontSize = 22.sp,
        modifier = modifier
            .padding(4.dp)
    )
    Text(
        text = "Humidity: $humidity%",
        fontSize = 22.sp,
        modifier = modifier
            .padding(4.dp)
    )
}

//Precipitation information
@Composable
fun PrecipitationInfoColumn(
    city: String,
    date: String,
    maxTemperature: String,
    minTemperature: String,
    weatherIconUrl: String,
    weatherDesc: String,
    chanceRain: String = "0",
    chanceSnow: String = "0",
    wind: String,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {

    }
}

//Row of forecast cards
@Composable
fun ForecastRow(
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 0.dp)
            .fillMaxWidth()
    ) {
        ForecastCard (
            modifier = modifier
                .weight(0.5f)
                .fillMaxHeight()
        )
        VerticalDivider(thickness = 12.dp, color = Color.Transparent)
        ForecastCard (
            modifier = modifier
                .weight(0.5f)
                .fillMaxHeight()
        )
    }
}

//Card displaying future weather information
@Composable
fun ForecastCard(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = modifier
    ) {
        Text(
            text = "forecast here"
        )
    }
}