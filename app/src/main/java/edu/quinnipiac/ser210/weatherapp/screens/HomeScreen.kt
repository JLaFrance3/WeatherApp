package edu.quinnipiac.ser210.weatherapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import edu.quinnipiac.ser210.weatherapp.api.WeatherInterface
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel
import edu.quinnipiac.ser210.weatherapp.navigation.WeatherScreens

@Composable
fun HomeScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel
) {
    val weatherResults = weatherViewModel.weatherResult.observeAsState()

    //Convert mapped <String, responses> to a map of <String, Arraylist>
    val weatherData = mutableMapOf<String, ArrayList<WeatherInterface>>()
    weatherResults.value?.forEach { (city, response) ->
        weatherData.put(city, response.body()?: arrayListOf())
    }

    MainContent(navController = navController, weatherData.toMap())
}

@Composable
fun MainContent (
    navController: NavController,
    weatherData: Map<String, ArrayList<WeatherInterface>>
) {
    WeatherColumn(
        navController = navController,
        weatherData = weatherData,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun WeatherColumn(
    weatherData: Map<String, ArrayList<WeatherInterface>>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(weatherData.keys.toList()) {
            LocationCard(
                city = it,
                weather = weatherData.get(it)?: arrayListOf()
            ) { city ->
                    navController.navigate(route = WeatherScreens.DetailScreen.name+"/$city")
            }
        }
    }
}

@Composable
fun LocationCard(
    city: String,
    weather: ArrayList<WeatherInterface>,
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
            //TODO: Improve cards
            Text(city)
        }
    }
}