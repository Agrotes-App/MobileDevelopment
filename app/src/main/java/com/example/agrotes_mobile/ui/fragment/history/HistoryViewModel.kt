package com.example.agrotes_mobile.ui.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.repository.app.UserRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getAllHistory(): LiveData<List<DiseaseEntity>> = userRepository.getAllHistory()
    fun delete(entity: DiseaseEntity) = viewModelScope.launch { userRepository.delete(entity) }
}