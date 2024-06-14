package com.example.challengeb2ra.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("fdsnws/event/1/query?format=geojson")
    suspend fun getEarthquakes(
        @Query("starttime") startTime: String? = null,
        @Query("endtime") endTime: String? = null
    ): ApiResponse
}

object Api {
    private const val BASE_URL = "https://earthquake.usgs.gov/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

data class ApiResponse(
    val features: List<Earthquake>
)