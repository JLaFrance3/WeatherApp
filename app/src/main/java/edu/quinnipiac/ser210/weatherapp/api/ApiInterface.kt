package edu.quinnipiac.ser210.weatherapp.api

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "x-rapidapi-key: xxx",
        "x-rapidapi-host: weather-api167.p.rapidapi.com",
        "Accept: application/json"
    )
    @GET("api/weather/current")
    suspend fun getWeather(
        @Query("place") place: String,
        @Query("cnt") count: Int,
        @Query("units") units: Int,
        @Query("type") type: Int,
        @Query("lang") lang: String,
        @Query("mode") mode: String
    ): Response<ArrayList<Weather>>

    companion object {
        var BASE_URL = "https://weather-api167.p.rapidapi.com/"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}