package edu.quinnipiac.ser210.weatherapp.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import edu.quinnipiac.ser210.weatherapp.api.WeatherData
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel
import edu.quinnipiac.ser210.weatherapp.navigation.WeatherScreens

@Composable
fun HomeScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel
) {
    val weatherResults = weatherViewModel.weatherResult.observeAsState()

    //Convert mapped <String, responses> to a map of <String, Arraylist>
    val weatherData = weatherResults.value?.filterValues { it.body() != null}?.mapValues { it.value.body()!! }

    Log.d("HomeScreen", "Initializing MainContent")
    MainContent(navController = navController, weatherData)
}

@Composable
fun MainContent (
    navController: NavController,
    weatherData: Map<String, WeatherData>?
) {
    WeatherColumn(
        navController = navController,
        weatherData = weatherData,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun WeatherColumn(
    weatherData: Map<String, WeatherData>?,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        if (weatherData != null) {
            items(weatherData.keys.toList()) {
                LocationCard(
                    city = it,
                    weather = weatherData.get(it),
                    modifier = modifier.padding(16.dp)
                ) { city ->
                    navController.navigate(route = WeatherScreens.DetailScreen.name+"/$city")
                }
            }
        }
    }
}

@Composable
fun LocationCard(
    city: String,
    weather: WeatherData?,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit = {}
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 0.dp)
                .clickable {
                    onItemClick(city)
                }
        ) {
            if (weather != null) {
                val currentCondition = weather.data.current_condition.firstOrNull()
                if (currentCondition != null) {
                    currentCondition.weatherIconUrl.firstOrNull()?.let { WeatherIcon(it.value, modifier = modifier) }
                    currentCondition.weatherDesc.firstOrNull()?.let { LocationInfo(
                        city = city,
                        temperature = currentCondition.temp_F,
                        weatherDesc = it.value,
                        modifier = modifier
                    ) }
                }
            }
        }
    }
}

@Composable
fun WeatherIcon(weatherIconUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = weatherIconUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(56.dp)
    )
}

@Composable
fun LocationInfo(
    city: String,
    temperature: String,
    weatherDesc: String,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(start = 8.dp)
            .height(48.dp)
    ) {
        Text(
            text = "City: $city",
            fontSize = 22.sp,
            lineHeight = 16.sp
        )
        Row {
            Text(
                text = "$temperature°F",
                fontSize = 16.sp,
                lineHeight = 12.sp
            )
            Text(
                text = weatherDesc,
                fontSize = 16.sp,
                lineHeight = 12.sp
            )
        }
    }
}