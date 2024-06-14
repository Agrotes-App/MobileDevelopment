package com.example.agrotes_mobile.ui.activities.prediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.repository.DiseaseRepository
import kotlinx.coroutines.launch

class PredictionViewModel(private val diseaseRepository: DiseaseRepository): ViewModel() {
    fun insert(entity: DiseaseEntity) = viewModelScope  .launch { diseaseRepository.insert(entity) }
}