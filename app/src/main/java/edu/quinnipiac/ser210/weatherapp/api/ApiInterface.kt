package edu.quinnipiac.ser210.weatherapp.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import edu.quinnipiac.ser210.weatherapp.BuildConfig;
import retrofit2.http.Query


interface ApiInterface {
    @Headers(
        "x-rapidapi-key: ${BuildConfig.API_KEY}",
        "x-rapidapi-host: world-weather-online-api1.p.rapidapi.com"
    )
    @GET("weather.ashx")
    suspend fun getWeather(
        @Query("q") query: String,//Latitude/longitude string "41.40,-72.90"
        @Query("num_of_days") numDays: Int,//4
        @Query("tp") timePeriod: Int,//24
        @Query("lang") language: String,//"en"
        @Query("aqi") airQualityIndex: String,//"no"
        @Query("alerts") alerts: String,//"no"
        @Query("format") format: String//"json"
    ): Response<WeatherData>

    companion object {
        var BASE_URL = "https://world-weather-online-api1.p.rapidapi.com/"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}