package com.example.agrotes_mobile.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.data.Result
import com.example.agrotes_mobile.data.remote.responses.StoryResponse
import com.example.agrotes_mobile.repository.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllDisease(): LiveData<Result<StoryResponse>> = userRepository.getAllDisease()
}