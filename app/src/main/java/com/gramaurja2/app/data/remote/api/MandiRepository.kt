package com.gramaurja2.app.data.remote.api

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MandiRepository @Inject constructor(
    private val api: MandiApiService
) {
    // Placeholder API Key for data.gov.in
    private val apiKey = "YOUR_DATA_GOV_IN_API_KEY"

    suspend fun getPricesForCrops(cropNames: List<String>): List<MandiRecord> {
        if (apiKey == "YOUR_DATA_GOV_IN_API_KEY") {
            // DEMO MODE: Return mock data for Karnataka markets
            return listOf(
                MandiRecord("Hubli", "Dharwad", "Paddy", "Common", "2024-05-20", "2100", "2350", "2280"),
                MandiRecord("Mandya", "Mandya", "Sugarcane", "Local", "2024-05-20", "3100", "3400", "3250"),
                MandiRecord("Mysuru", "Mysuru", "Ragi", "Regular", "2024-05-20", "3800", "4200", "4050"),
                MandiRecord("Belagavi", "Belagavi", "Maize", "Hybrid", "2024-05-20", "1900", "2100", "2020"),
                MandiRecord("Raichur", "Raichur", "Paddy", "Sona Masuri", "2024-05-20", "2400", "2800", "2650")
            ).filter { record -> 
                cropNames.any { crop -> record.commodity.contains(crop, ignoreCase = true) }
            }
        }

        return runCatching {
            val response = api.getMandiPrices(apiKey)
            response.records.filter { record ->
                cropNames.any { crop -> record.commodity.contains(crop, ignoreCase = true) }
            }
        }.getOrDefault(emptyList())
    }
}
