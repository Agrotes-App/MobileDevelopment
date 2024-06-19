package com.example.agrotes_mobile.ui.activities.prediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.repository.app.UserRepository
import kotlinx.coroutines.launch

class PredictionViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getDiseaseByName(name: String?) = userRepository.getDiseaseByName(name)
    fun insert(entity: DiseaseEntity) = viewModelScope  .launch { userRepository.insert(entity) }
}