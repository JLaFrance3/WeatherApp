package edu.quinnipiac.ser210.weatherapp.api

data class Wind(
    val degrees: Int,
    val direction: String,
    val speed: Double,
    val speed_unit: String
)