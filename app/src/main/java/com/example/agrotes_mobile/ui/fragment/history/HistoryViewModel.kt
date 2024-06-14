package com.example.agrotes_mobile.ui.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.repository.DiseaseRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val diseaseRepository: DiseaseRepository): ViewModel() {

    fun getAllHistory(): LiveData<List<DiseaseEntity>> = diseaseRepository.getAllHistory()
}