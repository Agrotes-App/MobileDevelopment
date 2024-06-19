package com.example.agrotes_mobile.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.agrotes_mobile.data.local.entity.UserEntity
import com.example.agrotes_mobile.repository.app.UserRepository

class MainViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getSession(): LiveData<UserEntity> = userRepository.getSession().asLiveData()
}