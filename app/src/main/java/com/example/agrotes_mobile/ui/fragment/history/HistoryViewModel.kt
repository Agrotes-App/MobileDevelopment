package com.example.agrotes_mobile.ui.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.repository.user.UserRepository

class HistoryViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getAllHistory(): LiveData<List<DiseaseEntity>> = userRepository.getAllHistory()
}