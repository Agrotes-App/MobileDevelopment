package com.example.agrotes_mobile.repository.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.agrotes_mobile.data.remote.responses.weather.WeatherResponse
import com.example.agrotes_mobile.data.remote.retrofit.weather.WeatherApiService
import com.example.agrotes_mobile.data.remote.retrofit.weather.WeatherConfig
import com.example.agrotes_mobile.utils.Result

class WeatherRepository(private var weatherApiService: WeatherApiService) {

    fun getWeather(lat: Double?, lon: Double?): LiveData<Result<WeatherResponse>> = liveData {
        emit(Result.Loading)
        try {
            weatherApiService = WeatherConfig.getApiService()
            val result = weatherApiService.getWeather(lat, lon)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: WeatherRepository? = null
        fun getInstance(weatherApiService: WeatherApiService): WeatherRepository = instance ?: synchronized(this) {
            instance ?: WeatherRepository(weatherApiService)
        }.also { instance = it }
    }
}