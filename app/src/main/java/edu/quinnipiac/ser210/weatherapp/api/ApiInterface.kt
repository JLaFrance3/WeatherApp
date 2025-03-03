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
        "x-rapidapi-host: open-weather13.p.rapidapi.com"
    )
    @GET("city/hamden/EN")
    suspend fun getWeather(): Response<ArrayList<Weather>>

    companion object {
        var BASE_URL = "https://open-weather13.p.rapidapi.com/city/"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}