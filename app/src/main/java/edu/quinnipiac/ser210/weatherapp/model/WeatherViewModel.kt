package edu.quinnipiac.ser210.weatherapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.quinnipiac.ser210.weatherapp.api.ApiInterface
import edu.quinnipiac.ser210.weatherapp.api.Weather
import edu.quinnipiac.ser210.weatherapp.api.WeatherInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel(){
    private val weatherApi = ApiInterface.create()
    val _weatherResult = MutableLiveData<List<Response<ArrayList<WeatherInterface>>>>()
    val weatherResult : LiveData<List<Response<ArrayList<WeatherInterface>>>> = _weatherResult

    fun getData(queries: List<String>) {
        viewModelScope.launch {
            try {
                //Map location name to responses
                val responseMap = mutableMapOf<String, Response<ArrayList<WeatherInterface>>>()

                //Map all query responses
                val responses = queries.map { (location, coordinates) ->
                    async {
                        val response = weatherApi.getWeather(
                            query = coordinates,
                            numDays = 3,
                            timePeriod = 24,
                            language = "en",
                            airQualityIndex = "no",
                            alerts = "no",
                            format = "json"
                        )
                        responseMap[location] = response
                    }
                }

                //Await concurrent network calls and check each one
                responses.awaitAll()

                responseMap.forEach { response ->
                    if (response.isSuccessful) {
                        Log.d("API Response: ", response.body().toString())
                    }
                    else {
                        Log.d("network error","Failed to load data")
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