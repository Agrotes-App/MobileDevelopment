package com.example.agrotes_mobile.data.remote.retrofit.weather

import com.example.agrotes_mobile.BuildConfig
import com.example.agrotes_mobile.data.remote.responses.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
    ): WeatherResponse
}