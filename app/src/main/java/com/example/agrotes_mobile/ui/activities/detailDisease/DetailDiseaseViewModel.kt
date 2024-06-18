package com.example.agrotes_mobile.ui.activities.detailDisease

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.data.remote.responses.disease.DiseaseResponses
import com.example.agrotes_mobile.repository.user.UserRepository
import com.example.agrotes_mobile.utils.Result

class DetailDiseaseViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getDiseaseById(id: String?): LiveData<Result<DiseaseResponses>> = userRepository.getDiseaseById(id)
}