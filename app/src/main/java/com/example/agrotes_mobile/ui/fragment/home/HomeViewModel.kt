package com.example.agrotes_mobile.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.data.remote.responses.disease.DiseaseResponses
import com.example.agrotes_mobile.utils.helper.Result
import com.example.agrotes_mobile.repository.app.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllDisease(): LiveData<Result<List<DiseaseResponses>>> = userRepository.getAllDiseases()
}