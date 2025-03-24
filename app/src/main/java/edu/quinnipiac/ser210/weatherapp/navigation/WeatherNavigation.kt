/**
 * NavController for navigating from homescreen to detail screens
 */

package edu.quinnipiac.ser210.weatherapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.quinnipiac.ser210.weatherapp.data.Location
import edu.quinnipiac.ser210.weatherapp.model.WeatherViewModel
import edu.quinnipiac.ser210.weatherapp.screens.DetailScreen
import edu.quinnipiac.ser210.weatherapp.screens.HomeScreen

//NavHost for weather app
@Composable
fun WeatherAppNavigation() {
    // Create navcontroller and allow for back navigation if not on homescreen
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = backStackEntry?.destination?.route != WeatherScreens.HomeScreen.name
    val weatherViewModel: WeatherViewModel = viewModel()

    //Limited requests per day and API requires a lat/long query
    //Short list of locations to query
    val locations = listOf(
        Location("Hartford", "41.77,-72.67"),
//        Location("Hamden", "41.40,-72.90"),
//        Location("New York", "40.71,-74.01"),
//        Location("Chicago", "41.88,-87.63"),
//        Location("Los Angeles", "34.05,-118.24")
    )

    //Get data for each location in list
    weatherViewModel.getData(
        queries = locations
    )
    Log.d("Network Request: ", "Completed request, creating navigation items")

    Scaffold (
        topBar = {
            NavBar(
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() },
                modifier = Modifier
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
                    weatherViewModel = weatherViewModel
                )
            }
            composable(
                WeatherScreens.DetailScreen.name+"/{name}",
                arguments = listOf(navArgument(name = "name") {type = NavType.StringType})
            ) { backStackEntry ->
                DetailScreen(
                    navController = navController,
                    weatherViewModel = weatherViewModel,
                    backStackEntry.arguments?.getString("name") ?: ""
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
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text("Weather App",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier.padding(bottom = 4.dp),
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
        }
    )
}