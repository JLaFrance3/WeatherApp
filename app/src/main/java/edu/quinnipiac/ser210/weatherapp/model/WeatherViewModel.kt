package edu.quinnipiac.ser210.weatherapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.quinnipiac.ser210.weatherapp.api.ApiInterface
import edu.quinnipiac.ser210.weatherapp.api.Weather
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel(){
    private val weatherApi = ApiInterface.create()
    val _weatherResult = MutableLiveData<Response<ArrayList<Weather>>>()
    val weatherResult : LiveData<Response<ArrayList<Weather>>> = _weatherResult

    fun getData() {
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather()
                if (response.isSuccessful) {
                    Log.d("API Response: ", response.body().toString())
                    _weatherResult.value = response
                }
                else {
                    Log.d("network error","Failed to load data")
                }
            }
            catch (e : Exception) {
                e.message?.let { Log.d("network error", it) }
            }
        }
    }
}