/**
 * Data class allowing city name to be associated to lat/long coordinates. The API used
 * does not provide a city name in the returned JSON
 */

package edu.quinnipiac.ser210.weatherapp.data

data class Location(val city: String, val coordinates: String)
