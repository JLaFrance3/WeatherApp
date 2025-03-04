package edu.quinnipiac.ser210.weatherapp.api

data class Data(
    val current_condition: List<CurrentCondition>,
    val request: List<Request>,
    val weather: List<Weather>
)