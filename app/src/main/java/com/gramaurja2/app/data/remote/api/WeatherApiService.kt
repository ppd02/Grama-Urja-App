package com.gramaurja2.app.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

data class WeatherResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt: Long,
    val main: MainData,
    val weather: List<WeatherDescription>,
    val pop: Double // Probability of precipitation
)

data class MainData(
    val temp: Double
)

data class WeatherDescription(
    val main: String,
    val description: String
)
