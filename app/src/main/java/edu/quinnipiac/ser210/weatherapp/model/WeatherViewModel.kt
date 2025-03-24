/**
 * WeatherViewModel handling network requests
 */

package edu.quinnipiac.ser210.weatherapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.quinnipiac.ser210.weatherapp.api.ApiInterface
import edu.quinnipiac.ser210.weatherapp.api.WeatherData
import edu.quinnipiac.ser210.weatherapp.data.Location
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel(){
    private val weatherApi = ApiInterface.create()
    private val _weatherResult = MutableLiveData<Map<String, Response<WeatherData>>>()
    val weatherResult : LiveData<Map<String, Response<WeatherData>>> = _weatherResult

    fun getData(queries: List<Location>) {
        Log.d("Network Request: ", "Launching ViewModelScope")
        viewModelScope.launch {
            try {
                //Map location name to responses
                val responseMap = mutableMapOf<String, Response<WeatherData>>()

                //Map all query responses
                val responses = queries.map { location ->
                    async {
                        val response = weatherApi.getWeather(
                            query = location.coordinates,
                            numDays = 4,
                            timePeriod = 24,
                            language = "en",
                            airQualityIndex = "no",
                            alerts = "no",
                            format = "json"
                        )
                        Log.d("Network Request: ", "Received response for ${location.city}")
                        responseMap[location.city] = response
                    }
                }

                //Await concurrent network calls and check each one
                Log.d("Network Request: ", "Waiting for concurrent requests...")
                responses.awaitAll()

                //Log responses and update LiveData
                responseMap.forEach { (city, response) ->
                    if (response.isSuccessful) {
                        Log.d("API Response: ", response.body().toString())
                    }
                    else {
                        Log.d("network error","Failed to load data for $city")
                    }
                }
                _weatherResult.value = responseMap

            }
            catch (e : Exception) {
                e.message?.let { Log.d("network error", it) }
            }
        }
    }
}