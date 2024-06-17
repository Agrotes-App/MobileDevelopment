package com.example.agrotes_mobile.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.data.remote.responses.disease.DiseaseResponses
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.repository.user.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllDisease(): LiveData<Result<List<DiseaseResponses>>> = userRepository.getAllDiseases()
    fun getWeather(lat: Double?, lon: Double?) = userRepository.getWeather(lat, lon)
}