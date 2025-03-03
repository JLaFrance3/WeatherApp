package edu.quinnipiac.ser210.weatherapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.quinnipiac.ser210.weatherapp.api.Weather
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel
import edu.quinnipiac.ser210.weatherapp.navigation.WeatherScreens

@Composable
fun HomeScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel
) {
    val weatherResult = weatherViewModel.weatherResult.observeAsState()
    val weatherList = weatherResult.value?.body()
    val weatherListNonNullable = weatherList?.filterNotNull() ?: emptyList()

    MainContent(navController = navController, weatherListNonNullable)
}

@Composable
fun MainContent (
    navController: NavController,
    weatherList: List<Weather>
) {
    WeatherColumn(
        navController = navController,
        weatherList = weatherList,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun WeatherColumn(
    weatherList: List<Weather>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        itemsIndexed(items = weatherList) { index, item ->
            LocationCard(
                weather = item,
                modifier = Modifier
                    .padding(8.dp)
            ) { weather ->
                navController.navigate(route = WeatherScreens.DetailScreen.name+"/$weather")
            }
        }
    }
}

@Composable
fun LocationCard(weather: Weather, modifier: Modifier = Modifier, onItemClick: (String) -> Unit = {}) {
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
                    onItemClick(weather.name)
                }
        ) {
            //TODO: Improve cards
            Text(weather.name)
        }
    }
}