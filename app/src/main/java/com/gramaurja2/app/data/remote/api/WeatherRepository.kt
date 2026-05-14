package com.gramaurja2.app.data.remote.api

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApiService
) {
    // Note: Use your OpenWeatherMap API key here.
    private val apiKey = "YOUR_OPENWEATHERMAP_API_KEY"

    suspend fun checkRainProbability(city: String): Boolean {
        if (apiKey == "YOUR_OPENWEATHERMAP_API_KEY") {
            // DEMO MODE: If no API key is provided, simulate rain prediction for certain districts
            // to allow testing of the Smart Watering feature.
            return city.contains("Hubli", ignoreCase = true) || 
                   city.contains("Dharwad", ignoreCase = true) ||
                   city.contains("Belagavi", ignoreCase = true)
        }
        
        return runCatching {
            val response = api.getForecast(city, apiKey)
            response.list.take(8).any { it.pop > 0.4 || it.weather.any { w -> w.main == "Rain" } }
        }.getOrDefault(false)
    }
}
