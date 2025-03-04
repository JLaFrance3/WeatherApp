package edu.quinnipiac.ser210.weatherapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.quinnipiac.ser210.weatherapp.api.ApiInterface
import edu.quinnipiac.ser210.weatherapp.api.WeatherInterface
import edu.quinnipiac.ser210.weatherapp.data.Location
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel(){
    private val weatherApi = ApiInterface.create()
    private val _weatherResult = MutableLiveData<Map<String, Response<ArrayList<WeatherInterface>>>>()
    val weatherResult : LiveData<Map<String, Response<ArrayList<WeatherInterface>>>> = _weatherResult

    fun getData(queries: List<Location>) {
        viewModelScope.launch {
            try {
                //Map location name to responses
                val responseMap = mutableMapOf<String, Response<ArrayList<WeatherInterface>>>()

                //Map all query responses
                val responses = queries.map { location ->
                    async {
                        val response = weatherApi.getWeather(
                            query = location.coordinates,
                            numDays = 3,
                            timePeriod = 24,
                            language = "en",
                            airQualityIndex = "no",
                            alerts = "no",
                            format = "json"
                        )
                        responseMap[location.city] = response
                    }
                }

                //Await concurrent network calls and check each one
                responses.awaitAll()
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