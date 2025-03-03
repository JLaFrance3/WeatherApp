package edu.quinnipiac.ser210.weatherapp.navigation

enum class WeatherScreens {
    HomeScreen,
    DetailScreen;
    companion object {
        fun fromRoute(route: String?): WeatherScreens
                = when (route?.substringBefore("/")) {
            HomeScreen.name -> HomeScreen
            DetailScreen.name -> DetailScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route not recognized")
        }
    }
}