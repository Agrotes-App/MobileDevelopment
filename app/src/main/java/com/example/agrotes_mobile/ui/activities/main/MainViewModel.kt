package com.example.agrotes_mobile.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.agrotes_mobile.data.pref.UserModel
import com.example.agrotes_mobile.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()
}