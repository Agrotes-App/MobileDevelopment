package com.example.agrotes_mobile.di.weather

import android.content.Context
import com.example.agrotes_mobile.data.local.room.DiseaseRoomDatabase
import com.example.agrotes_mobile.data.pref.UserPreference
import com.example.agrotes_mobile.data.pref.dataStore
import com.example.agrotes_mobile.data.remote.retrofit.app.ApiConfig
import com.example.agrotes_mobile.data.remote.retrofit.weather.WeatherConfig
import com.example.agrotes_mobile.repository.user.UserRepository
import com.example.agrotes_mobile.repository.weather.WeatherRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object WeatherInjection {
    fun provideUserRepository(): WeatherRepository {
        val apiService = WeatherConfig.getApiService()
        return WeatherRepository.getInstance(apiService)
    }
}