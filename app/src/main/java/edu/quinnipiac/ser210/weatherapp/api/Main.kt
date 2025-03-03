package edu.quinnipiac.ser210.weatherapp.api

data class Main(
    val ground_level_pressure: Int,
    val humidity: Int,
    val humidity_unit: String,
    val pressure: Int,
    val pressure_unit: String,
    val sea_level_pressure: Int,
    val temprature: Double,
    val temprature_feels_like: Double,
    val temprature_max: Double,
    val temprature_min: Double,
    val temprature_unit: String
)