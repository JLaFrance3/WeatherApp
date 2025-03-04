package edu.quinnipiac.ser210.weatherapp.api

data class Request(
    val query: String,
    val type: String
)