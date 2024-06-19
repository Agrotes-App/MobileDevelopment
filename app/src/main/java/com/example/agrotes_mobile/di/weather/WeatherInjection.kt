package com.example.agrotes_mobile.di.weather

import com.example.agrotes_mobile.data.remote.retrofit.weather.WeatherConfig
import com.example.agrotes_mobile.repository.weather.WeatherRepository

// Weather Injection
object WeatherInjection {
    fun provideUserRepository(): WeatherRepository {
        val apiService = WeatherConfig.getApiService()
        return WeatherRepository.getInstance(apiService)
    }
}