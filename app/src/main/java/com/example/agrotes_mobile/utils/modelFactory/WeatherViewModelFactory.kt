package com.example.agrotes_mobile.utils.modelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agrotes_mobile.di.weather.WeatherInjection
import com.example.agrotes_mobile.repository.weather.WeatherRepository
import com.example.agrotes_mobile.ui.fragment.home.WeatherViewModel

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory private constructor(private val weatherRepository: WeatherRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WeatherViewModel::class.java) -> {
                WeatherViewModel(weatherRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherViewModelFactory? = null

        @JvmStatic
        fun getInstance(): WeatherViewModelFactory {
            if (INSTANCE == null) {
                synchronized(WeatherViewModelFactory::class.java) {
                    INSTANCE = WeatherViewModelFactory(WeatherInjection.provideUserRepository())
                }
            }
            return INSTANCE as WeatherViewModelFactory
        }
    }
}