package com.example.agrotes_mobile.ui.fragment.home

import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.repository.weather.WeatherRepository

class WeatherViewModel(private val weatherRepository: WeatherRepository): ViewModel() {
    fun getWeather(lat: Double?, lon: Double?) = weatherRepository.getWeather(lat, lon)

}