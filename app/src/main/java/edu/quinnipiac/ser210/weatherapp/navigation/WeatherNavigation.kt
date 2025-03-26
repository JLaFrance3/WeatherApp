/**
 * NavController for navigating from homescreen to detail screens
 */

package edu.quinnipiac.ser210.weatherapp.navigation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.quinnipiac.ser210.weatherapp.api.WeatherData
import edu.quinnipiac.ser210.weatherapp.data.Location
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel
import edu.quinnipiac.ser210.weatherapp.screens.DetailScreen
import edu.quinnipiac.ser210.weatherapp.screens.HomeScreen
import retrofit2.Response

//NavHost for weather app
@Composable
fun WeatherAppNavigation() {
    // Create navcontroller and allow for back navigation if not on homescreen
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = backStackEntry?.destination?.route != WeatherScreens.HomeScreen.name
    val weatherViewModel: WeatherViewModel = viewModel()

    // Get weather data responses from view model to pass to navBar
    val weatherResults = weatherViewModel.weatherResult.observeAsState()
    val weatherResponses = weatherResults.value

    var toggleColor by remember { mutableStateOf(false) }
    val backgroundColor = if (toggleColor) Color.DarkGray else Color.White

    //Limited requests per day and API requires a lat/long query
    //Short list of locations to query
    val locations = listOf(
        Location("Hartford", "41.77,-72.67"),
        Location("Hamden", "41.40,-72.90"),
        Location("New York", "40.71,-74.01"),
        Location("Chicago", "41.88,-87.63"),
        Location("Los Angeles", "34.05,-118.24")
    )

    // Only request data once when the composable is first launched
    LaunchedEffect(Unit) {
        weatherViewModel.getData(queries = locations)
    }
    Log.d("Network Request: ", "Completed request, creating navigation items")

    Scaffold (
        topBar = {
            NavBar(
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() },
                cityName = backStackEntry?.arguments?.getString("name") ?: "",
                weatherResponses = weatherResponses,
                modifier = Modifier,
                onSettingsClick = { toggleColor = !toggleColor }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WeatherScreens.HomeScreen.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(WeatherScreens.HomeScreen.name) {
                HomeScreen(
                    navController = navController,
                    weatherViewModel = weatherViewModel,
                    backgroundColor = backgroundColor
                )
            }
            composable(
                WeatherScreens.DetailScreen.name+"/{name}",
                arguments = listOf(navArgument(name = "name") {type = NavType.StringType})
            ) { backStackEntry ->
                DetailScreen(
                    navController = navController,
                    weatherViewModel = weatherViewModel,
                    backStackEntry.arguments?.getString("name") ?: "",
                    backgroundColor = backgroundColor
                )
            }
        }
    }
}

//Nav bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    cityName: String = "",
    weatherResponses: Map<String, Response<WeatherData>>?,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showHelpDialog by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text("Weather App",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }
        },
        actions = {
            // Share Button
            if (canNavigateBack) {
                IconButton(onClick = {
                    // Get weather for selected city from map of viewmodel responses
                    val weatherData = weatherResponses?.get(cityName)?.body()

                    if (weatherData != null) {
                        val currentCondition = weatherData.data.current_condition[0]

                        // Construct share string
                        val currentWeather = "Its currently ${currentCondition.temp_F}°F " +
                                "but feels like ${currentCondition.FeelsLikeF}°F\n" +
                                "Its a ${currentCondition.weatherDesc[0].value} day!"

                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Check out the weather in $cityName!\n" + currentWeather)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }
                }){
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                    )
                }
            }
            // Help Button
            IconButton(onClick = { showHelpDialog = true}){
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                )
            }
            // Settings Button
            IconButton(onClick = { onSettingsClick() }){
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                )
            }

        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
    )

    // Display the AlertDialog if showHelpDialog is true
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text(text = "App & API Information") },
            text = { Text(text = "This weather app makes use of the World Weather Online API, which provides different types of real-time weather data! Access up to 14 days hourly and 15 min weather forecast, astronomy, global weather alerts, and more.") },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}