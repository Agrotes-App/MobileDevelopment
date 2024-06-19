package com.example.agrotes_mobile.ui.activities.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.agrotes_mobile.data.local.entity.UserEntity
import com.example.agrotes_mobile.repository.app.UserRepository

class EditProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getUserById(id: String?) = userRepository.getUserById(id)
    fun getSession(): LiveData<UserEntity> = userRepository.getSession().asLiveData()
    fun updateProfile(username: String, email: String) = userRepository.updateProfile(username, email)
}