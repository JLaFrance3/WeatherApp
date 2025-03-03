package edu.quinnipiac.ser210.weatherapp.api

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunrise_txt: String,
    val sunset: Int,
    val sunset_txt: String,
    val type: Int
)