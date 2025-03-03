package edu.quinnipiac.ser210.weatherapp.api

data class Weather(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val dt_txt: String,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val snow: Snow,
    val summery: String,
    val sys: Sys,
    val timezone: Int,
    val visibility_distance: Int,
    val visibility_unit: String,
    val weather: List<WeatherX>,
    val wind: Wind
)