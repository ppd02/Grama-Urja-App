package com.gramaurja2.app.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface MandiApiService {
    @GET("resource/9ef27131-f255-4551-b251-5079a0194848")
    suspend fun getMandiPrices(
        @Query("api-key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("filters[state]") state: String = "Karnataka",
        @Query("limit") limit: Int = 50
    ): MandiResponse
}

data class MandiResponse(
    val records: List<MandiRecord>
)

data class MandiRecord(
    val market: String,
    val district: String,
    val commodity: String,
    val variety: String,
    val arrival_date: String,
    val min_price: String,
    val max_price: String,
    val modal_price: String
)
